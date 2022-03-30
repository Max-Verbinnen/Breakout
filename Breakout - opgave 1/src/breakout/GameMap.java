package breakout;

import java.util.ArrayList;
import java.util.Arrays;

public class GameMap {
	
	private static final int INIT_BALL_DIAMETER = 700;
	private static final int HEIGHT = 30000;
	private static final int WIDTH = 50000;
	private static int BLOCK_LINES = 8;
	private static int BLOCK_COLUMNS = 10;
	private static final Vector INIT_BALL_VELOCITY = new Vector(5,7);

	private GameMap() { throw new AssertionError("This class is not intended to be instantiated"); }

	private static BlockState createBlock(Point bottomLeft, char blockType) {
		Vector marginBL = new Vector(20,20);
		Vector size = new Vector(WIDTH/BLOCK_COLUMNS-70,HEIGHT/BLOCK_LINES-70);
		Point blockTL = bottomLeft.plus(marginBL);
		Point blockBR = blockTL.plus(size);
		Rect loc = new Rect(blockTL,blockBR);
		
		switch (blockType) {
			case '#': return new NormalBlock(loc);
			case 'S': return new SturdyBlock(loc);
			case '!': return new PowerupBlock(loc);
			case 'R': return new ReplicationBlock(loc);
		}
		
		return null;
	}
	private static PaddleState createPaddle(Point bottomLeft) {
		Vector size = new Vector(WIDTH/BLOCK_COLUMNS/2,HEIGHT/BLOCK_LINES/2);
		Point center = bottomLeft.plus(size);
		return new PaddleState(center);
	}
	private static Ball createBall(Point bottomLeft) {
		Vector centerD = new Vector(WIDTH/BLOCK_COLUMNS/2,HEIGHT/BLOCK_LINES/2);
		Point center = bottomLeft.plus(centerD);
		int diameter = INIT_BALL_DIAMETER;
		return new NormalBall(new Circle(center,diameter),INIT_BALL_VELOCITY);
	}
	
	/**
	 * Return the initial breakout state represented by string `description`.
	 * @pre | description != null
	 * @post | result != null
	 */
	public static BreakoutState createStateFromDescription(String description) {
		String[] lines = description.split("\n", BLOCK_LINES);
		
		Vector unitVecRight = new Vector(WIDTH/BLOCK_COLUMNS,0);
		Vector unitVecDown = new Vector(0,HEIGHT/BLOCK_LINES);
		BlockState[] blocks = new BlockState[BLOCK_COLUMNS*BLOCK_LINES];
		int nblock = 0;
		Ball[] balls = new Ball[BLOCK_COLUMNS*BLOCK_LINES];
		int nball = 0;
		PaddleState paddle = null;
		
		Point topLeft = new Point(0,0);
		assert lines.length <= BLOCK_LINES;
		for(String line : lines) {
			assert line.length() <= BLOCK_COLUMNS;
			Point cursor = topLeft;
			for(char c : line.toCharArray()) {
				switch(c) {
					case '#': blocks[nblock++] = createBlock(cursor, c); break;
					case 'S': blocks[nblock++] = createBlock(cursor, c); break;
					case '!': blocks[nblock++] = createBlock(cursor, c); break;
					case 'R': blocks[nblock++] = createBlock(cursor, c); break;
					case 'o': balls[nball++] = createBall(cursor); break;
					case '=': paddle = createPaddle(cursor); break;
				}
				cursor = cursor.plus(unitVecRight);
			}
			topLeft = topLeft.plus(unitVecDown);
		}
		Point topRight = new Point(WIDTH, HEIGHT);
		
		return new BreakoutState(Arrays.stream(balls).filter(x -> x != null).toArray(Ball[]::new),
								 Arrays.stream(blocks).filter(x -> x != null).toArray(BlockState[]::new),
								 topRight, paddle);
	}
}
