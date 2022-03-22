package breakout;

import java.util.Arrays;

/**
 * Represents the state of the breakout game
 * 
 * @invar | getBalls() != null
 * @invar | getBlocks() != null
 * @invar | getPaddle() != null
 * @invar | getBottomRight() != null
 * 
 * @invar | Arrays.stream(getBalls()).allMatch(ball -> ball != null)
 * @invar | Arrays.stream(getBlocks()).allMatch(block -> block != null)
 * 
 * @invar | withinBounds(getBalls(), getBottomRight())
 * @invar | withinBounds(getBlocks(), getBottomRight())
 * @invar | withinBounds(getPaddle(), getBottomRight())
 */
public class BreakoutState {
	/**
	 * @invar | balls != null
	 * @invar | blocks != null
	 * @invar | bottomRight != null
	 * @invar | paddle != null
	 * 
	 * @invar | Arrays.stream(balls).allMatch(ball -> ball != null)
	 * @invar | Arrays.stream(blocks).allMatch(block -> block != null)
	 * 
	 * @invar | withinBounds(balls, bottomRight)
	 * @invar | withinBounds(blocks, bottomRight)
	 * @invar | withinBounds(paddle, bottomRight)
	 * 
	 * @representationObject (applicable for all 4)
	 */
	private BallState[] balls;
	private BlockState[] blocks;
	private Point bottomRight;
	private PaddleState paddle;
	
	private static final Vector INIT_PADDLE_VELOCITY = new Vector(10, 0);
 
	/**
	 * Initializes this object so that it stores the balls, blocks, bottomRight & paddle objects.
	 * 
	 * @throws IllegalArgumentException | balls == null
	 * @throws IllegalArgumentException | blocks == null
	 * @throws IllegalArgumentException | bottomRight == null || !(new Point(0, 0)).isUpAndLeftFrom(bottomRight)
	 * @throws IllegalArgumentException | paddle == null
	 * 
	 * @throws IllegalArgumentException | Arrays.stream(balls).anyMatch(ball -> ball == null)
	 * @throws IllegalArgumentException | Arrays.stream(blocks).anyMatch(block -> block == null)
	 * 
	 * @throws IllegalArgumentException | !withinBounds(balls, bottomRight)
	 * @throws IllegalArgumentException | !withinBounds(blocks, bottomRight)
	 * @throws IllegalArgumentException | !withinBounds(paddle, bottomRight)
	 *  
	 * @post  | Arrays.equals(getBalls(), balls)
	 * @post  | Arrays.equals(getBlocks(), blocks)
	 * @post  | getBottomRight() == bottomRight
	 * @post  | getPaddle() == paddle
	 * 
	 * @inspects | balls, blocks, bottomRight, paddle
	 */
	public BreakoutState(BallState[] balls, BlockState[] blocks, Point bottomRight, PaddleState paddle) {
		if (balls == null) throw new IllegalArgumentException("´balls´ is invalid!");
		if (blocks == null) throw new IllegalArgumentException("´blocks´ is invalid!");
		if (bottomRight == null || !(new Point(0, 0)).isUpAndLeftFrom(bottomRight)) throw new IllegalArgumentException("´bottomRight´ is invalid!");
		if (paddle == null) throw new IllegalArgumentException("´paddle´ is invalid!");
		if (Arrays.stream(balls).anyMatch(ball -> ball == null)) throw new IllegalArgumentException("´balls´ contains null pointer!");
		if (Arrays.stream(blocks).anyMatch(block -> block == null)) throw new IllegalArgumentException("´blocks´ contains null pointer!");
		if (!withinBounds(balls, bottomRight)) throw new IllegalArgumentException("´balls´ aren't all located within game window!");
		if (!withinBounds(blocks, bottomRight)) throw new IllegalArgumentException("´blocks´ aren't all located within game window!");
		if (!withinBounds(paddle, bottomRight)) throw new IllegalArgumentException("´paddle´ isn't located within game window!");
		
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
		(new Rectangle(new Point(0, 0), bottomRight, true)).collide(balls, false);
		
		// Check whether any balls hit the bottom of the field, in which case they must be removed from the game.
		balls = Arrays.stream(balls)
					  .filter(ball -> bottomRight.getY() - ball.getCenter().getY() > ball.getDiameter() / 2)
					  .toArray(BallState[]::new);
		
		// Check whether any ball hit any block, in which case the block must be removed from the game and the ball must bounce back.
		for (int i = 0; i < blocks.length; i++) {
			int[] collidedBalls = (new Rectangle(blocks[i].getTopLeft(), blocks[i].getBottomRight(), false)).collide(balls, true);
			if (collidedBalls.length > 0) blocks[i] = null;
		}
		blocks = Arrays.stream(blocks).filter(block -> block != null).toArray(BlockState[]::new);
		
		// Check whether any ball hit the paddle, in which case it must bounce back.
		// Additionally, the ball must speed up by one fifth of the current velocity of the paddle.
		int paddleLeft = paddle.getCenter().getX() - paddle.getWidth() / 2;
		int paddleTop = paddle.getCenter().getY() - paddle.getHeight() / 2;
		int paddleRight = paddle.getCenter().getX() + paddle.getWidth() / 2;
		int paddleBottom = paddle.getCenter().getY() + paddle.getHeight() / 2;
		
		int[] collidedBalls = (new Rectangle(new Point(paddleLeft, paddleTop), new Point(paddleRight, paddleBottom), false)).collide(balls, false);
		for (int i = 0; i < collidedBalls.length; i++) {
			BallState ball = balls[collidedBalls[i]];
			balls[collidedBalls[i]] = new BallState(ball.getCenter(), ball.getDiameter(), ball.getVelocity().plus(INIT_PADDLE_VELOCITY.scaled(paddleDir).scaledDiv(5)));
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
	
	/**
	 * Returns whether ball is located within game window.
	 * 
	 * @pre | balls != null
	 * @pre | Arrays.stream(balls).allMatch(ball -> ball != null)
	 * 
	 * @post | result == Arrays.stream(balls).allMatch(ball -> ball.getCenter().getX() >= 0 && ball.getCenter().getX() <= bottomRight.getX() && ball.getCenter().getY() >= 0 && ball.getCenter().getY() <= bottomRight.getY())
	 * 
	 * @inspects | balls, bottomRight
	 */
	public boolean withinBounds(BallState[] balls, Point bottomRight) {
		return Arrays.stream(balls).allMatch(ball -> 
					ball.getCenter().getX() >= 0
					&& ball.getCenter().getX() <= bottomRight.getX()
					&& ball.getCenter().getY() >= 0
					&& ball.getCenter().getY() <= bottomRight.getY()
				);
	}
	
	/**
	 * Returns whether block is located within game window.
	 * 
	 * @pre | blocks != null
	 * @pre | Arrays.stream(blocks).allMatch(block -> block != null)
	 * 
	 * @post | result == Arrays.stream(blocks).allMatch(block -> block.getTopLeft().getX() >= 0 && block.getTopLeft().getX() <= bottomRight.getX() && block.getTopLeft().getY() >= 0 && block.getTopLeft().getY() <= bottomRight.getY() && block.getBottomRight().getX() <= bottomRight.getX() && block.getBottomRight().getY() <= bottomRight.getY())
	 * 
	 * @inspects | blocks, bottomRight
	 */
	public boolean withinBounds(BlockState[] blocks, Point bottomRight) {
		return Arrays.stream(blocks).allMatch(block -> 
					block.getTopLeft().getX() >= 0
					&& block.getTopLeft().getX() <= bottomRight.getX()
					&& block.getTopLeft().getY() >= 0
					&& block.getTopLeft().getY() <= bottomRight.getY()
					&& block.getBottomRight().getX() <= bottomRight.getX()
					&& block.getBottomRight().getY() <= bottomRight.getY()
				);
	}
	
	/**
	 * Returns whether paddle is located within game window.
	 * 
	 * @pre | paddle != null
	 * 
	 * @post | result == (paddle.getCenter().getX() + paddle.getWidth() / 2 <= bottomRight.getX() && paddle.getCenter().getX() - paddle.getWidth() / 2 >= 0)
	 * 
	 * @inspects | paddle, bottomRight
	 */
	public boolean withinBounds(PaddleState paddle, Point bottomRight) {
		return paddle.getCenter().getX() + paddle.getWidth() / 2 <= bottomRight.getX()
			   && paddle.getCenter().getX() - paddle.getWidth() / 2 >= 0;
	}
}
