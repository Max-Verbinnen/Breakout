package breakout;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BlockTest {
	Point p11;
	Point p29;
	Point p38;
	Point pm14;
	
	Rect r1138;
	Rect rm1438;
	
	BlockState b1;
	BlockState b2;
	BlockState b3;
	BlockState b4;

	@BeforeEach
	void setUp() throws Exception {
		p11 = new Point(1,1);
		p29 = new Point(2,9);
		p38 = new Point(3,8);
		pm14 = new Point(-1,4);
		r1138 = new Rect(p11,p38);
		rm1438 = new Rect(pm14,p38);
		b1 = new NormalBlock(r1138);
		b2 = new ReplicationBlock(r1138);
		b3 = new PowerupBlock(r1138);
		b4 = new SturdyBlock(r1138, 3);
	}

	@Test
	void testBlock() {
		assertEquals(r1138,b1.getLocation());
	}

	@Test
	void testHandleCollision() {
		Ball ball1 = new NormalBall(new Circle(p29, 2), new Vector(10, -10));
		Ball ball2 = new NormalBall(new Circle(p38, 2), new Vector(10, 10));
		BreakoutState dummyGame = new BreakoutState(new Ball[] {ball1, ball2}, new BlockState[] {b1, b2, b3, b4}, new Point(10000, 10000), new NormalPaddle(new Point(5000, 5000)));
		
		b1.handleCollision(dummyGame, ball1);
		assertEquals(3, dummyGame.getBlocks().length);
		b2.handleCollision(dummyGame, ball1);
		assertEquals(2, dummyGame.getBlocks().length);
		b3.handleCollision(dummyGame, ball1);
		assertEquals(1, dummyGame.getBlocks().length);
		b4.handleCollision(dummyGame, ball1);
		assertEquals(1, dummyGame.getBlocks().length);
		b4.handleCollision(dummyGame, ball1);
		assertEquals(1, dummyGame.getBlocks().length);
		b4.handleCollision(dummyGame, ball1);
		assertEquals(0, dummyGame.getBlocks().length);
	}
	
	@Test
	void testColor() {
		assertEquals(b1.getColor(), new Color(0x80,0x00,0xff));
		assertEquals(b2.getColor(), new Color(0xfa, 0xff, 0x7a));
		assertEquals(b3.getColor(), Color.magenta);
		assertEquals(b4.getColor(), Color.DARK_GRAY);
	}
}
