package breakout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaddleStateTest {
	PaddleState ps1;
	PaddleState ps2;
	
	@BeforeEach
	void setUp() throws Exception {
		ps1 = new PaddleState(new Point(0, 1));
		ps2 = new PaddleState(new Point(5, 7));
	}
	
	@Test
	void testBallState() {
		assertEquals(new Point(0, 1), ps1.getCenter());
		assertEquals(new Point(5, 7), ps2.getCenter());
	}

}
