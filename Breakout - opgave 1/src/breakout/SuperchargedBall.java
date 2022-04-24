package breakout;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Represents the state of a supsercharged ball in the breakout game.
 * 
 * @invar | getLocation() != null
 * @invar | getVelocity() != null
 * @invar | getLifetime() >= -BreakoutState.MAX_ELAPSED_TIME
 */
public class SuperchargedBall extends Ball {
	
	/**
	 * @invar | lifetime >= -BreakoutState.MAX_ELAPSED_TIME
	 */
	private int lifetime; // In milliseconds

	/**
	 * Construct a new supercharged ball at a given `location`, with a given `velocity` and `lifetime`.
	 * 
	 * @pre | location != null
	 * @pre | velocity != null
	 * @post | getLocation() == location
	 * @post | getVelocity() == velocity
	 * @post | getLifetime() == lifetime
	 */
	public SuperchargedBall(Circle location, Vector velocity, int lifetime) {
		super(location, velocity);
		this.lifetime = lifetime;
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
	public SuperchargedBall copyBall(Ball ball, Vector velocity) {
		return new SuperchargedBall(ball.getLocation(), velocity, ball.getLifetime());
	}

	/**
	 * Changes the velocity of the ball after hitting a rectangle.
	 * 
	 * @pre | rect != null
	 * 
	 * @post | (destroyed) ? getVelocity().equals(old(getVelocity())) : getVelocity().equals(old(bounceOn(rect)))
	 * 
	 * @mutates | this
	 */
	@Override
	public void hitBlock(Rect rect, boolean destroyed) {
		if (destroyed) {
			// Ball should maintain its velocity
		} else {
			Vector newVelocity = bounceOn(rect);
			setVelocity(newVelocity);
		}
	}
	
	/**
	 * Return color of the ball.
	 * 
	 * @inspects | this
	 * @creates | result
	 */
	@Override
	public Color getColor() {
		float hue = (float) ((getLifetime() % 2500) / 2500.0);
		int rgb = Color.HSBtoRGB(hue, 1, 1);
		return new Color(rgb);
	}
	
	/**
	 * Return lifetime of the ball in millis.
	 * 
	 * @inspects | this
	 */
	public int getLifetime() {
		return lifetime;
	}
	
	/**
	 * Update lifetime of the ball.
	 * 
	 * @pre | elapsedTime >= 0
	 * @pre | game != null
	 * 
	 * @post | getLifetime() == old(getLifetime()) - elapsedTime
	 * 
	 * @mutates | this
	 */
	public void updateLifetime(int elapsedTime, BreakoutState game) {
		lifetime -= elapsedTime;
		if (lifetime <= 0) game.makeBallNormal(this);
	}

	/**
	 * Create a clone of the ball.
	 * 
	 * @creates | result
	 */
	@Override
	public Ball clone() {
		return new SuperchargedBall(getLocation(), getVelocity(), lifetime);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof SuperchargedBall otherBall) {
			if (this.getLocation().equals(otherBall.getLocation()) && this.getVelocity().equals(otherBall.getVelocity()) && this.getLifetime() == otherBall.getLifetime()) {
				return true;
			}
		}
		return false;
	}

}
