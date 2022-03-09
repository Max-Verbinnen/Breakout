package breakout;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BreakoutStateTest {
	BreakoutState bs1;
	BreakoutState bs2;
	BreakoutState bs3;
	BreakoutState bs4;
	
	@BeforeEach
	void setUp() throws Exception {
		bs1 = new BreakoutState( // Simplified game
					new BallState[] {new BallState(new Point(25000, 15000), 200, new Vector(5, 7))},
					new BlockState[] {new BlockState(new Point(10, 10), new Point(30, 30)), new BlockState(new Point(35, 10), new Point(55, 30))},
					new Point(50000, 30000),
					new PaddleState(new Point(25000, 22000))
				  );
		bs2 = new BreakoutState( // Won
					new BallState[] {new BallState(new Point(25000, 15000), 200, new Vector(5, 7))},
					new BlockState[] {},
					new Point(50000, 30000),
					new PaddleState(new Point(25000, 22000))
				  );
		bs3 = new BreakoutState( // Dead
					new BallState[] {},
					new BlockState[] {new BlockState(new Point(10, 10), new Point(30, 30))},
					new Point(50000, 30000),
					new PaddleState(new Point(25000, 22000))
				  );
		bs4 = new BreakoutState( // Ball hits side
					new BallState[] {new BallState(new Point(1, 1), 200, new Vector(-1, 1)), new BallState(new Point(10000, 29898), 200, new Vector(1, 1))},
					new BlockState[] {new BlockState(new Point(60, 60), new Point(90, 90)), new BlockState(new Point(55, 10), new Point(75, 30))},
					new Point(50000, 30000),
					new PaddleState(new Point(25000, 22000))
			      );
	}
	
	@Test
	void testBallState() {
		// Global getters
		assertEquals(new Point(25000, 15000), bs1.getBalls()[0].getCenter());
		assertEquals(200, bs1.getBalls()[0].getDiameter());
		assertEquals(new Vector(5, 7), bs1.getBalls()[0].getVelocity());

		assertEquals(new Point(10, 10), bs1.getBlocks()[0].getTopLeft());
		assertEquals(new Point(30, 30), bs1.getBlocks()[0].getBottomRight());
		assertEquals(new Point(35, 10), bs1.getBlocks()[1].getTopLeft());
		assertEquals(new Point(55, 30), bs1.getBlocks()[1].getBottomRight());
		
		assertEquals(new Point(25000, 22000), bs1.getPaddle().getCenter());
		assertEquals(new Point(50000, 30000), bs1.getBottomRight());
		
		// Tick method
		BallState oldBall = bs1.getBalls()[0];
		bs1.tick(0);
		assertEquals(oldBall.getCenter().plus(oldBall.getVelocity()), bs1.getBalls()[0].getCenter());
		
		BallState topBall = bs4.getBalls()[0];
		bs4.tick(0);
		assertEquals(topBall.getVelocity().mirrorOver(Vector.LEFT), bs4.getBalls()[0].getVelocity());
		
		BallState bottomBall = bs4.getBalls()[1];
		bs4.tick(0);
		assertTrue(bs4.getBalls().length == 1);
		
		// Paddle movement
		Point oldPaddlePosition = bs1.getPaddle().getCenter();
		bs1.movePaddleRight();
		assertEquals(oldPaddlePosition.plus(new Vector(10, 0)), bs1.getPaddle().getCenter());
		bs1.movePaddleLeft();
		assertEquals(oldPaddlePosition, bs1.getPaddle().getCenter());
		
		// Win & Lose situation
		assertFalse(bs1.isWon());
		assertFalse(bs1.isDead());
		assertTrue(bs2.isWon());
		assertFalse(bs2.isDead());
		assertFalse(bs3.isWon());
		assertTrue(bs3.isDead());
	}

}
