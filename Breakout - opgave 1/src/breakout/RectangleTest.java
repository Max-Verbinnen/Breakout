package breakout;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RectangleTest {
	Rectangle r1;
	Rectangle r2;
	BallState[] balls1 = new BallState[5];
	BallState[] balls2 = new BallState[2];
	
	@BeforeEach
	void setUp() throws Exception {
		r1 = new Rectangle(new Point(10, 11), new Point(30, 31), false);
		r2 = new Rectangle(new Point(1, 7), new Point(100, 200), true);
		
		balls1[0] = new BallState(new Point(15, 10), 4, new Vector(2, 7));
		balls1[1] = new BallState(new Point(15, 29), 4, new Vector(2, 7));
		balls1[2] = new BallState(new Point(15, 1), 4, new Vector(2, 7));
		balls1[3] = new BallState(new Point(9, 20), 4, new Vector(2, 7));
		balls1[4] = new BallState(new Point(15, 32), 4, new Vector(2, -7));
		
		balls2[0] = new BallState(new Point(99, 150), 4, new Vector(2, 7));
		balls2[1] = new BallState(new Point(101, 150), 4, new Vector(-2, 7));
	}
	
	@Test
	void testBallState() {
		assertEquals(10, r1.getLeftBound());
		assertEquals(11, r1.getTopBound());
		assertEquals(30, r1.getRightBound());
		assertEquals(31, r1.getBottomBound());
		
		assertEquals(Vector.RIGHT, r1.getLeftBoundVector());
		assertEquals(Vector.DOWN, r1.getTopBoundVector());
		assertEquals(Vector.LEFT, r1.getRightBoundVector());
		assertEquals(Vector.UP, r1.getBottomBoundVector());
		
		assertEquals(Vector.LEFT, r2.getLeftBoundVector());
		assertEquals(Vector.UP, r2.getTopBoundVector());
		assertEquals(Vector.RIGHT, r2.getRightBoundVector());
		assertEquals(Vector.DOWN, r2.getBottomBoundVector());
	}
	
	@Test
	void testCollide() {
		assertArrayEquals(r1.collide(balls1, false), new int[] {0, 3, 4});
		assertArrayEquals(r2.collide(balls2, false), new int[] {0});
	}
	
	@Test
	void testCollideOnce() {
		assertArrayEquals(r1.collide(balls1, true), new int[] {0});
		assertArrayEquals(r1.collide(balls1, true), new int[] {3});
		assertArrayEquals(r1.collide(balls1, true), new int[] {4});

		assertArrayEquals(r2.collide(balls2, true), new int[] {0});
	}

}
