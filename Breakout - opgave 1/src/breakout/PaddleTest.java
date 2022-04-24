package breakout;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaddleTest {
	Point p11;
	PaddleState p1;
	PaddleState p2;

	@BeforeEach
	void setUp() throws Exception {
		p11 = new Point(5000,5000);
		p1 = new NormalPaddle(p11);
		p2 = new ReplicatorPaddle(p11, 3);
	}

	@Test
	void testPaddle() {
		assertEquals(p11, p1.getCenter());
	}

	@Test
	void testGetLocation() {
		assertEquals(new Point(5000-PaddleState.WIDTH/2,5000-PaddleState.HEIGHT/2), p1.getLocation().getTopLeft());
		assertEquals(new Point(5000+PaddleState.WIDTH/2,5000+PaddleState.HEIGHT/2), p1.getLocation().getBottomRight());
	}

	@Test
	void testCreatePaddle() {
		assertEquals(p1.getCenter(), p1.createPaddle(p1.getCenter()).getCenter());
		assertEquals(p2.getCenter(), p2.createPaddle(p2.getCenter()).getCenter());
	}
	
	@Test 
	void testHandleCollision() {
		BreakoutState dummyGame = new BreakoutState(new Ball[0], new BlockState[0], new Point(10000, 10000), p2);
		assertEquals(p2.handleCollision(dummyGame), 3);
		assertTrue(dummyGame.getPaddle() instanceof ReplicatorPaddle);
		assertEquals(p2.handleCollision(dummyGame), 2);		
		assertTrue(dummyGame.getPaddle() instanceof ReplicatorPaddle);
		assertEquals(p2.handleCollision(dummyGame), 1);		
		assertTrue(dummyGame.getPaddle() instanceof NormalPaddle);
		
		assertEquals(p1.handleCollision(dummyGame), 0);	
	}
	
	@Test
	void testColor() {
		assertEquals(new Color(0x99,0xff,0xff), p1.getColor());
		assertEquals(new Color(0xfa, 0xff, 0x7a), p2.getColor());
	}
}
