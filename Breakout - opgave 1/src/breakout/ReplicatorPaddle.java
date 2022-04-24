package breakout;

import java.awt.Color;

/**
 * Represents the state of a replicator paddle in the breakout game.
 *
 * @immutable
 * @invar | getCenter() != null
 * @invar | getTimes() >= 0
 */
public class ReplicatorPaddle extends PaddleState {
	
	/**
	 * @invar | times >= 0
	 */
	private int times;
	
	/**
	 * Construct a replicator paddle located around a given center in the field.
	 * 
	 * @pre | center != null
	 * 
	 * @post | getCenter().equals(center)
	 * @post | getTimes() == times
	 */
	public ReplicatorPaddle(Point center, int times) {
		super(center);
		this.times = times;
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
	public ReplicatorPaddle createPaddle(Point center) {
		return new ReplicatorPaddle(center, times);
	}
	
	/**
	 * Returns the times it can be hit before it transforms into a normal paddle.
	 * 
	 * @inspects | this
	 */
	public int getTimes() {
		return times;
	}
	
	/**
	 * Returns how many additional balls should be created at collision.
	 * 
	 * @pre | game != null
	 * 
	 * @post | result >= 0 && result <= 3
	 * 
	 * @inspects | this
	 */
	@Override
	public int handleCollision(BreakoutState game) {		
		if (times - 1 == 0) {
			game.makePaddleNormal();
		}
		
		return times--;
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
		return new Color(0xfa, 0xff, 0x7a);
	}

}
