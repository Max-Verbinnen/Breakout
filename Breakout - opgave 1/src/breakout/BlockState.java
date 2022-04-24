package breakout;

import java.awt.Color;

/**
 * Represents the state of a block in the breakout game.
 *
 * @immutable
 * @invar | getLocation() != null
 */
public abstract class BlockState {
	
	/**
	 * @invar | location != null
	 */
	private final Rect location;

	/**
	 * Construct a block occupying a given rectangle in the field.
	 * 
	 * @pre | location != null
	 * @post | getLocation().equals(location)
	 */
	public BlockState(Rect location) {
		this.location = location;
	}

	/**
	 * Return the rectangle occupied by this block in the field.
	 * 
	 * @inspects | this
	 */
	public Rect getLocation() {
		return location;
	}
	
	/**
	 * Determine if block needs to be removed, return true if removed.
	 * 
	 * @pre | game != null
	 * @pre | ball != null
	 */
	public abstract boolean handleCollision(BreakoutState game, Ball ball);
	
	/**
	 * Return the color of the block.
	 * 
	 * @inspects | this
	 * 
	 * @creates | result
	 */
	public abstract Color getColor();
	
}
