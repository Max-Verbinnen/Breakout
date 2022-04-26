package breakout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Represents the current state of a breakout game.
 *  
 * @invar | getBalls() != null
 * @invar | getBlocks() != null
 * @invar | getPaddle() != null
 * @invar | getBottomRight() != null
 * @invar | Point.ORIGIN.isUpAndLeftFrom(getBottomRight())
 * @invar | Arrays.stream(getBlocks()).allMatch(b -> getField().contains(b.getLocation()))
 * @invar | getField().contains(getPaddle().getLocation())
 */
public class BreakoutState {

	private static final Vector PADDLE_VEL = new Vector(10,0);
	public static final int MAX_ELAPSED_TIME = 50;
	/**
	 * @invar | bottomRight != null
	 * @invar | Point.ORIGIN.isUpAndLeftFrom(bottomRight)
	 */
	private final Point bottomRight;
	/**
	 * @invar | balls != null
	 * @representationObject
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
	 * Construct a new BreakoutState with the given balls, blocks, paddle.
	 * 
	 * @throws IllegalArgumentException | balls == null
	 * @throws IllegalArgumentException | blocks == null
	 * @throws IllegalArgumentException | bottomRight == null
	 * @throws IllegalArgumentException | paddle == null
	 * @throws IllegalArgumentException | !Point.ORIGIN.isUpAndLeftFrom(bottomRight)
	 * @throws IllegalArgumentException | !(new Rect(Point.ORIGIN,bottomRight)).contains(paddle.getLocation())
	 * @throws IllegalArgumentException | !Arrays.stream(blocks).allMatch(b -> (new Rect(Point.ORIGIN,bottomRight)).contains(b.getLocation()))
	 * @throws IllegalArgumentException | !Arrays.stream(balls).allMatch(b -> (new Rect(Point.ORIGIN,bottomRight)).contains(b.getLocation()))
	 * @post | Arrays.equals(getBalls(),balls)
	 * @post | Arrays.equals(getBlocks(),blocks)
	 * @post | getBottomRight().equals(bottomRight)
	 * @post | getPaddle().equals(paddle)
	 */
	public BreakoutState(Ball[] balls, BlockState[] blocks, Point bottomRight, PaddleState paddle) {
		if( balls == null) throw new IllegalArgumentException();
		if( blocks == null) throw new IllegalArgumentException();
		if( bottomRight == null) throw new IllegalArgumentException();
		if( paddle == null) throw new IllegalArgumentException();

		if(!Point.ORIGIN.isUpAndLeftFrom(bottomRight)) throw new IllegalArgumentException();
		this.bottomRight = bottomRight;
		if(!getFieldInternal().contains(paddle.getLocation())) throw new IllegalArgumentException();
		if(!Arrays.stream(blocks).allMatch(b -> getFieldInternal().contains(b.getLocation()))) throw new IllegalArgumentException();
		if(!Arrays.stream(balls).allMatch(b -> getFieldInternal().contains(b.getLocation()))) throw new IllegalArgumentException();
	
		this.balls = balls.clone();
		this.blocks = blocks.clone();
		this.paddle = paddle;

		this.topWall = new Rect( new Point(0,-1000), new Point(bottomRight.getX(),0));
		this.rightWall = new Rect( new Point(bottomRight.getX(),0), new Point(bottomRight.getX()+1000,bottomRight.getY()));
		this.leftWall = new Rect( new Point(-1000,0), new Point(0,bottomRight.getY()));
		this.walls = new Rect[] {topWall,rightWall, leftWall };
	}

	/**
	 * Return the balls of this BreakoutState.
	 * 
	 * @creates | result
	 */
	public Ball[] getBalls() {
		return balls.clone();
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

	// internal version of getField which can be invoked in partially inconsistent states
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

	private Ball removeDead(Ball ball) {
		if (ball.getLocation().getBottommostPoint().getY() > bottomRight.getY()) { return null; }
		else { return ball; }
	}

	/**
	 * Remove given block from blocks.
	 * 
	 * @throws IllegalArgumentException | block == null
	 * 
	 * @post | Arrays.equals(getBlocks(), Arrays.stream(old(getBlocks())).filter(x -> x != block).toArray(BlockState[]::new))
	 * 
	 * @mutates | this
	 */
	public void removeBlock(BlockState block) {
		
		if( blocks == null) throw new IllegalArgumentException();
		
		ArrayList<BlockState> nblocks = new ArrayList<BlockState>();
		for( BlockState b : blocks ) {
			if(b != block) {
				nblocks.add(b);
			}
		}
		blocks = nblocks.toArray(new BlockState[] {});
	}
	
	/**
	 * Replace ball with a normal ball.
	 * 
	 * @throws IllegalArgumentException | ball == null
	 * 
	 * @post | getBalls().length == old(getBalls().length)
	 * @post | IntStream.range(0, getBalls().length).filter(i -> old(getBalls())[i] == ball)
	 * 		 | .allMatch(i -> getBalls()[i] instanceof NormalBall)
	 * @post | IntStream.range(0, getBalls().length).filter(i -> old(getBalls())[i] != ball)
	 * 		 | .allMatch(i -> getBalls()[i] == old(getBalls())[i])
	 * 
	 * @mutates | this
	 */
	public void makeBallNormal(Ball ball) {		
		
		if (ball == null) throw new IllegalArgumentException();
		
		for (int i = 0; i < balls.length; i++) {
			if (balls[i].equals(ball)) {
				balls[i] = new NormalBall(ball.getLocation(), ball.getVelocity());
			}
		}
	}
	
	/**
	 * Replace ball with a supercharged ball.
	 * 
	 * @throws IllegalArgumentException | ball == null
	 * 
	 * @post | getBalls().length == old(getBalls().length)
	 * @post | IntStream.range(0, getBalls().length).filter(i -> old(getBalls())[i] == ball)
	 * 		 | .allMatch(i -> getBalls()[i] instanceof SuperchargedBall)
	 * @post | IntStream.range(0, getBalls().length).filter(i -> old(getBalls())[i] != ball)
	 * 		 | .allMatch(i -> getBalls()[i] == old(getBalls())[i])
	 * 
	 * @mutates | this
	 */
	public void makeBallSupercharged(Ball ball) {
		
		if (ball == null) throw new IllegalArgumentException();
		
		for (int i = 0; i < balls.length; i++) {
			if (balls[i].equals(ball)) {
				balls[i] = new SuperchargedBall(ball.getLocation(), ball.getVelocity(), 10000);
			}
		}
	}
	
	/**
	 * Replace `oldBlock` with `newBlock`.
	 * 
	 * @throws IllegalArgumentException | oldBlock == null
	 * @throws IllegalArgumentException | newBlock == null
	 * 
	 * @post | getBlocks().length == old(getBlocks().length)
	 * @post | IntStream.range(0, getBlocks().length).filter(i -> old(getBlocks())[i] == oldBlock)
	 * 		 | .allMatch(i -> getBlocks()[i] == newBlock)
	 * @post | IntStream.range(0, getBlocks().length).filter(i -> old(getBlocks())[i] != oldBlock)
	 * 		 | .allMatch(i -> getBlocks()[i] == old(getBlocks())[i])
	 * 
	 * @mutates | this
	 */
	public void replaceBlock(BlockState oldBlock, BlockState newBlock) {
		if (oldBlock == null) throw new IllegalArgumentException();
		if (newBlock == null) throw new IllegalArgumentException();
		 
		for (int i = 0; i < blocks.length; i++) {
			if (blocks[i] == oldBlock) {
				blocks[i] = newBlock;
			}
		}
	}
	
	/**
	 * Replace current paddle with new paddle.
	 * 
	 * @throws IllegalArgumentException | newPaddle == null
	 * 
	 * @post | getPaddle().getCenter().equals(old(getPaddle().getCenter()))
	 * @post | getPaddle() == newPaddle
	 * 
	 * @mutates | this
	 */
	public void replacePaddle(PaddleState newPaddle) {
		if (newPaddle == null) throw new IllegalArgumentException();
		paddle = newPaddle;
	}
	
	/**
	 * Add new balls to the game.
	 * 
	 * @throws IllegalArgumentException | newBalls == null
	 * @throws IllegalArgumentException | !Arrays.stream(newBalls).allMatch(b -> b != null)
	 * 
	 * @post | getBalls().length == old(getBalls().length) + newBalls.length
	 * @post | IntStream.range(0, getBalls().length).allMatch(i -> (i < old(getBalls().length) ? getBalls()[i] == old(getBalls())[i] : getBalls()[i] == newBalls[i - old(getBalls().length)]))
	 *
	 * @mutates | this
	 */
	public void addBalls(Ball[] newBalls) {
		
		if( newBalls == null) throw new IllegalArgumentException();
		if( !Arrays.stream(newBalls).allMatch(b -> b != null)) throw new IllegalArgumentException();
		
		Ball[] finalBalls = new Ball[balls.length + newBalls.length];
		
		System.arraycopy(balls, 0, finalBalls, 0, balls.length);
		System.arraycopy(newBalls, 0, finalBalls, balls.length, newBalls.length);
		
		balls = finalBalls;
	}

	/**
	 * Move all moving objects one step forward.
	 * 
	 * @mutates this
	 */
	public void tick(int paddleDir, int elapsedTime) {
		stepBalls(elapsedTime);
		bounceBallsOnWalls();
		removeDeadBalls();
		bounceBallsOnBlocks();
		bounceBallsOnPaddle(paddleDir);
		clampBalls();
		balls = Arrays.stream(balls).filter(x -> x != null).toArray(Ball[]::new);
	}

	private void clampBalls() {
		for (Ball ball: balls) {
			if (ball != null) {
				Circle loc = getFieldInternal().constrain(ball.getLocation());
				ball.setLocation(loc);
			}
		}
	}

	private void bounceBallsOnPaddle(int paddleDir) {
		Vector paddleVel = PADDLE_VEL.scaled(paddleDir);
		
		for (Ball ball: balls) {
			if (ball != null) {
				Vector nspeed = ball.bounceOn(paddle.getLocation());

				if (nspeed != null) {
					Point ncenter = ball.getLocation().getCenter().plus(nspeed);
					nspeed = nspeed.plus(paddleVel.scaledDiv(5));
					ball.setLocation(ball.getLocation().withCenter(ncenter));
					ball.setVelocity(nspeed);
					
					int additionalBalls = paddle.handleCollision(this);
					ball.replicateBalls(additionalBalls, this);
				}
			}
		}
	}

	private void bounceBallsOnBlocks() {
		for (int i = 0; i < balls.length; i++) {
			Ball ball = balls[i];

			for (BlockState block: blocks) {
				if (ball == null || block == null || ball.bounceOn(block.getLocation()) == null) continue;
				
				boolean destroyed = block.handleCollision(this, ball);
				ball = balls[i]; // 'Refresh' ball
				ball.hitBlock(block.getLocation(), destroyed);
			}
		}
	}

	private void removeDeadBalls() {
		for(int i = 0; i < balls.length; ++i) {
			balls[i] = removeDead(balls[i]);
		}
	}

	private void bounceBallsOnWalls() {
		for (Ball ball: balls) {
			Circle loc = ball.getLocation();

			for (Rect wall : walls) {
				Vector nspeed = ball.bounceOn(wall);
				if (nspeed != null) {
					ball.setVelocity(nspeed);
				}
			}
		}
	}

	private void stepBalls(int elapsedTime) {
		for (Ball ball: balls) {
			Point newcenter = ball.getLocation().getCenter().plus(ball.getVelocity().scaled(elapsedTime));
			ball.setLocation(ball.getLocation().withCenter(newcenter));
			ball.updateLifetime(elapsedTime, this);
		}
	}

	/**
	 * Move the paddle right.
	 * 
	 * @throws IllegalArgumentException | elapsedTime < 0
	 * 
	 * @mutates this
	 */
	public void movePaddleRight(int elapsedTime) {
		
		if (elapsedTime < 0) throw new IllegalArgumentException();
		
		Point ncenter = paddle.getCenter().plus(PADDLE_VEL.scaled(elapsedTime));
		paddle = paddle.createPaddle(getField().minusMargin(PaddleState.WIDTH/2,0).constrain(ncenter));
	}

	/**
	 * Move the paddle left.
	 * 
	 * @throws IllegalArgumentException | elapsedTime < 0
	 * 
	 * @mutates this
	 */
	public void movePaddleLeft(int elapsedTime) {
		
		if (elapsedTime < 0) throw new IllegalArgumentException();
		
		Point ncenter = paddle.getCenter().plus(PADDLE_VEL.scaled(elapsedTime).scaled(-1));
		paddle = paddle.createPaddle(getField().minusMargin(PaddleState.WIDTH/2,0).constrain(ncenter));
	}

	/**
	 * Return whether this BreakoutState represents a game where the player has won.
	 * 
	 * @post | result == (getBlocks().length == 0 && !isDead())
	 * @inspects this
	 */
	public boolean isWon() {
		return getBlocks().length == 0 && !isDead();
	}

	/**
	 * Return whether this BreakoutState represents a game where the player is dead.
	 * 
	 * @post | result == (getBalls().length == 0)
	 * @inspects this
	 */
	public boolean isDead() {
		return getBalls().length == 0;
	}
}
