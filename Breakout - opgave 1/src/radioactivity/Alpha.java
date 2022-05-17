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
public class Alpha extends Balpha {
	
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
		super(location, velocity);
		linkedBalls = new HashSet<Ball>();
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
	 * Move this alpha by the given vector.
	 * 
	 * @pre | v != null
	 * 
	 * @post | getLocation().getCenter().equals(old(getLocation()).getCenter().plus(v))
	 * @post | getLocation().getDiameter() == old(getLocation()).getDiameter()
	 * 
	 * @mutates_properties | getLocation()
	 */
	public void move(Vector v) {
		setLocation(new Circle(getLocation().getCenter().plus(v), getLocation().getDiameter()));
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
		return new Alpha(getLocation(), getVelocity());
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
	 * Update the alpha after hitting a wall at a given location.
	 * 
	 * @pre | rect != null
	 * @pre | collidesWith(rect)
	 * @post | getLocation().equals(old(getLocation()))
	 * 
	 * @mutates_properties | getVelocity()
	 */
	public void hitWall(Rect rect) {
		setVelocity(bounceOn(rect));
	}
	
	/**
	 * Update the alpha after hitting a paddle at a given location.
	 * 
	 * @pre | rect != null
	 * @pre | collidesWith(rect)
	 * @pre | paddleVel != null
	 * @post | getLocation().equals(old(getLocation()))
	 * 
	 * @mutates_properties | getVelocity()
	 */
	public void hitPaddle(Rect rect, Vector paddleVel) {
		Vector nspeed = bounceOn(rect);
		setVelocity(nspeed.plus(paddleVel.scaledDiv(5)));
	}

}
