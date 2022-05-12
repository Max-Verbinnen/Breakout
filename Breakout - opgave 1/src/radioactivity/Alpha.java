package radioactivity;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import utils.Circle;
import utils.Rect;
import utils.Vector;

public class Alpha {

	private Circle location;
	private Vector velocity;
	
	Set<Ball> linkedBalls;
	
	public Alpha(Circle location, Vector velocity) {
		this.location = location;
		this.velocity = velocity;
		linkedBalls = new HashSet<Ball>();
	}
	
	/**
	 * Return this Alpha's location.
	 */
	public Circle getLocation() {
		return location;
	}
	
	public void setLocation(Circle location) {
		this.location = location;
	}

	/**
	 * Return this Alpha's velocity.
	 */
	public Vector getVelocity() {
		return velocity;
	}
	
	public void setVelocity(Vector velocity) {
		this.velocity = velocity;
	}
	
	public Set<Ball> getBalls() {
		return Set.copyOf(linkedBalls);
	}
	
	public void move(Vector v) {
		location = new Circle(getLocation().getCenter().plus(v), getLocation().getDiameter());
	}
	
	public Alpha clone() {
		return new Alpha(location, velocity);
	}
	
	public Color getColor() {
		return Color.cyan;
	}
	
	/**
	 * Check whether this alpha collides with a given `rect`.
	 * 
	 * @pre | rect != null
	 * @post | result == ((rect.collideWith(getLocation()) != null) &&
	 *       |            (getVelocity().product(rect.collideWith(getLocation())) > 0))
	 * @inspects this
	 */
	public boolean collidesWith(Rect rect) {
		Vector coldir = rect.collideWith(getLocation());
		return coldir != null && (getVelocity().product(coldir) > 0);
	}
	
	/**
	 * Update the Alpha after hitting a wall at a given location.
	 * 
	 * @pre | rect != null
	 * @pre | collidesWith(rect)
	 * @post | getLocation().equals(old(getLocation()))
	 * @mutates this
	 */
	public void hitWall(Rect rect) {
		velocity = bounceOn(rect);
	}
	
	/**
	 * Check whether this alpha collides with a given `rect` and if so, return the
	 * new velocity this ball will have after bouncing on the given rect.
	 * 
	 * @pre | rect != null
	 * @post | (rect.collideWith(getLocation()) == null && result == null) ||
	 *       | (rect.collideWith(getLocation()) != null && getVelocity().product(rect.collideWith(getLocation())) <= 0 && result == null) ||
	 *       | (rect.collideWith(getLocation()) != null && result.equals(getVelocity().mirrorOver(rect.collideWith(getLocation()))))
	 * @inspects this
	 */
	public Vector bounceOn(Rect rect) {
		Vector coldir = rect.collideWith(location);
		if (coldir != null && velocity.product(coldir) > 0) {
			return velocity.mirrorOver(coldir);
		}
		return null;
	}
	
	/**
	 * Update the Alpha after hitting a paddle at a given location.
	 * 
	 * @pre | rect != null
	 * @pre | collidesWith(rect)
	 * @pre | paddleVel != null
	 * @post | getLocation().equals(old(getLocation()))
	 * @mutates this
	 */
	public void hitPaddle(Rect rect, Vector paddleVel) {
		Vector nspeed = bounceOn(rect);
		velocity = nspeed.plus(paddleVel.scaledDiv(5));
	}
}
