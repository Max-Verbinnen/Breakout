package breakout;

/**
 * This class represents the state of the ball.
 * 
 * @immutable
 * 
 * @invar | getCenter() != null
 * @invar | getVelocity() != null
 * @invar | getDiameter() > 0
 */
public class BallState {
	/**
	 * @invar | center != null
	 * @invar | velocity != null
	 * @invar | diameter > 0
	 * 
	 * @representationObject (applicable for first 2)
	 */
	private final Point center;
	private final Vector velocity;
	private final int diameter;
	
	/**
	 * Return a new BallState with a center, diameter and velocity.
	 * 
	 * @pre | center != null
	 * @pre | velocity != null
	 * @pre | diameter > 0
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
