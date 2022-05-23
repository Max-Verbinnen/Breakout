package breakout;

import java.util.stream.IntStream;

import breakout.radioactivity.Alpha;
import breakout.radioactivity.Ball;
import breakout.utils.Circle;
import breakout.utils.Point;
import breakout.utils.Rect;
import breakout.utils.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

//import breakout.gui.GameView;

/**
 * Represents the current state of a breakout game.
 * 
 * @invar | getBalls() != null
 * @invar | Arrays.stream(getBalls()).allMatch(b -> b != null)
 * @invar | getAlphas() != null
 * @invar | Arrays.stream(getAlphas()).allMatch(a -> a != null)
 * 
 * @invar alpha's exhaustiveness
 * 		  | Arrays.stream(getBalls()).allMatch(ball ->
 * 		  | 	ball.getAlphas().stream().allMatch(alpha -> Arrays.stream(getAlphas()).anyMatch(a -> a.equalsContent(alpha)))
 * 		  | )
 * @invar ball's exhaustiveness
 * 		  | Arrays.stream(getAlphas()).allMatch(alpha ->
 * 		  | 	alpha.getBalls().stream().allMatch(ball -> Arrays.stream(getBalls()).anyMatch(b -> b.equalsContent(ball)))
 * 		  | )
 * @invar no duplicate references in alphas
 * 		  | getAlphas().length == Set.of(getAlphas()).size()
 * @invar no duplicate references in balls
 * 		  | getBalls().length == Set.of(getBalls()).size()
 * 
 * @invar | getBlocks() != null
 * @invar | getPaddle() != null
 * @invar | getBottomRight() != null
 * 
 * @invar | Point.ORIGIN.isUpAndLeftFrom(getBottomRight())
 * @invar | Arrays.stream(getAlphas()).allMatch(a -> getField().contains(a.getLocation()))
 * @invar | Arrays.stream(getBalls()).allMatch(b -> getField().contains(b.getLocation()))
 * @invar | Arrays.stream(getBlocks()).allMatch(b -> getField().contains(b.getLocation()))
 * @invar | getField().contains(getPaddle().getLocation())
 */
public class BreakoutState {
	
	private class BalphaContainer {
		public Alpha[] alphas;
		public Ball[] balls;
		
		BalphaContainer(Alpha[] alphas, Ball[] balls) { this.alphas = alphas; this.balls = balls; }
	}

	private static final Vector PADDLE_VEL = new Vector(20, 0);
	public static final int MAX_BALL_REPLICATE = 5;
	private static final Vector[] BALL_VEL_VARIATIONS = new Vector[] { new Vector(0, 0), new Vector(2, -2),
			new Vector(-2, 2), new Vector(2, 2), new Vector(-2, -2) };
	public static int MAX_ELAPSED_TIME = 50;

	/**
	 * @invar | bottomRight != null
	 * @invar | Point.ORIGIN.isUpAndLeftFrom(bottomRight)
	 */
	private final Point bottomRight;
	/**
	 * @invar | alphas != null
	 * @invar | Arrays.stream(alphas).allMatch(a -> a != null)
	 * @invar | Arrays.stream(alphas).allMatch(a -> getFieldInternal().contains(a.getLocation()))
	 * @invar alpha's exhaustiveness
	 * 		  | Arrays.stream(balls).allMatch(ball ->
	 * 		  | 	ball.getAlphas().stream().allMatch(alpha -> Arrays.stream(alphas).anyMatch(a -> a.equalsContent(alpha)))
	 * 		  | )
	 * @invar no duplicate references in alphas
	 * 		  | alphas.length == Set.of(alphas).size()
	 * 
	 * @representationObject
	 * @representationObjects
	 */
	private Alpha[] alphas;
	/**
	 * @invar | balls != null
	 * @invar | Arrays.stream(balls).allMatch(b -> b != null)
	 * @invar | Arrays.stream(balls).allMatch(b -> getFieldInternal().contains(b.getLocation()))
	 * @invar ball's exhaustiveness
	 * 		  | Arrays.stream(alphas).allMatch(alpha ->
	 * 		  | 	alpha.getBalls().stream().allMatch(ball -> Arrays.stream(balls).anyMatch(b -> b.equalsContent(ball)))
	 * 		  | )
	 * @invar no duplicate references in balls
	 * 		  | balls.length == Set.of(balls).size()
	 * 
	 * @representationObject
	 * @representationObjects
	 */
	private Ball[] balls;
	/**
	 * @invar | blocks != null
	 * @invar | Arrays.stream(blocks).allMatch(b -> getFieldInternal().contains(b.getLocation()))
	 * @representationObject
	 */
	private BlockState[] blocks;
	/**
	 * @invar | paddle != null
	 * @invar | getFieldInternal().contains(paddle.getLocation())
	 */
	private PaddleState paddle;

	private final Rect topWall;
	private final Rect rightWall;
	private final Rect leftWall;
	private final Rect[] walls;

	/**
	 * Construct a new BreakoutState with the given alphas, balls, blocks, paddle.
	 * 
	 * @throws IllegalArgumentException | balls == null
	 * @throws IllegalArgumentException | !Arrays.stream(balls).allMatch(b -> b != null)
	 * @throws IllegalArgumentException | alphas == null
	 * @throws IllegalArgumentException | !Arrays.stream(alphas).allMatch(a -> a != null)
	 * 
	 * @throws IllegalArgumentException
	 * 		  | !Arrays.stream(balls).allMatch(ball ->
	 * 		  | 	ball.getAlphas().stream().allMatch(alpha -> Arrays.stream(alphas).anyMatch(a -> a.equalsContent(alpha)))
	 * 		  | )
	 * @throws IllegalArgumentException
	 * 		  | !Arrays.stream(alphas).allMatch(alpha ->
	 * 		  | 	alpha.getBalls().stream().allMatch(ball -> Arrays.stream(balls).anyMatch(b -> b.equalsContent(ball)))
	 * 		  | )
	 * @throws IllegalArgumentExeption
	 * 		  | alphas.length != Set.of(alphas).size()
	 * @throws IllegalArgumentExeption
	 * 		  | balls.length != Set.of(balls).size()
	 * 
	 * @throws IllegalArgumentException | blocks == null
	 * @throws IllegalArgumentException | bottomRight == null
	 * @throws IllegalArgumentException | paddle == null
	 * 
	 * @throws IllegalArgumentException | !Point.ORIGIN.isUpAndLeftFrom(bottomRight)
	 * @throws IllegalArgumentException | !(new Rect(Point.ORIGIN,bottomRight)).contains(paddle.getLocation())
	 * @throws IllegalArgumentException | !Arrays.stream(blocks).allMatch(b -> (new Rect(Point.ORIGIN,bottomRight)).contains(b.getLocation()))
	 * @throws IllegalArgumentException | !Arrays.stream(balls).allMatch(b -> (new Rect(Point.ORIGIN,bottomRight)).contains(b.getLocation()))
	 * @throws IllegalArgumentException | !Arrays.stream(alphas).allMatch(a -> (new Rect(Point.ORIGIN,bottomRight)).contains(a.getLocation()))
	 * 
	 * @post | IntStream.range(0, balls.length).allMatch(i -> getBalls()[i].equalsContent(balls[i]))
	 * @post | IntStream.range(0, balls.length).allMatch(i ->
	 * 		 | 		balls[i].getAlphas().size() == getBalls()[i].getAlphas().size() &&
	 * 		 |		balls[i].getAlphas().stream().allMatch(a -> getBalls()[i].getAlphas().stream().anyMatch(al -> a.equalsContent(al))) &&
	 * 		 |		getBalls()[i].getAlphas().stream().allMatch(a -> balls[i].getAlphas().stream().anyMatch(al -> a.equalsContent(al)))
	 * 		 | )
	 * @post | IntStream.range(0, alphas.length).allMatch(i -> getAlphas()[i].equalsContent(alphas[i]))
	 * @post | IntStream.range(0, alphas.length).allMatch(i ->
	 * 		 | 		alphas[i].getBalls().size() == getAlphas()[i].getBalls().size() &&
	 * 		 |		alphas[i].getBalls().stream().allMatch(b -> getAlphas()[i].getBalls().stream().anyMatch(ba -> b.equalsContent(ba))) &&
	 * 		 |		getAlphas()[i].getBalls().stream().allMatch(b -> alphas[i].getBalls().stream().anyMatch(ba -> b.equalsContent(ba)))
	 * 		 | )
	 * @post | Arrays.equals(getBlocks(), blocks)
	 * @post | getBottomRight().equals(bottomRight)
	 * @post | getPaddle().equals(paddle)
	 */
	public BreakoutState(Alpha[] alphas, Ball[] balls, BlockState[] blocks, Point bottomRight, PaddleState paddle) {
		if (balls == null)
			throw new IllegalArgumentException();
		if (!Arrays.stream(balls).allMatch(b -> b != null)) {
			throw new IllegalArgumentException();
		}
		if (alphas == null)
			throw new IllegalArgumentException();
		if (!Arrays.stream(alphas).allMatch(a -> a != null)) {
			throw new IllegalArgumentException();
		}
		if (!Arrays.stream(balls).allMatch(ball -> ball.getAlphas().stream().allMatch(alpha -> Arrays.stream(alphas).anyMatch(a -> a.equalsContent(alpha))))) {
			throw new IllegalArgumentException();
		}
		if (!Arrays.stream(alphas).allMatch(alpha -> alpha.getBalls().stream().allMatch(ball -> Arrays.stream(balls).anyMatch(b -> b.equalsContent(ball))))) {
			throw new IllegalArgumentException();
		}
		if (alphas.length != Set.of(alphas).size()) {
			throw new IllegalArgumentException();
		}
		if (balls.length != Set.of(balls).size()) {
			throw new IllegalArgumentException();
		}

		if (blocks == null)
			throw new IllegalArgumentException();
		if (bottomRight == null)
			throw new IllegalArgumentException();
		if (paddle == null)
			throw new IllegalArgumentException();

		if (!Point.ORIGIN.isUpAndLeftFrom(bottomRight))
			throw new IllegalArgumentException();
		this.bottomRight = bottomRight;
		if (!getFieldInternal().contains(paddle.getLocation()))
			throw new IllegalArgumentException();
		if (!Arrays.stream(blocks).allMatch(b -> getFieldInternal().contains(b.getLocation())))
			throw new IllegalArgumentException();
		if (!Arrays.stream(balls).allMatch(b -> getFieldInternal().contains(b.getLocation())))
			throw new IllegalArgumentException();
		if (!Arrays.stream(alphas).allMatch(a -> getFieldInternal().contains(a.getLocation())))
			throw new IllegalArgumentException();

		// balls.clone() does a shallow copy by default
		BalphaContainer copy = fullCopy(alphas, balls);
		this.alphas = copy.alphas;
		this.balls = copy.balls;
		this.blocks = blocks.clone();
		this.paddle = paddle;

		this.topWall = new Rect(new Point(0, -1000), new Point(bottomRight.getX(), 0));
		this.rightWall = new Rect(new Point(bottomRight.getX(), 0),
				new Point(bottomRight.getX() + 1000, bottomRight.getY()));
		this.leftWall = new Rect(new Point(-1000, 0), new Point(0, bottomRight.getY()));
		this.walls = new Rect[] { topWall, rightWall, leftWall };
	}
	
	private BalphaContainer fullCopy(Alpha[] alphaArray, Ball[] ballArray) {
		Alpha[] alphasCopy = new Alpha[alphaArray.length];
		for (int i = 0 ; i < alphaArray.length ; i++) {
			alphasCopy[i] = alphaArray[i].clone();
		}
		Ball[] ballsCopy = new Ball[ballArray.length];
		for (int i = 0 ; i < ballArray.length ; i++) {
			ballsCopy[i] = ballArray[i].clone();
			for (Alpha a : ballArray[i].getAlphas()) {
				for (int j = 0 ; j < alphaArray.length ; j++) {
					if (a == alphaArray[j]) ballsCopy[i].linkTo(alphasCopy[j]);
				}
			}
		}
		
		return new BalphaContainer(alphasCopy, ballsCopy);
	}
	
	/**
	 * Return the alphas of this BreakoutState.
	 *
	 * @inspects | this
	 * @creates | result
     * @creates | ...result
	 */
	public Alpha[] getAlphas() {
		return fullCopy(alphas, balls).alphas;
	}

	/**
	 * Return the balls of this BreakoutState.
	 *
	 * @inspects | this
	 * @creates | result
     * @creates | ...result
	 */
	public Ball[] getBalls() {
		return fullCopy(alphas, balls).balls;
	}

	/**
	 * Return the blocks of this BreakoutState.
	 *
	 * @creates | result
	 */
	public BlockState[] getBlocks() {
		return blocks.clone();
	}

	/**
	 * Return the paddle of this BreakoutState.
	 */
	public PaddleState getPaddle() {
		return paddle;
	}

	/**
	 * Return the point representing the bottom right corner of this BreakoutState.
	 * The top-left corner is always at Coordinate(0,0).
	 */
	public Point getBottomRight() {
		return bottomRight;
	}

	// internal version of getField which can be invoked in partially inconsistent
	// states
	private Rect getFieldInternal() {
		return new Rect(Point.ORIGIN, bottomRight);
	}

	/**
	 * Return a rectangle representing the game field.
	 * 
	 * @post | result != null
	 * @post | result.getTopLeft().equals(Point.ORIGIN)
	 * @post | result.getBottomRight().equals(getBottomRight())
	 */
	public Rect getField() {
		return getFieldInternal();
	}

	private void bounceWalls(Ball ball) {
		for (Rect wall : walls) {
			if (ball.collidesWith(wall)) {
				ball.hitWall(wall);
			}
		}
	}
	
	private void bounceWalls(Alpha alpha) {
		for (Rect wall : walls) {
			if (alpha.collidesWith(wall)) {
				alpha.hitWall(wall);
				for (Ball b : alpha.getBalls()) {
					b.setVelocity(Vector.magnetSpeed(alpha.getLocation().getCenter(), b.getCenter(), b.getEcharge(), b.getVelocity()));
				}
			}
		}
	}

	private Ball removeDead(Ball ball) {
		if (ball.getLocation().getBottommostPoint().getY() > bottomRight.getY()) {
			ball.getAlphas().forEach(a -> ball.unLink(a));
			return null;
		}
		else { return ball; }
	}
	
	private Alpha removeDead(Alpha alpha) {
		if (alpha.getLocation().getBottommostPoint().getY() > bottomRight.getY()) { 
			alpha.getBalls().forEach(b -> b.unLink(alpha));
			return null; 
		}
		else { return alpha; }
	}

	private void clampBall(Ball b) {
		Circle loc = getFieldInternal().constrain(b.getLocation());
	    b.move(loc.getCenter().minus(b.getLocation().getCenter()),0);
	}

	private void clampAlpha(Alpha a) {
		Circle loc = getFieldInternal().constrain(a.getLocation());
	    a.move(loc.getCenter().minus(a.getLocation().getCenter()));
	}
	
	private Ball collideBallBlocks(Ball ball) {
		for (BlockState block : blocks) {
			if (ball.collidesWith(block.getLocation())) {
				boolean destroyed = hitBlock(block);
				ball.hitBlock(block.getLocation(), destroyed);
				paddle = block.paddleStateAfterHit(paddle);
				Ball newBall = block.ballStateAfterHit(ball);
				
				// When ball becomes supercharged, we need to manually update the links
				if (newBall != ball) {
					for (Alpha a: ball.getAlphas()) {
						newBall.linkTo(a);
						ball.unLink(a);
					}
				}
				
				return newBall;
			}
		}
		return ball;
	}

	private boolean hitBlock(BlockState block) {
		boolean destroyed = true;
		ArrayList<BlockState> nblocks = new ArrayList<BlockState>();
		for (BlockState b : blocks) {
			if (b != block) {
				nblocks.add(b);
			} else {
				BlockState nb = block.blockStateAfterHit();
				if (nb != null) {
					nblocks.add(nb);
					destroyed = false;
				}
			}
		}
		blocks = nblocks.toArray(new BlockState[] {});
		return destroyed;
	}

	/**
	 * Move all moving objects one step forward.
	 * 
	 * @mutates | this
	 * @mutates | ...getBalls()
	 * @pre | elapsedTime >= 0
	 * @pre | elapsedTime <= MAX_ELAPSED_TIME
	 */
	public void tick(int paddleDir, int elapsedTime) {
		stepBallsAndAlphas(elapsedTime);
		bounceBallsAndAlphasOnWalls();
		removeDeadBallsAndAlphas();
		bounceBallsOnBlocks();
		bounceBallsAndAlphasOnPaddle(paddleDir);
		clampBallsAndAlphas();
		balls = Arrays.stream(balls).filter(x -> x != null).toArray(Ball[]::new);
		alphas = Arrays.stream(alphas).filter(x -> x != null).toArray(Alpha[]::new);
	}

	private void clampBallsAndAlphas() {
		for(int i = 0; i < balls.length; ++i) {
			if(balls[i] != null) {
				clampBall(balls[i]);
			}		
		}
		for(int i = 0; i < alphas.length; ++i) {
			if(alphas[i] != null) {
				clampAlpha(alphas[i]);
			}		
		}
	}

	private void collideBallPaddle(Ball ball, Vector paddleVel) {
		if (ball.collidesWith(paddle.getLocation())) {
			ball.hitPaddle(paddle.getLocation(),paddleVel);
			
			Alpha newAlpha = new Alpha(ball.getLocation(), ball.getVelocity().plus(BALL_VEL_VARIATIONS[4]));
			ball.linkTo(newAlpha);
			Alpha[] newAlphas = new Alpha[alphas.length + 1];
			for(int i = 0; i < alphas.length; i++) {
				newAlphas[i] = alphas[i];
			}
			newAlphas[alphas.length] = newAlpha;
			alphas = newAlphas;
			
			int nrBalls = paddle.numberOfBallsAfterHit();
			if(nrBalls > 1) {
				Ball[] curballs = balls;
				balls = new Ball[curballs.length + nrBalls - 1];
				for(int i = 0; i < curballs.length; ++i) {
					balls[i] = curballs[i];
				}
				for(int i = 1; i < nrBalls; ++i) {
					Vector nballVel = ball.getVelocity().plus(BALL_VEL_VARIATIONS[i]);
					balls[curballs.length + i -1] = ball.cloneWithVelocity(nballVel);					
				}
			}

			paddle = paddle.stateAfterHit();
		}
	}
	
	private void collideAlphaPaddle(Alpha alpha, Vector paddleVel) {
		if (alpha.collidesWith(paddle.getLocation())) {
			alpha.hitPaddle(paddle.getLocation(), paddleVel);
			Ball newBall = new NormalBall(alpha.getLocation(), alpha.getVelocity().plus(BALL_VEL_VARIATIONS[4]));
			newBall.linkTo(alpha);
			Ball[] newBalls = new Ball[balls.length + 1];
			for(int i = 0; i < balls.length; i++) {
				newBalls[i] = balls[i];
			}
			newBalls[balls.length] = newBall;
			balls = newBalls;
		}
	}

	private void bounceBallsAndAlphasOnPaddle(int paddleDir) {
		Vector paddleVel = PADDLE_VEL.scaled(paddleDir);
		for(int i = 0; i < balls.length; ++i) {
			if(balls[i] != null) {
				collideBallPaddle(balls[i], paddleVel);
			}
		}
		for(int i = 0; i < alphas.length; ++i) {
			if(alphas[i] != null) {
				collideAlphaPaddle(alphas[i], paddleVel);
			}
		}
	}

	private void bounceBallsOnBlocks() {
		for(int i = 0; i < balls.length; ++i) {
			if(balls[i] != null) {
				balls[i] = collideBallBlocks(balls[i]);
			}
		}
	}

	private void removeDeadBallsAndAlphas() {
		for(int i = 0; i < balls.length; ++i) {
			balls[i] = removeDead(balls[i]);
		}
		for(int i = 0; i < alphas.length; ++i) {
			alphas[i] = removeDead(alphas[i]);
		}
	}

	private void bounceBallsAndAlphasOnWalls() {
		for(int i = 0; i < balls.length; ++i) {
			bounceWalls(balls[i]);
		}
		for(int i = 0; i < alphas.length; ++i) {
			bounceWalls(alphas[i]);
		}
	}

	private void stepBallsAndAlphas(int elapsedTime) {
		for(int i = 0; i < balls.length; ++i) {
			balls[i].move(balls[i].getVelocity().scaled(elapsedTime), elapsedTime);
		}
		for(int i = 0; i < alphas.length; ++i) {
			alphas[i].move(alphas[i].getVelocity().scaled(elapsedTime));
		}
	}
	/**
	 * Move the paddle right.
	 * 
	 * @param elapsedTime
	 * 
	 * @mutates | this
	 */
	public void movePaddleRight(int elapsedTime) {
		paddle = paddle.move(PADDLE_VEL.scaled(elapsedTime), getField());
	}

	/**
	 * Move the paddle left.
	 * 
	 * @mutates | this
	 */
	public void movePaddleLeft(int elapsedTime) {
		paddle = paddle.move(PADDLE_VEL.scaled(-elapsedTime), getField());
	}

	/**
	 * Return whether this BreakoutState represents a game where the player has won.
	 * 
	 * @post | result == (getBlocks().length == 0 && !isDead())
	 * @inspects | this
	 */
	public boolean isWon() {
		return getBlocks().length == 0 && !isDead();
	}

	/**
	 * Return whether this BreakoutState represents a game where the player is dead.
	 * 
	 * @post | result == (getBalls().length == 0)
	 * @inspects | this
	 */
	public boolean isDead() {
		return getBalls().length == 0;
	}
}
