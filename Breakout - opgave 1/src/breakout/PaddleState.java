package breakout;

/**
 * This class represents the state of the paddle.
 * 
 * @immutable
 * 
 * @invar | getCenter() != null
 * @invar | getWidth() > 0
 * @invar | getHeight() > 0
 */
public class PaddleState {
	/**
	 * @invar | center != null
	 * @invar | WIDTH > 0
	 * @invar | HEIGHT > 0
	 * 
	 * @representationObject (geldt enkel voor center)
	 */
	private final Point center;

	private final int WIDTH = 5500;
	private final int HEIGHT = 800;
	
	/**
	 * Return a new BlockState with a topLeft Point and bottomRight Point.
	 * 
	 * @pre | center != null
	 * 
	 * @post | getCenter() == center
	 */
	public PaddleState(Point center) {
		this.center = center;
	}
	
	/** Return this paddle's center. */
	public Point getCenter() {
		return center;
	}
	
	/** Return the paddle's width. */
	public int getWidth() {
		return WIDTH;
	}
	
	/** Return the paddle's height. */
	public int getHeight() {
		return HEIGHT;
	}
}
