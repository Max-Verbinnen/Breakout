package radioactivity;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import breakout.BreakoutState;
import logicalcollections.LogicalSet;
import utils.Circle;
import utils.Point;
import utils.Rect;
import utils.Vector;

/**
 * Represents the state of a ball in the breakout game.
 * 
 * @invar | getLocation() != null
 * @invar | getVelocity() != null
 * 
 * @invar | getAlphas() != null
 * @invar | getAlphas().stream().allMatch(a -> a != null)
 * @invar | getAlphas().stream().allMatch(a -> a.getBalls().contains(this))
 * 
 * @invar | getEcharge() != 0
 * @invar | (getAlphas().size() % 2 == 0) ? getEcharge() > 0 : getEcharge() < 0
 * @invar | Math.abs(getEcharge()) == ((getAlphas().isEmpty()) ? 1 : getAlphas().stream().mapToInt(a -> a.getBalls().size()).max().getAsInt())
 */
public abstract class Ball {

	/**
	 * @invar | location() != null
	 * @invar | velocity() != null
	 */
	protected Circle location;
	protected Vector velocity;
	
	/**
	 * @invar | linkedAlphas != null
	 * @invar | linkedAlphas.stream().allMatch(a -> a != null)
	 * @invar | linkedAlphas.stream().allMatch(a -> a.linkedBalls.contains(this)) // a.linkedBalls is not null, because that ball invariant has been checked in a previous 'phase'
	 * 
	 * @representationObject
	 * @peerObjects
	 */
	Set<Alpha> linkedAlphas;
	
	/**
	 * @invar | eCharge != 0
	 * @invar | (linkedAlphas.size() % 2 == 0) ? eCharge > 0 : eCharge < 0
	 * @invar | Math.abs(eCharge) == ((linkedAlphas.isEmpty()) ? 1 : linkedAlphas.stream().mapToInt(a -> a.linkedBalls.size()).max().getAsInt())
	 */
	int eCharge;

	/**
	 * Construct a new ball at a given `location`, with a given `velocity`.
	 * 
	 * @pre | location != null
	 * @pre | velocity != null
	 * 
	 * @post | getLocation().equals(location)
	 * @post | getVelocity().equals(velocity)
	 * @post | getAlphas().isEmpty()
	 */
	public Ball(Circle location, Vector velocity) {
		this.location = location;
		this.velocity = velocity;
		linkedAlphas = new HashSet<Alpha>();
		calculateEcharge();
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
	 * Set this ball's location.
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
	 * Return this ball's velocity.
	 * 
	 * @inspects | this
	 */
	public Vector getVelocity() {
		return velocity;
	}
	
	/**
	 * Set this ball's velocity.
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
	 * Return this ball's electric charge.
	 * 
	 * @inspects | this
	 */
	public int getEcharge() {
		return eCharge;
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
	public Set<Alpha> getAlphas() {
		return Set.copyOf(linkedAlphas);
	}
	
	private void calculateEcharge() {
		int charge = 1;
		for (Alpha a : linkedAlphas) {
			charge = Math.max(charge, a.linkedBalls.size());
		}
		if (linkedAlphas.size() % 2 != 0) charge *= -1;
		eCharge = charge;
	}
	
	/**
	 * Link alpha particle `a` to this ball.
	 * 
	 * @pre | a != null
	 * 
	 * @post | getAlphas().equals(LogicalSet.plus(old(getAlphas()), a))
	 * @post | a.getBalls().equals(LogicalSet.plus(old(a.getBalls()), this))
	 * 
	 * @mutates_properties | getAlphas(), a.getBalls()
	 */
	public void linkTo(Alpha a) {
		linkedAlphas.add(a);
		a.linkedBalls.add(this);
		for (Ball b : a.linkedBalls) {
			b.calculateEcharge();
		}
	}
	
	/**
	 * Delete link between alpha particle `a` and this ball.
	 * 
	 * @pre | a != null
	 * 
	 * @post | getAlphas().equals(LogicalSet.minus(old(getAlphas()), a))
	 * @post | a.getBalls().equals(LogicalSet.minus(old(a.getBalls()), this))
	 * 
	 * @mutates_properties | getAlphas(), a.getBalls()
	 */
	public void unLink(Alpha a) {
		linkedAlphas.remove(a);
		a.linkedBalls.remove(this);
		for (Ball b : a.linkedBalls) {
			b.calculateEcharge();
		}
		calculateEcharge();
	}

	/**
	 * Check whether this ball collides with a given `rect` and if so, return the
	 * new velocity this ball will have after bouncing on the given rect.
	 * 
	 * @pre | rect != null
	 * @post | (rect.collideWith(getLocation()) == null && result == null) ||
	 *       | (rect.collideWith(getLocation()) != null && getVelocity().product(rect.collideWith(getLocation())) <= 0 && result == null) ||
	 *       | (rect.collideWith(getLocation()) != null && result.equals(getVelocity().mirrorOver(rect.collideWith(getLocation()))))
	 * 
	 * @inspects | this
	 */
	public Vector bounceOn(Rect rect) {
		Vector coldir = rect.collideWith(location);
		if (coldir != null && velocity.product(coldir) > 0) {
			return velocity.mirrorOver(coldir);
		}
		return null;
	}

	/**
	 * Check whether this ball collides with a given `rect`.
	 * 
	 * @pre | rect != null
	 * @post | result == ((rect.collideWith(getLocation()) != null) &&
	 *       |            (getVelocity().product(rect.collideWith(getLocation())) > 0))
	 * @inspects | this
	 */
	public boolean collidesWith(Rect rect) {
		Vector coldir = rect.collideWith(getLocation());
		return coldir != null && (getVelocity().product(coldir) > 0);
	}

	/**
	 * Move this BallState by the given vector.
	 * 
	 * @pre | v != null
	 * @pre | elapsedTime >= 0
	 * @pre | elapsedTime <= BreakoutState.MAX_ELAPSED_TIME
	 * 
	 * @post | getLocation().getCenter().equals(old(getLocation()).getCenter().plus(v))
	 * @post | getLocation().getDiameter() == old(getLocation()).getDiameter()
	 * 
	 * @mutates | this
	 */
	public abstract void move(Vector v, int elapsedTime);

	/**
	 * Update the BallState after hitting a block at a given location, taking into account whether the block was destroyed by the hit or not.
	 * 
	 * @pre | rect != null
	 * @pre | collidesWith(rect)
	 * 
	 * @post | getLocation().equals(old(getLocation()))
	 * 
	 * @mutates | this
	 */
	public abstract void hitBlock(Rect rect, boolean destroyed);

	/**
	 * Update the BallState after hitting a paddle at a given location.
	 * 
	 * @pre | rect != null
	 * @pre | collidesWith(rect)
	 * @pre | paddleVel != null
	 * 
	 * @post | getLocation().equals(old(getLocation()))
	 * 
	 * @mutates | this
	 */
	public abstract void hitPaddle(Rect rect, Vector paddleVel);

	/**
	 * Update the BallState after hitting a wall at a given location.
	 * 
	 * @pre | rect != null
	 * @pre | collidesWith(rect)
	 * 
	 * @post | getLocation().equals(old(getLocation()))
	 * 
	 * @mutates | this
	 */
	public abstract void hitWall(Rect rect);

	/**
	 * Return the color this ball should be painted in.
	 * 
	 * @post | result != null
	 * @inspects | this
	 */
	public abstract Color getColor();
	
	/**
	 * Return this point's center.
	 * 
	 * @post | getLocation().getCenter().equals(result)
	 * @inspects | this
	 */
	public Point getCenter() {
		return getLocation().getCenter();
	}
	
	/**
	 * Return a clone of this BallState with the given velocity.
	 * 
	 * @post | result.getLocation().equals(getLocation())
	 * @post | result.getVelocity().equals(v)
	 * 
	 * @inspects | this
	 * @creates | result
	 */
	public abstract Ball cloneWithVelocity(Vector v);
	
	/**
	 * Return a clone of this ball.
	 * 
	 * @post | result.getLocation().equals(getLocation())
	 * @post | result.getVelocity().equals(getVelocity())
	 * 
	 * @inspects | this
	 * @creates | result
	 */
	public Ball clone() {
		return cloneWithVelocity(getVelocity());
	}
	
	/**
	 * Check if two balls are equal based on their content.
	 */
	public boolean equalsContent(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ball other = (Ball) obj;
		if (!getVelocity().equals(other.getVelocity()))
			return false;
		if (!getLocation().getCenter().equals(other.getLocation().getCenter()))
			return false;
		if (getLocation().getDiameter() != other.getLocation().getDiameter())
			return false;
		return true;
	}

}
