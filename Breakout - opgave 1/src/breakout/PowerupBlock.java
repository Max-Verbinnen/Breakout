package breakout;

import java.awt.Color;

/**
 * Represents the state of a powerup block in the breakout game.
 *
 * @immutable
 * @invar | getLocation() != null
 */
public class PowerupBlock extends BlockState {

	/**
	 * Construct a powerup block occupying a given rectangle in the field.
	 * 
	 * @pre | location != null
	 * @post | getLocation().equals(location)
	 */
	public PowerupBlock(Rect location) {
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
		game.makeBallSupercharged(ball);
		return false; // So that ball bounces when it hits powerup block
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
		return Color.magenta;
	}

}
