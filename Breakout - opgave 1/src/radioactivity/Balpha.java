package radioactivity;

import utils.Vector;
import utils.Circle;
import utils.Rect;

/**
 * Represents the state of a 'balpha' particle (ball or alpha).
 * 
 * @invar | getLocation() != null
 * @invar | getVelocity() != null
 */
public abstract class Balpha {

	/**
	 * @invar | location != null
	 * @invar | velocity != null
	 */
	private Circle location;
	private Vector velocity;
	
	/**
	 * Construct a new alpha at a given `location`, with a given `velocity`.
	 * 
	 * @pre | location != null
	 * @pre | velocity != null
	 * 
	 * @post | getLocation().equals(location)
	 * @post | getVelocity().equals(velocity)
	 */
	public Balpha(Circle location, Vector velocity) {
		this.location = location;
		this.velocity = velocity;
	}
	
	/**
	 * Return this particle's location.
	 * 
	 * @inspects | this
	 */
	public Circle getLocation() {
		return location;
	}

	/**
	 * Set this particle's location.
	 * 
	 * @pre | location != null
	 * @post | getLocation() == location
	 * 
	 * @mutates_properties | getLocation()
	 */
	public void setLocation(Circle location) {
		this.location = location;
	}
	
	/**
	 * Return this particle's velocity.
	 * 
	 * @inspects | this
	 */
	public Vector getVelocity() {
		return velocity;
	}

	/**
	 * Set this particle's velocity.
	 * 
	 * @pre | velocity != null
	 * @post | getVelocity() == velocity
	 * 
	 * @mutates_properties | getVelocity()
	 */
	public void setVelocity(Vector velocity) {
		this.velocity = velocity;
	}
	
	/**
	 * Check whether this particle collides with a given `rect`.
	 * 
	 * @pre | rect != null
	 * @post | result == ((rect.collideWith(getLocation()) != null) &&
	 *       |            (getVelocity().product(rect.collideWith(getLocation())) > 0))
	 * 
	 * @inspects | this
	 */
	public boolean collidesWith(Rect rect) {
		Vector coldir = rect.collideWith(getLocation());
		return coldir != null && (getVelocity().product(coldir) > 0);
	}
	
	/**
	 * Check whether this particle collides with a given `rect` and if so, return the
	 * new velocity this particle will have after bouncing on the given rect.
	 * 
	 * @pre | rect != null
	 * @post | (rect.collideWith(getLocation()) == null && result == null) ||
	 *       | (rect.collideWith(getLocation()) != null && getVelocity().product(rect.collideWith(getLocation())) <= 0 && result == null) ||
	 *       | (rect.collideWith(getLocation()) != null && result.equals(getVelocity().mirrorOver(rect.collideWith(getLocation()))))
	 *
	 * @inspects | this
	 */
	public Vector bounceOn(Rect rect) {
		Vector coldir = rect.collideWith(getLocation());
		if (coldir != null && getVelocity().product(coldir) > 0) {
			return getVelocity().mirrorOver(coldir);
		}
		return null;
	}
	
	/**
	 * Check if two particles are equal based on their content.
	 * 
	 * @post | result == (this == obj) || (
	 * 		 | 		obj != null &&
	 * 		 |		getClass() == obj.getClass() &&
	 * 		 |		getVelocity().equals(((Balpha)obj).getVelocity()) &&
	 * 		 |		getLocation().getCenter().equals(((Balpha)obj).getLocation().getCenter()) &&
	 * 		 |		getLocation().getDiameter() == ((Balpha)obj).getLocation().getDiameter()
	 * 		 | )
	 * 
	 * @inspects | this, obj
	 */
	public boolean equalsContent(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Balpha other = (Balpha) obj;
		if (!getVelocity().equals(other.getVelocity()))
			return false;
		if (!getLocation().getCenter().equals(other.getLocation().getCenter()))
			return false;
		if (getLocation().getDiameter() != other.getLocation().getDiameter())
			return false;
		return true;
	}
}
