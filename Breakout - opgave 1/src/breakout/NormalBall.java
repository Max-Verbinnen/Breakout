package breakout;

import java.awt.Color;

/**
 * Represents the state of a normal ball in the breakout game.
 * 
 * @invar | getLocation() != null
 * @invar | getVelocity() != null
 */
public class NormalBall extends Ball {

	/**
	 * Construct a new normal ball at a given `location`, with a given `velocity`.
	 * 
	 * @pre | location != null
	 * @pre | velocity != null
	 * @post | getLocation() == location
	 * @post | getVelocity() == velocity
	 */
	public NormalBall(Circle location, Vector velocity) {
		super(location, velocity);
	}
	
	/**
	 * Return a copy of the ball given as argument with a given velocity vector.
	 * 
	 * @pre | ball != null
	 * @pre | velocity != null
	 * 
	 * @post | result != null
	 * @post | result.getLocation() == ball.getLocation()
	 * @post | result.getVelocity() == velocity
	 * @post | result.getLifetime() == ball.getLifetime()
	 * 
	 * @creates | result
	 */
	@Override
	public NormalBall copyBall(Ball ball, Vector velocity) {
		return new NormalBall(ball.getLocation(), velocity);
	}

	/**
	 * Changes the velocity of the ball after hitting a rectangle.
	 * 
	 * @pre | rect != null
	 * 
	 * @post | getVelocity().equals(old(bounceOn(rect)))
	 * 
	 * @mutates | this
	 */
	@Override
	public void hitBlock(Rect rect, boolean destroyed) {
		Vector newVelocity = bounceOn(rect);
		setVelocity(newVelocity);
	}
	
	/**
	 * Return color of the ball.
	 * 
	 * @inspects | this
	 * @creates | result
	 */
	@Override
	public Color getColor() {
		return Color.yellow;
	}

}
