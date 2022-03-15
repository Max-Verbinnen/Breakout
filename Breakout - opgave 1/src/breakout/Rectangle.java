package breakout;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents rectangles (e.g. paddle, block or game window)
 *
 * @immutable
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
		
		ArrayList<Integer> collidedBalls = new ArrayList<Integer>();
		
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
			else if (ball.getCenter().getX() > leftBound && ball.getCenter().getX() < rightBound) {
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
