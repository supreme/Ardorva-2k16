package org.hyperion.rs2;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.hyperion.rs2.model.World;
import org.hyperion.util.Logger;
import org.hyperion.util.Logger.Level;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

/**
 * Starts everything else including MINA and the <code>GameEngine</code>.
 * @author Graham Edgecombe
 * 
 */
public class RS2Server {

	/**
	 * The port to listen on.
	 */
	public static final int PORT = 43594;

	/**
	 * The <code>IoAcceptor</code> instance.
	 */
	private final IoAcceptor acceptor = new NioSocketAcceptor();

	/**
	 * The <code>GameEngine</code> instance.
	 */
	private static final GameEngine engine = new GameEngine();

	/**
	 * Creates the server and the <code>GameEngine</code> and initializes the
	 * <code>World</code>.
	 * @throws IOException if an I/O error occurs loading the world.
	 * @throws ClassNotFoundException if a class the world loads was not found.
	 * @throws IllegalAccessException if a class loaded by the world was not accessible.
	 * @throws InstantiationException if a class loaded by the world was not created.
	 */
	public RS2Server() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		World.getWorld().init(engine);
		acceptor.setHandler(new ConnectionHandler());
		//acceptor.getFilterChain().addFirst("throttleFilter", new ConnectionThrottleFilter());
	}

	/**
	 * Binds the server to the specified port.
	 * @param port The port to bind to.
	 * @return The server instance, for chaining.
	 * @throws IOException
	 */
	public RS2Server bind(int port) throws IOException {
		Logger.log(Level.CORE, "Binding to port: " + port + "...");
		//TODO: Set reuse address
		acceptor.bind(new InetSocketAddress(port));
		return this;
	}

	/**
	 * Starts the <code>GameEngine</code>.
	 * @throws ExecutionException if an error occured during background loading.
	 */
	public void start() throws ExecutionException {
		ScriptManager.getScriptManager().loadScripts(Constants.SCRIPTS_DIRECTORY);
		if(World.getWorld().getBackgroundLoader().getPendingTaskAmount() > 0) {
			Logger.log(Level.CORE, "Waiting for pending background loading tasks...");
			World.getWorld().getBackgroundLoader().waitForPendingTasks();
		}
		World.getWorld().getBackgroundLoader().shutdown();
		engine.start();
		Logger.log(Level.CORE, "Ready");
	}

	/**
	 * Gets the <code>GameEngine</code>.
	 * @return The game engine.
	 */
	public static GameEngine getEngine() {
		return engine;
	}
	
}
