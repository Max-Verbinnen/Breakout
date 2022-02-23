package breakout;

/**
 * This class represents the state of the ball.
 * 
 * @immutable
 */
public class BallState {

	private final Point center;
	private final int diameter;
	private final Vector velocity;
	
	/**
	 * Return a new BallState with a center, diameter and velocity.
	 * 
	 * @pre | center != null
	 * @pre | velocity != null
	 * 
	 * @post | getCenter() == center
	 * @post | getDiameter() == diameter
	 * @post | getVelocity() == velocity
	 */
	public BallState(Point center, int diameter, Vector velocity) {
		this.center = center;
		this.diameter = diameter;
		this.velocity = velocity;
	}
	
	/** Return this ball's center. */
	public Point getCenter() {
		return center;
	}
	
	/** Return this ball's diameter. */
	public int getDiameter() {
		return diameter;
	}
	
	/** Return this ball's velocity. */
	public Vector getVelocity() {
		return velocity;
	}
}
