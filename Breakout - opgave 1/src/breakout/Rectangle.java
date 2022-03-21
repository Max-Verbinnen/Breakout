package breakout;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Represents rectangles (e.g. paddle, block or game window)
 *
 * @immutable
 * 
 * @invar | getLeftBound() < getRightBound()
 * @invar | getTopBound() < getBottomBound()
 * @invar | getLeftBound() >= 0 && getTopBound() >= 0 && getRightBound() >= 0 && getBottomBound() >= 0
 * 
 * @invar | getLeftBoundVector() != null
 * @invar | getTopBoundVector() != null
 * @invar | getRightBoundVector() != null
 * @invar | getBottomBoundVector() != null
 */
public class Rectangle {
	/**
	 * @invar | leftBound < rightBound
	 * @invar | topBound < bottomBound
	 * @invar | leftBound >= 0 && topBound >= 0 && rightBound >= 0 && bottomBound >= 0
	 */
	private final int leftBound;
	private final int topBound;
	private final int rightBound;
	private final int bottomBound;
	
	/**
	 * @invar | leftBoundVector != null
	 * @invar | topBoundVector != null
	 * @invar | rightBoundVector != null
	 * @invar | bottomBoundVector != null
	 * 
	 * @representationObject (applicable for all 4)
	 */
	private final Vector leftBoundVector;
	private final Vector topBoundVector;
	private final Vector rightBoundVector;
	private final Vector bottomBoundVector;
	
	/**
	 * Returns a new rectangle.
	 * 
	 * @pre | topLeft != null
	 * @pre | bottomRight != null
	 * @pre | topLeft.isUpAndLeftFrom(bottomRight)
	 * @pre | (new Point(0, 0)).isUpAndLeftFrom(topLeft)
	 * 
	 * @post | getLeftBound() == topLeft.getX()
	 * @post | getTopBound() == topLeft.getY()
	 * @post | getRightBound() == bottomRight.getX()
	 * @post | getBottomBound() == bottomRight.getY()
	 * 
	 * @post | getLeftBoundVector() == ((isInverted) ? Vector.LEFT : Vector.RIGHT)
	 * @post | getTopBoundVector() == ((isInverted) ? Vector.UP : Vector.DOWN)
	 * @post | getRightBoundVector() == ((isInverted) ? Vector.RIGHT : Vector.LEFT)
	 * @post | getBottomBoundVector() == ((isInverted) ? Vector.DOWN : Vector.UP)
	 * 
	 * @inspects | topLeft, bottomRight
	 */
	public Rectangle(Point topLeft, Point bottomRight, boolean isInverted) {
		leftBound = topLeft.getX();
		topBound = topLeft.getY();
		rightBound = bottomRight.getX();
		bottomBound = bottomRight.getY();
		
		leftBoundVector = (isInverted) ? Vector.LEFT : Vector.RIGHT;
		topBoundVector = (isInverted) ? Vector.UP : Vector.DOWN;
		rightBoundVector = (isInverted) ? Vector.RIGHT : Vector.LEFT;
		bottomBoundVector = (isInverted) ? Vector.DOWN : Vector.UP;
	}
	
	/**
	 * Returns left bound of rectangle.
	 */
	public int getLeftBound() {
		return leftBound;
	}
	
	/**
	 * Returns top bound of rectangle.
	 */
	public int getTopBound() {
		return topBound;
	}
	
	/**
	 * Returns right bound of rectangle.
	 */
	public int getRightBound() {
		return rightBound;
	}
	
	/**
	 * Returns bottom bound of rectangle.
	 */
	public int getBottomBound() {
		return bottomBound;
	}
	
	/**
	 * Returns the left bound vector (= used to mirror velocity)
	 */
	public Vector getLeftBoundVector() {
		return leftBoundVector;
	}
	
	/**
	 * Returns the top bound vector (= used to mirror velocity)
	 */
	public Vector getTopBoundVector() {
		return topBoundVector;
	}
	
	/**
	 * Returns the right bound vector (= used to mirror velocity)
	 */
	public Vector getRightBoundVector() {
		return rightBoundVector;
	}
	
	/**
	 * Returns the bottom bound vector (= used to mirror velocity)
	 */
	public Vector getBottomBoundVector() {
		return bottomBoundVector;
	}
	
	/**
	 * Return balls that collided with rectangle.
	 * 
	 * @pre | balls != null
	 * @pre | Arrays.stream(balls).allMatch(ball -> ball != null)
	 * 
	 * @post | result != null
	 * 
	 * @mutates | balls
	 * @inspects | this
	 * @creates | result
	 */
	public int[] collide(BallState[] balls, boolean canOnlyBeHitOnce) {
		
		HashSet<Integer> collidedBalls = new HashSet<Integer>();
		
		for (int i = 0; i < balls.length; i++) {

			BallState ball = balls[i];
			int radius = ball.getDiameter() / 2;
			
			// Check horizontal  line of the rectangle
			if (ball.getCenter().getY() > topBound && ball.getCenter().getY() < bottomBound) {
				if (Math.abs(leftBound - ball.getCenter().getX()) <= radius && ball.getVelocity().product(leftBoundVector) > 0) {
					balls[i] = new BallState(ball.getCenter(), ball.getDiameter(), ball.getVelocity().mirrorOver(leftBoundVector));
					collidedBalls.add(i);
					if (canOnlyBeHitOnce) return collidedBalls.stream().mapToInt(x -> x).toArray();
					
				} else if (Math.abs(rightBound - ball.getCenter().getX()) <= radius && ball.getVelocity().product(rightBoundVector) > 0) {
					balls[i] = new BallState(ball.getCenter(), ball.getDiameter(), ball.getVelocity().mirrorOver(rightBoundVector));
					collidedBalls.add(i);
					if (canOnlyBeHitOnce) return collidedBalls.stream().mapToInt(x -> x).toArray();
				}
			}
			// Check vertical line of the rectangle
			if (ball.getCenter().getX() > leftBound && ball.getCenter().getX() < rightBound) {
				if (Math.abs(topBound - ball.getCenter().getY()) <= radius && ball.getVelocity().product(topBoundVector) > 0) {
					balls[i] = new BallState(ball.getCenter(), ball.getDiameter(), ball.getVelocity().mirrorOver(topBoundVector));
					collidedBalls.add(i);
					if (canOnlyBeHitOnce) return collidedBalls.stream().mapToInt(x -> x).toArray();
					
				} else if (Math.abs(bottomBound - ball.getCenter().getY()) <= radius && ball.getVelocity().product(bottomBoundVector) > 0) {
					balls[i] = new BallState(ball.getCenter(), ball.getDiameter(), ball.getVelocity().mirrorOver(bottomBoundVector));
					collidedBalls.add(i);
					if (canOnlyBeHitOnce) return collidedBalls.stream().mapToInt(x -> x).toArray();
					
				}
			}
		}
		
		return collidedBalls.stream().mapToInt(x -> x).toArray();
	}
}
