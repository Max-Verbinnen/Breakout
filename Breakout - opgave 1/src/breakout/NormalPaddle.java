package breakout;

import java.awt.Color;

/**
 * Represents the state of a normal paddle in the breakout game.
 *
 * @immutable
 * @invar | getCenter() != null
 */
public class NormalPaddle extends PaddleState {

	/**
	 * Construct a normal paddle located around a given center in the field.
	 * 
	 * @pre | center != null
	 * @post | getCenter().equals(center)
	 */
	public NormalPaddle(Point center) {
		super(center);
	}
	
	/**
	 * Return a new paddle.
	 * 
	 * @pre | center != null
	 * 
	 * @post | result.getCenter().equals(center)
	 * 
	 * @creates | result
	 */
	public NormalPaddle createPaddle(Point center) {
		return new NormalPaddle(center);
	}
	
	/**
	 * Returns how many additional balls should be created at collision.
	 * 
	 * @pre | game != null
	 * 
	 * @post | result == 0
	 * 
	 * @inspects | this
	 */
	@Override
	public int handleCollision(BreakoutState game) {
		return 0;
	}
	
	/**
	 * Return the color of the paddle.
	 * 
	 * @inspects | this
	 * 
	 * @creates | result
	 */
	@Override
	public Color getColor() {
		return new Color(0x99,0xff,0xff);
	}
	
}
