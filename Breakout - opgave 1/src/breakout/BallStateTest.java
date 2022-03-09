package breakout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BallStateTest {
	BallState bs1;
	BallState bs2;
	
	@BeforeEach
	void setUp() throws Exception {
		bs1 = new BallState(new Point(10, 12), 5, new Vector(3, 5));
		bs2 = new BallState(new Point(40, 25), 9, new Vector(7, 3));
	}
	
	@Test
	void testBallState() {
		assertEquals(new Point(10, 12), bs1.getCenter());
		assertEquals(new Point(40, 25), bs2.getCenter());
		assertEquals(5, bs1.getDiameter());
		assertEquals(9, bs2.getDiameter());
		assertEquals(new Vector(3, 5), bs1.getVelocity());
		assertEquals(new Vector(7, 3), bs2.getVelocity());
	}

}
