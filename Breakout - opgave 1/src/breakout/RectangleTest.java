package breakout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RectangleTest {
	Rectangle r1;
	Rectangle r2;
	
	@BeforeEach
	void setUp() throws Exception {
		r1 = new Rectangle(new Point(10, 11), new Point(30, 31), false);
		r2 = new Rectangle(new Point(1, 7), new Point(100, 200), true);
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

}
