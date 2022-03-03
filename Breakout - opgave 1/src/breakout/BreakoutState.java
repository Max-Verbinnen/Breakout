package breakout;

import java.util.Arrays;

// TODO: implement, document
public class BreakoutState {
	
	private BallState[] balls;
	private BlockState[] blocks;
	private Point bottomRight;
	private PaddleState paddle;
 
	
	public BreakoutState(BallState[] balls, BlockState[] blocks, Point bottomRight, PaddleState paddle) {
		if (balls == null || blocks == null || bottomRight == null || paddle == null) {
			throw new IllegalArgumentException("Not a valid argument.");
		}

		this.balls = balls.clone();
		this.blocks = blocks.clone();
		this.bottomRight = bottomRight;
		this.paddle = paddle;
	}
	
	public BallState[] getBalls() {
		return balls.clone();
	}

	public BlockState[] getBlocks() {
		return blocks.clone();
	}

	public PaddleState getPaddle() {
		return paddle;
	}

	public Point getBottomRight() {
		return bottomRight;
	}

	public void tick(int paddleDir) {

		// Move all balls one step forward according to their current velocity.
		for (int i = 0; i < balls.length; i++) {

			BallState ball = balls[i];			
			balls[i] = new BallState(ball.getCenter().plus(ball.getVelocity()), ball.getDiameter(), ball.getVelocity());
		}
		
		// Check whether any balls hit the walls on the left, right and top side of the game area, in which case they must bounce back.
		for (int i = 0; i < balls.length; i++) {

			BallState ball = balls[i];
			int radius = ball.getDiameter() / 2;
			
			int centerToLeftBound = ball.getCenter().getX();
			int centerToTopBound = ball.getCenter().getY();
			int centerToRightBound = bottomRight.getX() - ball.getCenter().getX();
			
			if (centerToLeftBound <= radius && ball.getVelocity().product(Vector.LEFT) > 0) {
				balls[i] = new BallState(ball.getCenter(), ball.getDiameter(), ball.getVelocity().mirrorOver(Vector.LEFT));
				ball = balls[i];
			}
			if (centerToTopBound <= radius && ball.getVelocity().product(Vector.UP) > 0) {
				balls[i] = new BallState(ball.getCenter(), ball.getDiameter(), ball.getVelocity().mirrorOver(Vector.UP));
				ball = balls[i];
			}
			if (centerToRightBound <= radius && ball.getVelocity().product(Vector.RIGHT) > 0) {
				balls[i] = new BallState(ball.getCenter(), ball.getDiameter(), ball.getVelocity().mirrorOver(Vector.RIGHT));
				ball = balls[i];
			}
		}
		
		// Check whether any balls hit the bottom of the field, in which case they must be removed from the game.
		balls = Arrays.stream(balls)
					  .filter(ball -> bottomRight.getY() - ball.getCenter().getY() > ball.getDiameter() / 2)
					  .toArray(BallState[]::new);
		
		// TODO: Check whether any ball hit any block, in which case the block must be removed from the game and the ball must bounce back.
		
		// TODO: Check whether any ball hit the paddle, in which case it must bounce back.
		// Additionally, the ball must speed up by one fifth of the current velocity of the paddle.
		
	}

	public void movePaddleRight() {
		paddle = new PaddleState(paddle.getCenter().plus(new Vector(10, 0)));
	}

	public void movePaddleLeft() {
		paddle = new PaddleState(paddle.getCenter().minus(new Vector(10, 0)));
	}
	
	public boolean isWon() {
		return balls.length >= 1 && blocks.length == 0;
	}

	public boolean isDead() {
		return balls.length == 0;
	}
}
