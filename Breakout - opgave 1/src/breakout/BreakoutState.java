package breakout;

import java.util.Arrays;

/**
 * Represents the state of the breakout game
 * 
 * @invar | getBalls() != null
 * @invar | getBlocks() != null
 * @invar | getPaddle() != null
 * @invar | getBottomRight() != null
 */
public class BreakoutState {
	/**
	 * @invar | balls != null
	 * @invar | blocks != null
	 * @invar | bottomRight != null
	 * @invar | paddle != null
	 * 
	 * @representationObject (dit geldt voor alle 4)
	 */
	private BallState[] balls;
	private BlockState[] blocks;
	private Point bottomRight;
	private PaddleState paddle;
	private static final Vector INIT_PADDLE_VELOCITY = new Vector(10, 0);
 
	/**
	 * Initializes this object so that it stores the balls, blocks, bottomRight & paddle objects.
	 * 
	 * @throws IllegalArgumentException
	 * 		  | balls == null || blocks == null || bottomRight == null || paddle == null
	 * 
	 * @post  | Arrays.equals(getBalls(), balls)
	 * @post  | Arrays.equals(getBlocks(), blocks)
	 * @post  | getBottomRight() == bottomRight
	 * @post  | getPaddle() == paddle
	 * 
	 * @inspects | balls, blocks, bottomRight, paddle
	 */
	public BreakoutState(BallState[] balls, BlockState[] blocks, Point bottomRight, PaddleState paddle) {
		if (balls == null || blocks == null || bottomRight == null || paddle == null) {
			throw new IllegalArgumentException("Not a valid argument.");
		}

		this.balls = balls.clone();
		this.blocks = blocks.clone();
		this.bottomRight = bottomRight;
		this.paddle = paddle;
	}
	
	/**
	 * Returns the sequence of balls that are currently in the game.
	 * 
	 * @creates | result
	 */
	public BallState[] getBalls() {
		return balls.clone();
	}
	
	/**
	 * Returns the sequence of blocks that are shown on the screen.
	 * 
	 * @creates | result
	 */
	public BlockState[] getBlocks() {
		return blocks.clone();
	}
	
	/**
	 * Returns the paddle object.
	 */
	public PaddleState getPaddle() {
		return paddle;
	}

	/**
	 * Returns the bottom right coordinate (as a Point).
	 */
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
		
		// Check whether any ball hit any block, in which case the block must be removed from the game and the ball must bounce back.
		int deletedBlocks = 0;
		for (int i = 0; i < blocks.length; i++) {
			
			BlockState block = blocks[i];
			
			int blockLeft = block.getTopLeft().getX();
			int blockTop = block.getTopLeft().getY();
			int blockRight = block.getBottomRight().getX();
			int blockBottom = block.getBottomRight().getY();
			
			for (int j = 0; j < balls.length && block != null; j++) {
				
				BallState ball = balls[j];
				int radius = ball.getDiameter() / 2;
				
				// Check horizontal  line of the block
				if (ball.getCenter().getY() > blockTop && ball.getCenter().getY() < blockBottom) {
					if (Math.abs(blockLeft - ball.getCenter().getX()) <= radius && ball.getVelocity().product(Vector.RIGHT) > 0) {
						balls[j] = new BallState(ball.getCenter(), ball.getDiameter(), ball.getVelocity().mirrorOver(Vector.RIGHT));
						blocks[i] = null;
						deletedBlocks++;
						block = blocks[i];
						
					}else if (Math.abs(blockRight - ball.getCenter().getX()) <= radius && ball.getVelocity().product(Vector.LEFT) > 0) {
						balls[j] = new BallState(ball.getCenter(), ball.getDiameter(), ball.getVelocity().mirrorOver(Vector.LEFT));
						blocks[i] = null;
						deletedBlocks++;
						block = blocks[i];
						
					}
				}
				// Check vertical line of the block
				else if (ball.getCenter().getX() > blockLeft && ball.getCenter().getX() < blockRight) {
					if (Math.abs(blockTop - ball.getCenter().getY()) <= radius && ball.getVelocity().product(Vector.DOWN) > 0) {
						balls[j] = new BallState(ball.getCenter(), ball.getDiameter(), ball.getVelocity().mirrorOver(Vector.DOWN));
						blocks[i] = null;
						deletedBlocks++;
						block = blocks[i];
						
					}else if (Math.abs(blockBottom - ball.getCenter().getY()) <= radius && ball.getVelocity().product(Vector.UP) > 0) {
						balls[j] = new BallState(ball.getCenter(), ball.getDiameter(), ball.getVelocity().mirrorOver(Vector.UP));
						blocks[i] = null;
						deletedBlocks++;
						block = blocks[i];
						
					}
				}
			}
		}
		BlockState[] newBlocks = new BlockState[blocks.length - deletedBlocks];
		int j = 0;
		for (int i = 0; i < blocks.length; i++) {
			if (blocks[i] != null) {
				newBlocks[j] = blocks[i];
				j++;
			}
		}
		blocks = newBlocks;
		
		// Check whether any ball hit the paddle, in which case it must bounce back.
		// Additionally, the ball must speed up by one fifth of the current velocity of the paddle.
		int paddleLeft = paddle.getCenter().getX() - paddle.getWidth() / 2;
		int paddleTop = paddle.getCenter().getY() - paddle.getHeight() / 2;
		int paddleRight = paddle.getCenter().getX() + paddle.getWidth() / 2;
		int paddleBottom = paddle.getCenter().getY() + paddle.getHeight() / 2;
		
		for (int i = 0; i < balls.length; i++) {
			
			BallState ball = balls[i];
			int radius = ball.getDiameter() / 2;
			
			// Check horizontal  line of the paddle
			if (ball.getCenter().getY() > paddleTop && ball.getCenter().getY() < paddleBottom) {
				if (Math.abs(paddleLeft - ball.getCenter().getX()) <= radius && ball.getVelocity().product(Vector.RIGHT) > 0) {
					balls[i] = new BallState(ball.getCenter(), ball.getDiameter(), ball.getVelocity().mirrorOver(Vector.RIGHT).plus(INIT_PADDLE_VELOCITY.scaled(paddleDir).scaledDiv(5)));
					
				}else if (Math.abs(paddleRight - ball.getCenter().getX()) <= radius && ball.getVelocity().product(Vector.LEFT) > 0) {
					balls[i] = new BallState(ball.getCenter(), ball.getDiameter(), ball.getVelocity().mirrorOver(Vector.LEFT).plus(INIT_PADDLE_VELOCITY.scaled(paddleDir).scaledDiv(5)));
					
				}
			}
			// Check vertical line of the paddle
			else if (ball.getCenter().getX() > paddleLeft && ball.getCenter().getX() < paddleRight) {
				if (Math.abs(paddleTop - ball.getCenter().getY()) <= radius && ball.getVelocity().product(Vector.DOWN) > 0) {
					balls[i] = new BallState(ball.getCenter(), ball.getDiameter(), ball.getVelocity().mirrorOver(Vector.DOWN).plus(INIT_PADDLE_VELOCITY.scaled(paddleDir).scaledDiv(5)));
					
				}else if (Math.abs(paddleBottom - ball.getCenter().getY()) <= radius && ball.getVelocity().product(Vector.UP) > 0) {
					balls[i] = new BallState(ball.getCenter(), ball.getDiameter(), ball.getVelocity().mirrorOver(Vector.UP).plus(INIT_PADDLE_VELOCITY.scaled(paddleDir).scaledDiv(5)));
					
				}
			}
		}
	}

	public void movePaddleRight() {
		if (paddle.getCenter().getX() + paddle.getWidth() / 2 < bottomRight.getX())
			paddle = new PaddleState(paddle.getCenter().plus(INIT_PADDLE_VELOCITY));
	}

	public void movePaddleLeft() {
		if (paddle.getCenter().getX() - paddle.getWidth() / 2 > 0)
			paddle = new PaddleState(paddle.getCenter().minus(INIT_PADDLE_VELOCITY));
	}
	
	/**
	 * Returns whether game is won
	 * 
	 * @post | result == (getBalls().length >= 1 && getBlocks().length == 0)
	 * 
	 * @inspects | this
	 */
	public boolean isWon() {
		return balls.length >= 1 && blocks.length == 0;
	}
	
	/**
	 * Returns whether player has died
	 * 
	 * @post | result == (getBalls().length == 0)
	 * 
	 * @inspects | this
	 */
	public boolean isDead() {
		return balls.length == 0;
	}
}
