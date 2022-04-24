package breakout;

import java.awt.Color;

/**
 * Represents the state of a normal block in the breakout game.
 *
 * @immutable
 * @invar | getLocation() != null
 */
public class NormalBlock extends BlockState {

	/**
	 * Construct a normal block occupying a given rectangle in the field.
	 * 
	 * @pre | location != null
	 * @post | getLocation().equals(location)
	 */
	public NormalBlock(Rect location) {
		super(location);
	}
	
	/**
	 * Determine if block needs to be removed, return true if removed.
	 * 
	 * @pre | game != null
	 * @pre | ball != null
	 * 
	 * @inspects | this
	 */
	@Override
	public boolean handleCollision(BreakoutState game, Ball ball) {
		game.removeBlock(this);
		return true;
	}
	
	/**
	 * Return the color of the block.
	 * 
	 * @inspects | this
	 * 
	 * @creates | result
	 */
	@Override
	public Color getColor() {
		return new Color(0x80,0x00,0xff);
	}
}
