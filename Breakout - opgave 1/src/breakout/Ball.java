package breakout;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the state of a ball in the breakout game.
 * 
 * @invar | getLocation() != null
 * @invar | getVelocity() != null
 */
public abstract class Ball {
	
	private Circle location;
	private Vector velocity;
	private final Vector[] NEW_BALLS_VECTORS = new Vector[] {new Vector(2, -2), new Vector(-2, 2), new Vector(2, 2)};
	
	/**
	 * Construct a new ball at a given `location`, with a given `velocity`.
	 * 
	 * @pre | location != null
	 * @pre | velocity != null
	 * @post | getLocation() == location
	 * @post | getVelocity().equals(velocity) 
	 */
	public Ball(Circle location, Vector velocity) {
		this.location = location;
		this.velocity = velocity;
	}
	
	/**
	 * Return this ball's location.
	 */
	public Circle getLocation() {
		return location;
	}

	/**
	 * Return this ball's velocity.
	 */
	public Vector getVelocity() {
		return velocity;
	}
	
	public void setLocation(Circle location) {
		this.location = location;
	}
	
	public void setVelocity(Vector velocity) {
		this.velocity = velocity;
	}
	
	/**
	 * Return this point's center.
	 * 
	 * @post | getLocation().getCenter().equals(result)
	 */
	public Point getCenter() {
		return getLocation().getCenter();
	}
	
	/**
	 * Return this point's diameter.
	 * 
	 * @post | result == getLocation().getDiameter()
	 */
	public int getDiameter() {
		return getLocation().getDiameter();
	}

	/**
	 * Check whether this ball collides with a given `rect` and if so, return the 
	 * new velocity this ball will have after bouncing on the given rect.
	 * 
	 * @pre | rect != null
	 * @post | (rect.collideWith(getLocation()) == null && result == null) ||
	 *       | (getVelocity().product(rect.collideWith(getLocation())) <= 0 && result == null) || 
	 *       | (result.equals(getVelocity().mirrorOver(rect.collideWith(getLocation()))))
	 */
	public Vector bounceOn(Rect rect) {
		Vector coldir = rect.collideWith(location);
		if(coldir != null && velocity.product(coldir) > 0) {
			return velocity.mirrorOver(coldir);
		}
		return null;
	}
	
	public void replicateBalls(int additionalBalls, BreakoutState game) {
		List<Ball> newBalls = new ArrayList<Ball>();
		
		for (int i = 0; i < additionalBalls; i++) {
			Ball newBall = createBall(this.getLocation(), this.getVelocity().plus(NEW_BALLS_VECTORS[i]), game);
			newBalls.add(newBall);
		}
		
		game.addBalls(newBalls.toArray(Ball[]::new));
	}
	
	public abstract Ball createBall(Circle location, Vector velocity, BreakoutState game);
	
	public abstract void hitBlock(Rect rect, boolean destroyed);

}
