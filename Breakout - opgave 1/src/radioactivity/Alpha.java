package radioactivity;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import utils.Circle;
import utils.Rect;
import utils.Vector;

/**
 * Represents the state of an alpha particle in the breakout game.
 * 
 * @invar | getLocation() != null
 * @invar | getVelocity() != null
 * 
 * @invar | getBalls() != null
 * @invar | getBalls().stream().allMatch(b -> b != null)
 * @invar | getBalls().stream().allMatch(b -> b.getAlphas().contains(this))
 */
public class Alpha {

	/**
	 * @invar | location != null
	 * @invar | velocity != null
	 */
	private Circle location;
	private Vector velocity;
	
	/**
	 * @invar | linkedBalls != null
	 * @invar | linkedBalls.stream().allMatch(b -> b != null)
	 * @invar | linkedBalls.stream().allMatch(b -> b.linkedAlphas.contains(this)) // b.linkedAlphas is not null, because that ball invariant has been checked in a previous 'phase'
	 *
	 * @representationObject
	 * @peerObjects
	 */
	Set<Ball> linkedBalls;
	
	/**
	 * Construct a new alpha at a given `location`, with a given `velocity`.
	 * 
	 * @pre | location != null
	 * @pre | velocity != null
	 * 
	 * @post | getLocation().equals(location)
	 * @post | getVelocity().equals(velocity)
	 * @post | getBalls().isEmpty()
	 */
	public Alpha(Circle location, Vector velocity) {
		this.location = location;
		this.velocity = velocity;
		linkedBalls = new HashSet<Ball>();
	}
	
	/**
	 * Return this alpha's location.
	 * 
	 * @inspects | this
	 */
	public Circle getLocation() {
		return location;
	}
	
	/**
	 * Set this alpha's location.
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
	 * Return this alpha's velocity.
	 * 
	 * @inspects | this
	 */
	public Vector getVelocity() {
		return velocity;
	}

	/**
	 * Set this alpha's velocity.
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
	 * Return a shallow copy of the linked alpha particles.
	 * 
	 * @post | result != null
	 * 
	 * @inspects | this
	 * @creates | result
	 * @peerObjects
	 */
	public Set<Ball> getBalls() {
		return Set.copyOf(linkedBalls);
	}
	
	/**
	 * Move this BallState by the given vector.
	 * 
	 * @pre | v != null
	 * 
	 * @post | getLocation().getCenter().equals(old(getLocation()).getCenter().plus(v))
	 * @post | getLocation().getDiameter() == old(getLocation()).getDiameter()
	 * 
	 * @mutates | this
	 */
	public void move(Vector v) {
		location = new Circle(getLocation().getCenter().plus(v), getLocation().getDiameter());
	}
	
	/**
	 * Return a clone of this alpha.
	 * 
	 * @post | result.getLocation().equals(getLocation())
	 * @post | result.getVelocity().equals(getVelocity())
	 * 
	 * @inspects | this
	 * @creates | result
	 */
	public Alpha clone() {
		return new Alpha(location, velocity);
	}
	
	/**
	 * Return the color this alpha should be painted in.
	 * 
	 * @post | result != null
	 * @post | result == Color.cyan
	 * 
	 * @inspects | this
	 */
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
	
	/**
	 * Check if two alphas are equal based on their content.
	 */
	public boolean equalsContent(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Alpha other = (Alpha) obj;
		if (!getVelocity().equals(other.getVelocity()))
			return false;
		if (!getLocation().getCenter().equals(other.getLocation().getCenter()))
			return false;
		if (getLocation().getDiameter() != other.getLocation().getDiameter())
			return false;
		return true;
	}

}
