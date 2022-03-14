package breakout;

import java.util.ArrayList;

public class Rectangle {
	
	private int leftBound;
	private int topBound;
	private int rightBound;
	private int bottomBound;
	
	private Vector leftBoundVector;
	private Vector topBoundVector;
	private Vector rightBoundVector;
	private Vector bottomBoundVector;
	
	public Rectangle(Point topLeft, Point bottomRight, boolean isInverted) {
		leftBound = topLeft.getX();
		topBound = topLeft.getY();
		rightBound = bottomRight.getX();
		bottomBound = bottomRight.getY();
		
		setOrientation(isInverted);
	}
	
	private void setOrientation(boolean isInverted) {
		
		leftBoundVector = Vector.RIGHT;
		topBoundVector = Vector.DOWN;
		rightBoundVector = Vector.LEFT;
		bottomBoundVector = Vector.UP;
		
		if(isInverted) {
			leftBoundVector = Vector.LEFT;
			topBoundVector = Vector.UP;
			rightBoundVector = Vector.RIGHT;
			bottomBoundVector = Vector.DOWN;
		}
	}
	
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
					if(canOnlyBeHitOnce) {
						return collidedBalls.stream().mapToInt(x -> x).toArray();
					}
					
				}else if (Math.abs(rightBound - ball.getCenter().getX()) <= radius && ball.getVelocity().product(rightBoundVector) > 0) {
					balls[i] = new BallState(ball.getCenter(), ball.getDiameter(), ball.getVelocity().mirrorOver(rightBoundVector));
					collidedBalls.add(i);
					if(canOnlyBeHitOnce) {
						return collidedBalls.stream().mapToInt(x -> x).toArray();
					}
				}
			}
			// Check vertical line of the rectangle
			else if (ball.getCenter().getX() > leftBound && ball.getCenter().getX() < rightBound) {
				if (Math.abs(topBound - ball.getCenter().getY()) <= radius && ball.getVelocity().product(topBoundVector) > 0) {
					balls[i] = new BallState(ball.getCenter(), ball.getDiameter(), ball.getVelocity().mirrorOver(topBoundVector));
					collidedBalls.add(i);
					if(canOnlyBeHitOnce) {
						return collidedBalls.stream().mapToInt(x -> x).toArray();
					}
					
				}else if (Math.abs(bottomBound - ball.getCenter().getY()) <= radius && ball.getVelocity().product(bottomBoundVector) > 0) {
					balls[i] = new BallState(ball.getCenter(), ball.getDiameter(), ball.getVelocity().mirrorOver(bottomBoundVector));
					collidedBalls.add(i);
					if(canOnlyBeHitOnce) {
						return collidedBalls.stream().mapToInt(x -> x).toArray();
					}
					
				}
			}
		}
		
		return collidedBalls.stream().mapToInt(x -> x).toArray();
	}
}
