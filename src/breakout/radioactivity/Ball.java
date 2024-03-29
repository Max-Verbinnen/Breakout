package breakout.radioactivity;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import breakout.BreakoutState;
import breakout.utils.Circle;
import breakout.utils.Point;
import breakout.utils.Rect;
import breakout.utils.Vector;
import logicalcollections.LogicalSet;

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
public abstract class Ball extends Balpha {
	
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
	private int eCharge;

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
		super(location, velocity);
		linkedAlphas = new HashSet<Alpha>();
		calculateEcharge();
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
	 * @mutates_properties | getAlphas(), a.getBalls(), (...a.getBalls()).getEcharge()
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
	 * @mutates_properties | getAlphas(), a.getBalls(), (...a.getBalls()).getEcharge(), getEcharge()
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
	 * Move this ball by the given vector.
	 * 
	 * @pre | v != null
	 * @pre | elapsedTime >= 0
	 * @pre | elapsedTime <= BreakoutState.MAX_ELAPSED_TIME
	 * 
	 * @post | getLocation().getCenter().equals(old(getLocation()).getCenter().plus(v))
	 * @post | getLocation().getDiameter() == old(getLocation()).getDiameter()
	 * 
	 * @mutates_properties | getLocation()
	 */
	public abstract void move(Vector v, int elapsedTime);

	/**
	 * Update the ball after hitting a block at a given location, taking into account whether the block was destroyed by the hit or not.
	 * 
	 * @pre | rect != null
	 * @pre | collidesWith(rect)
	 * 
	 * @post | getLocation().equals(old(getLocation()))
	 * 
	 * @mutates_properties | getVelocity()
	 */
	public abstract void hitBlock(Rect rect, boolean destroyed);

	/**
	 * Update the ball after hitting a paddle at a given location.
	 * 
	 * @pre | rect != null
	 * @pre | collidesWith(rect)
	 * @pre | paddleVel != null
	 * 
	 * @post | getLocation().equals(old(getLocation()))
	 * 
	 * @mutates_properties | getVelocity()
	 */
	public abstract void hitPaddle(Rect rect, Vector paddleVel);

	/**
	 * Update the ball after hitting a wall at a given location.
	 * 
	 * @pre | rect != null
	 * @pre | collidesWith(rect)
	 * 
	 * @post | getLocation().equals(old(getLocation()))
	 * 
	 * @mutates_properties | getVelocity()
	 */
	public abstract void hitWall(Rect rect);

	/**
	 * Return the color this ball should be painted in.
	 * 
	 * @post | result != null
	 * 
	 * @inspects | this
	 */
	public abstract Color getColor();
	
	/**
	 * Return this point's center.
	 * 
	 * @post | getLocation().getCenter().equals(result)
	 * 
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
	 * 
	 * @post | result == (this == obj) || (
	 * 		 | 		obj != null &&
	 * 		 |		getClass() == obj.getClass() &&
	 * 		 |		getVelocity().equals(((Ball)obj).getVelocity()) &&
	 * 		 |		getLocation().getCenter().equals(((Ball)obj).getLocation().getCenter()) &&
	 * 		 |		getLocation().getDiameter() == ((Ball)obj).getLocation().getDiameter() &&
	 * 		 | 		((Ball)obj).getEcharge() == getEcharge()
	 * 		 | )
	 * 
	 * @inspects | this, obj
	 */
	@Override
	public boolean equalsContent(Object obj) {
		return super.equalsContent(obj) && ((Ball)obj).getEcharge() == getEcharge();
	}

}
