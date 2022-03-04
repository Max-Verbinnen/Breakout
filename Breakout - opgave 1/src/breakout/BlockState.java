package breakout;

/**
 * This class represents the state of the block.
 * 
 * @immutable
 */
public class BlockState {

	private final Point topLeft;
	private final Point bottomRight;
	
	/**
	 * Return a new BlockState with a topLeft Point and bottomRight Point.
	 * 
	 * @pre | topLeft != null
	 * @pre | bottomRight != null
	 * @pre | topLeft.isUpAndLeftFrom(bottomRight)
	 * 
	 * @post | getTopLeft() == topLeft
	 * @post | getBottomRight() == bottomRight
	 */
	public BlockState(Point topLeft, Point bottomRight) {
		this.topLeft = topLeft;
		this.bottomRight = bottomRight;
	}
	
	/** Return this block's top left coordinate. */
	public Point getTopLeft() {
		return topLeft;
	}
	
	/** Return this block's bottom right coordinate. */
	public Point getBottomRight() {
		return bottomRight;
	}
}
