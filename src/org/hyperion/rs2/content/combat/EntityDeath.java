package org.hyperion.rs2.content.combat;

import java.util.ArrayList;

import org.hyperion.rs2.event.Event;
import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.GroundItem;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.Container.Type;
import org.hyperion.rs2.model.npc.NPC;
import org.hyperion.rs2.model.player.Player;

/**
 * Handles the death of an entity.
 * @author Stephen
 */
public class EntityDeath {

	/**
	 * The loot, if any.
	 */
	private static ArrayList<Item> loot = new ArrayList<Item>();
	
	/**
	 * Sends the death call to the specified entity.
	 * @param entity The entity awaiting death.
	 */
	public static void sendDeath(Entity entity) {
		if (entity instanceof Player) {
			handlePlayerDeath((Player) entity);
		} else {
			handleNPCDeath((NPC) entity);
		}
	}
	
	/**
	 * Handles a player death.
	 * @param player The player who has died.
	 */
	private static void handlePlayerDeath(final Player player) {
		/* Notify the soon to be raging player */
		player.getActionSender().sendMessage("Oh dear, you are dead.");
		
		/* Get the killer of the player */
		Entity killer = player.getDamageMap().getWinner();
		
		/* The effects of dying */
		player.setCanWalk(false);
		player.playAnimation(Entity.DEATH_ANIMATION);
		World.getWorld().submit(new Event(3000) {

			@Override
			public void execute() {
				/* Spawn player at default location */
				player.setLocation(Entity.DEFAULT_LOCATION);
				
				/* Remove items lost and display loot for killer */
				removeDeathItems(player);
				displayLoot(player, player); //TODO
				player.setDead(false);
			}
			
		});
	}
	
	/**
	 * Removes the items lost in the player's death.
	 * @param Player The player who has died.
	 */
	private static void removeDeathItems(Player player) {
		/* If a player is skulled remove all items and immediately return */
		if (player.isSkulled()) {
			player.getEquipment().clear();
			player.getInventory().clear();
			return;
		}
		
		/* An allotment of all items a player is carrying */
		int equipped = player.getEquipment().size();
		int holding = player.getInventory().size();
		Container sum = new Container(Type.STANDARD, equipped + holding);
		
		/* Populate the sum container */
		for (Item item : player.getEquipment().toArray()) {
			sum.add(item);
		}
		for (Item item : player.getInventory().toArray()) {
			sum.add(item);
		}
		
		/* Determine which items are kept */
		int toKeep = 3;
		//TODO Prayer - protect item.
		Container keeping = new Container(Type.STANDARD, toKeep);
		for (Item item : sum.toArray()) {
			for (Item kept : keeping.toArray()) {
				if (item.getDefinition().getValue() > kept.getDefinition().getValue()) {
					kept = item;
				}
			}
		}
		
		/* Lastly, set the player's inventory to the keeping container while populating the loot list */
		player.getEquipment().clear();
		player.getInventory().clear();
		for (Item item : keeping.toArray()) {
			player.getInventory().add(item);
		}
		for (Item item : sum.toArray()) {
			if (!keeping.hasItem(item)) {
				loot.add(item);
			}
		}
	}
	
	/**
	 * Displays the player's dropped items as ground items.
	 * @param killer The entity who killed the player.
	 * @param player The player who has died.
	 * @param loot The killers loot.
	 */
	private static void displayLoot(Entity killer, Player player) {
		/* If the loot is empty, immediately return */
		if (loot.size() <= 0) {
			return;
		}
		
		/* If the player died to an NPC, display global ground items */
		if (killer instanceof NPC) {
			for (Item item : loot) {
				GroundItem groundItem = new GroundItem(item, player, player.getLocation(), player.getRegion(), true);
				for (Player bystander : player.getRegion().getPlayers()) {
					bystander.getActionSender().sendGroundItem(groundItem);
					bystander.getRegion().addGroundItem(groundItem);
				}
			}
			return;
		}
		
		/* If the killer is another player, display loot only to them */
		if (killer instanceof Player) {
			for (Item item : loot) {
				GroundItem groundItem = new GroundItem(item, killer, player.getLocation(), player.getRegion(), false);
				((Player) killer).getActionSender().sendGroundItem(groundItem);
				killer.getRegion().addGroundItem(groundItem);
			}
		}
	}
	
	/**
	 * Handles the death of an NPC.
	 * @param npc The npc who has died.
	 */
	private static void handleNPCDeath(final NPC npc) {
		Location deathLocation = npc.getLocation(); //TODO: Change this to load from saved spot - Stephen
		npc.playAnimation(Animation.create(npc.getDefinition().getDeathAnimation()));
		
		/* Create an event for the duration of the death animation */
		World.getWorld().submit(new Event(2400) {

			@Override
			public void execute() {
				
				//World.getWorld().unregister(npc);
				//npc.reset();
				
				World.getWorld().submit(new Event(3600) {

					@Override
					public void execute() {
						npc.playAnimation(Animation.create(-1));
						npc.setDead(false);
						npc.setHealth(npc.getDefinition().getHitpoints());
					}
					
				});
			}
			
		});
		
		/*Random random = new Random();
		Entity killer = npc.inflictedMostDamage();
		NpcDrop drop = dropLoader.drops.get((int) npc.getId());
		
		int playerChance = random.nextInt(100) / 100;
		if (drop.getDropChance() >= playerChance) {
			GroundItemEvent.addFloorItem(new GroundItem(drop.getItemId(), drop.getItemAmount(), npc.getLocation(), killer, killer, false));
		}*/
	}
}
