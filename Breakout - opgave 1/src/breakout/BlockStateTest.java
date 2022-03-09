package breakout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BlockStateTest {
	BlockState bs1;
	BlockState bs2;
	
	@BeforeEach
	void setUp() throws Exception {
		bs1 = new BlockState(new Point(0, 1), new Point(4, 4));
		bs2 = new BlockState(new Point(5, 7), new Point(12, 16));
	}
	
	@Test
	void testBallState() {
		assertEquals(new Point(0, 1), bs1.getTopLeft());
		assertEquals(new Point(5, 7), bs2.getTopLeft());
		assertEquals(new Point(4, 4), bs1.getBottomRight());
		assertEquals(new Point(12, 16), bs2.getBottomRight());
	}

}
