package org.hyperion.rs2.model;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.GameEngine;
import org.hyperion.rs2.GenericWorldLoader;
import org.hyperion.rs2.WorldLoader;
import org.hyperion.rs2.WorldLoader.LoginResult;
import org.hyperion.rs2.cache.Cache;
import org.hyperion.rs2.event.Event;
import org.hyperion.rs2.event.EventManager;
import org.hyperion.rs2.event.impl.CleanupEvent;
import org.hyperion.rs2.event.impl.UpdateEvent;
import org.hyperion.rs2.model.definitions.DefinitionLoader;
import org.hyperion.rs2.model.npc.NPC;
import org.hyperion.rs2.model.npc.SpawnLoader;
import org.hyperion.rs2.model.object.ObjectManager;
import org.hyperion.rs2.model.player.Player;
import org.hyperion.rs2.model.player.PlayerDetails;
import org.hyperion.rs2.model.region.Mapdata;
import org.hyperion.rs2.model.region.RegionManager;
import org.hyperion.rs2.net.PacketBuilder;
import org.hyperion.rs2.net.PacketManager;
import org.hyperion.rs2.packet.PacketHandler;
import org.hyperion.rs2.task.Task;
import org.hyperion.rs2.task.impl.SessionLoginTask;
import org.hyperion.rs2.util.ConfigurationParser;
import org.hyperion.rs2.util.EntityList;
import org.hyperion.rs2.util.NameUtils;
import org.hyperion.util.BlockingExecutorService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.logging.Logger;

/**
 * Holds data global to the game world.
 * @author Graham Edgecombe
 *
 */
public class World {
	
	/**
	 * Logging class.
	 */
	private static final Logger logger = Logger.getLogger(World.class.getName());
	
	/**
	 * World instance.
	 */
	private static final World world = new World();
	
	/**
	 * Gets the world instance.
	 * @return The world instance.
	 */
	public static World getWorld() {
		return world;
	}
	
	/**
	 * An executor service which handles background loading tasks.
	 */
	private BlockingExecutorService backgroundLoader = new BlockingExecutorService(Executors.newSingleThreadExecutor());

	/**
	 * The game engine.
	 */
	private GameEngine engine;
	
	/**
	 * The event manager.
	 */
	private EventManager eventManager;
	
	/**
	 * The current loader implementation.
	 */
	private WorldLoader loader;

	/**
	 * Loads required server definitions. Items, objects, etc...
	 */
	private DefinitionLoader definitionLoader = new DefinitionLoader();
	
	/**
	 * A list of connected players.
	 */
	private EntityList<Player> players = new EntityList<Player>(Constants.MAX_PLAYERS);
	
	/**
	 * A list of active NPCs.
	 */
	private EntityList<NPC> npcs = new EntityList<NPC>(Constants.MAX_NPCS);
	
	/**
	 * The region manager.
	 */
	private RegionManager regionManager = new RegionManager();

	/**
	 * The object manager.
	 */
	private ObjectManager objectManager = new ObjectManager();

	/**
	 * Mapdata instance for XTEAs.
	 */
	private Mapdata mapdata;

	/**
	 * Creates the world and begins background loading tasks.
	 */
	public World() {
		backgroundLoader.submit(() -> {
			try {
				Cache.init();
				mapdata = new Mapdata();
				//XTEALoader.loadOld(new File("./data/xtea/"));
			} catch (IOException e) {
				e.printStackTrace();
			}
        });
		backgroundLoader.submit(() -> {
            try {
                definitionLoader.loadItemDefinitions();
                definitionLoader.loadNPCDefinitions();
                definitionLoader.loadBonusDefinitions();
                definitionLoader.loadShops();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
		backgroundLoader.submit(() -> {
            try {
                SpawnLoader.loadSpawns();
                objectManager.loadObjects();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
	}
	
	/**
	 * Gets the background loader.
	 * @return The background loader.
	 */
	public BlockingExecutorService getBackgroundLoader() {
		return backgroundLoader;
	}
	
	/**
	 * Gets the region manager.
	 * @return The region manager.
	 */
	public RegionManager getRegionManager() {
		return regionManager;
	}

	/**
	 * Gets the object manager.
	 * @return The object manager.
	 */
	public ObjectManager getObjectManager() {
		return objectManager;
	}
	
	/**
	 * Initialises the world: loading configuration and registering global
	 * events.
	 * @param engine The engine processing this world's tasks.
	 * @throws IOException if an I/O error occurs loading configuration.
	 * @throws ClassNotFoundException if a class loaded through reflection was not found.
	 * @throws IllegalAccessException if a class could not be accessed.
	 * @throws InstantiationException if a class could not be created.
	 * @throws IllegalStateException if the world is already initialised.
	 */
	public void init(GameEngine engine) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		if(this.engine != null) {
			throw new IllegalStateException("The world has already been initialised.");
		} else {
			this.engine = engine;
			this.eventManager = new EventManager(engine);
			this.registerGlobalEvents();
			this.loadConfiguration();
		}
	}
	
	/**
	 * Loads server configuration.
	 * @throws IOException if an I/O error occurs.
	 * @throws ClassNotFoundException if a class loaded through reflection was not found.
	 * @throws IllegalAccessException if a class could not be accessed.
	 * @throws InstantiationException if a class could not be created.
	 */
	private void loadConfiguration() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		FileInputStream fis = new FileInputStream("data/configuration.cfg");
		try {
			ConfigurationParser p = new ConfigurationParser(fis);
			Map<String, String> mappings = p.getMappings();
			if(mappings.containsKey("worldLoader")) {
				String worldLoaderClass = mappings.get("worldLoader");
				Class<?> loader = Class.forName(worldLoaderClass);
				this.loader = (WorldLoader) loader.newInstance();
				logger.fine("WorldLoader set to : " + worldLoaderClass);
			} else {
				this.loader = new GenericWorldLoader();
				logger.fine("WorldLoader set to default");
			}
			Map<String, Map<String, String>> complexMappings = p.getComplexMappings();
			if(complexMappings.containsKey("packetHandlers")) {
				Map<Class<?>, Object> loadedHandlers = new HashMap<Class<?>, Object>();
				for(Map.Entry<String, String> handler : complexMappings.get("packetHandlers").entrySet()) {
					int id = Integer.parseInt(handler.getKey());
					Class<?> handlerClass = Class.forName(handler.getValue());
					Object handlerInstance;
					if(loadedHandlers.containsKey(handlerClass)) {
						handlerInstance = loadedHandlers.get(loadedHandlers.get(handlerClass));
					} else {
						handlerInstance = handlerClass.newInstance();
					}
					PacketManager.getPacketManager().bind(id, (PacketHandler) handlerInstance);
					logger.fine("Bound " + handler.getValue() + " to opcode : " + id);
				}
			}
		} finally {
			fis.close();
		}
	}
	
	/**
	 * Registers global events such as updating.
	 */
	private void registerGlobalEvents() {
		submit(new UpdateEvent());
		submit(new CleanupEvent());
	}
	
	/**
	 * Submits a new event.
	 * @param event The event to submit.
	 */
	public void submit(Event event) {
		this.eventManager.submit(event);
	}
	
	/**
	 * Submits a new task.
	 * @param task The task to submit.
	 */
	public void submit(Task task) {
		this.engine.pushTask(task);
	}
	
	/**
	 * Gets the world loader.
	 * @return The world loader.
	 */
	public WorldLoader getWorldLoader() {
		return loader;
	}
	
	/**
	 * Gets the game engine.
	 * @return The game engine.
	 */
	public GameEngine getEngine() {
		return engine;
	}
	
	/**
	 * Loads a player's game in the work service.
	 * @param pd The player's details.
	 */
	public void load(final PlayerDetails pd) {
		engine.submitWork(() -> {
            LoginResult lr = loader.checkLogin(pd);
            int code = lr.getReturnCode();
            if(!NameUtils.isValidName(pd.getName())) {
                code = 11;
            }
            if(code != 2) {
                PacketBuilder bldr = new PacketBuilder();
                bldr.put((byte) code);
                pd.getSession().write(bldr.toPacket()).addListener(future -> future.getSession().close(false));
            } else {
                lr.getPlayer().getSession().setAttribute("player", lr.getPlayer());
                loader.loadPlayer(lr.getPlayer());
                engine.pushTask(new SessionLoginTask(lr.getPlayer()));
            }
        });
	}
	
	/**
	 * Registers a new npc.
	 * @param npc The npc to register.
	 */
	public void register(NPC npc) {
		npcs.add(npc);
		npc.getCombatUtility().refresh();
	}
	
	/**
	 * Unregisters an old npc.
	 * @param npc The npc to unregister.
	 */
	public void unregister(NPC npc) {
		npcs.remove(npc);
		npc.destroy();
	}

	/**
	 * Registers a new player.
	 * @param player The player to register.
	 */
	public void register(final Player player) {
		// do final checks e.g. is player online? is world full?
		int returnCode = 2;
		if(isPlayerOnline(player.getName())) {
			returnCode = 5;
		} else {
			if(!players.add(player)) {
				returnCode = 7;
				logger.info("Could not register player : " + player + " [world full]");
			}
		}
		final int fReturnCode = returnCode;
		PacketBuilder bldr = new PacketBuilder();
		bldr.put((byte) returnCode);
		bldr.put((byte) player.getRights().toInteger());
		bldr.put((byte) 0);
		bldr.putShort(player.getIndex());
		bldr.put((byte) 1);
		player.getSession().write(bldr.toPacket()).addListener(future -> {
            if(fReturnCode != 2) {
                player.getSession().close(false);
            } else {
                player.getActionSender().sendLogin();
            }
        });
		if(returnCode == 2) {
			System.out.println("Registered player : " + player + " [online=" + players.size() + "]");
		}
	}

	/**
	 * Gets the definition loader.
	 * @return The definition loader.
	 */
	public DefinitionLoader getDefinitionLoader() {
		return definitionLoader;
	}

	/** 
	 * Gets the player list.
	 * @return The player list.
	 */
	public EntityList<Player> getPlayers() {
		return players;
	}
	
	/**
	 * Gets the npc list.
	 * @return The npc list.
	 */
	public EntityList<NPC> getNPCs() {
		return npcs;
	}
	
	/**
	 * Checks if a player is online.
	 * @param name The player's name.
	 * @return <code>true</code> if they are online, <code>false</code> if not.
	 */
	public boolean isPlayerOnline(String name) {
		final String formatted = NameUtils.formatName(name);
		Predicate<Player> matchingName = p -> p.getName().equalsIgnoreCase(formatted);
		return players.stream().filter(matchingName).count() > 0;
	}

	/**
	 * Unregisters a player, and saves their game.
	 * @param player The player to unregister.
	 */
	public void unregister(final Player player) {
		player.getActionQueue().cancelQueuedActions();
		player.destroy();
		player.getSession().close(false);
		players.remove(player);
		logger.info("Unregistered player : " + player + " [online=" + players.size() + "]");
		engine.submitWork(() -> loader.savePlayer(player));
	}
	
	/**
	 * Gets a player by their name.
	 * @param name The username of the player.
	 */
	public Player getPlayerByName(String name) {
		for (Player player : players) {
			if (player != null) {
				if (player.getName().equalsIgnoreCase(name)) {
					return player;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Gets an NPC based on their index.
	 * @param index The index.
	 * @return The NPC.
	 */
	public NPC getNPC(int index) {
		return (NPC) npcs.get(index);
	}

	/**
	 * Handles an exception in any of the pools.
	 * @param t The exception.
	 */
	public void handleError(Throwable t) {
		logger.severe("An error occurred in an executor service! The server will be halted immediately.");
		t.printStackTrace();
		System.exit(1);
	}
}
