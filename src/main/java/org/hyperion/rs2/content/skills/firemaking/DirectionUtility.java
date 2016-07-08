package org.hyperion.rs2.content.skills.firemaking;

import org.hyperion.rs2.model.Tile;
import org.hyperion.rs2.model.player.Player;

/**
 * Provides clipping for firemaking.
 * @author Nikki
 * @author Stephen Andrews
 */
public class DirectionUtility {

	/**
	 * The radius in which we look for open tiles to move to.
	 */
	public final int RADIUS = 1;
	
	/**
	 * The possible directions we can move.
	 */
	public enum Direction {
		
		/**
		 * Moving in the East direction.
		 */
		EAST,
		
		/**
		 * Moving in the West direction.
		 */
		WEST,
		
		/**
		 * No available direction to move to.
		 */
		NONE;
	}
	
	/**
	 * Gets a valid direction to which we can move.
	 * @param player The player attempting to light a fire.
	 * @return A valid direction.
	 */
	public Direction getValidDirection(Player player) {
		/*TileMapBuilder bldr = new TileMapBuilder(player.getLocation(), RADIUS);

		TileMap map = bldr.build();

		Tile tile = map.getTile(RADIUS, RADIUS);
		Tile westTile = map.getTile(RADIUS - 1, RADIUS);
		Tile eastTile = map.getTile(RADIUS + 1, RADIUS);

		if (tile.isWesternTraversalPermitted() && westTile.isEasternTraversalPermitted()) {
			return Direction.WEST;
		} else if (tile.isEasternTraversalPermitted() && eastTile.isWesternTraversalPermitted()) {
			return Direction.EAST;
		} else {
			return Direction.NONE;
		}*/
		
		return null; //temporary
	}
	
	/**
	 * Moves the player to the appropriate clipped location.
	 * @param player The player lighting the fire.
	 */
	public void clippedMove(Player player) {
		Direction direction = getValidDirection(player);
		
		if (direction == Direction.WEST) {
			player.getWalkingQueue().addStep(player.getLocation().getX() - 1,
					player.getLocation().getY());
		} else if (direction == Direction.EAST) {
			player.getWalkingQueue().addStep(player.getLocation().getX() + 1,
					player.getLocation().getY());
		} else {
			// Cannot move!
		}
	}
}
