package breakout;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the state of a ball in the breakout game.
 * 
 * @invar | getLocation() != null
 * @invar | getVelocity() != null
 */
public abstract class Ball {
	
	/**
	 * @invar | location != null
	 */
	private Circle location;
	/**
	 * @invar | velocity != null
	 */
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
	 * 
	 * @inspects | this
	 */
	public Circle getLocation() {
		return location;
	}

	/**
	 * Return this ball's velocity.
	 * 
	 * @inspects | this
	 */
	public Vector getVelocity() {
		return velocity;
	}
	
	/**
	 * Set this ball's location to the given location circle.
	 * 
	 * @pre | location != null
	 * @post | getLocation() == location
	 * 
	 * @mutates | this
	 */
	public void setLocation(Circle location) {
		this.location = location;
	}
	
	/**
	 * Set this ball's velocity to the given velocity vector.
	 * 
	 * @pre | velocity != null
	 * @post | getVelocity() == velocity
	 * 
	 * @mutates | this
 	 */
	public void setVelocity(Vector velocity) {
		this.velocity = velocity;
	}
	
	/**
	 * Return this ball's center.
	 * 
	 * @post | getLocation().getCenter().equals(result)
	 * 
	 * @inspects | this
	 */
	public Point getCenter() {
		return getLocation().getCenter();
	}
	
	/**
	 * Return this ball's diameter.
	 * 
	 * @post | result == getLocation().getDiameter()
	 * 
	 * @inspects | this
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
	 *       
	 * @inspects | this
	 */
	public Vector bounceOn(Rect rect) {
		Vector coldir = rect.collideWith(location);
		if(coldir != null && velocity.product(coldir) > 0) {
			return velocity.mirrorOver(coldir);
		}
		return null;
	}
	
	/**
	 * Add the given amount of balls to the game.
	 * 
	 * @pre | additionalBalls <= 3
	 * @pre | game != null
	 * 
	 * @post | game.getBalls().length == old(game.getBalls().length) + additionalBalls
	 * 
	 * @inspects | this
	 * @mutates | game
	 */
	public void replicateBalls(int additionalBalls, BreakoutState game) {
		List<Ball> newBalls = new ArrayList<Ball>();
		
		for (int i = 0; i < additionalBalls; i++) {
			Ball newBall = copyBall(this, this.getVelocity().plus(NEW_BALLS_VECTORS[i]));
			newBalls.add(newBall);
		}
		
		game.addBalls(newBalls.toArray(Ball[]::new));
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
	public abstract Ball copyBall(Ball ball, Vector velocity);
	
	/**
	 * Changes the velocity of the ball after hitting a rectangle.
	 * 
	 * @pre | rect != null
	 * 
	 * @post | getVelocity().equals(old(getVelocity())) || getVelocity().equals(old(bounceOn(rect)))
	 * 
	 * @mutates | this
	 */
	public abstract void hitBlock(Rect rect, boolean destroyed);

	/**
	 * Return color of the ball.
	 * 
	 * @inspects | this
	 */
	public abstract Color getColor();

	/**
	 * Return lifetime of the ball in millis.
	 * 
	 * @inspects | this
	 */
	public int getLifetime() { return -1; };
	
	/**
	 * Update lifetime of the ball.
	 * 
	 * @pre | elapsedTime >= 0
	 * @pre | game != null
	 * 
	 * @post | getLifetime() <= old(getLifetime())
	 * 
	 * @mutates | this
	 */
	public void updateLifetime(int elapsedTime, BreakoutState game) { };
	
	/**
	 * Create a clone of the ball.
	 * 
	 * @creates | result
	 */
	public abstract Ball clone();
	
	@Override
	public abstract boolean equals(Object other);
	
}
