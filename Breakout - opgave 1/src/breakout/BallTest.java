package breakout;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BallTest {
	Point p11;
	Point p05;
	Point p38;
	Point pm14;
	
	Rect r1138;
	Rect rm1438;
	
	Vector v1010;
	Vector v55;
	
	Circle c052;
	Circle c382;
	Circle c389;
	
	Ball b1;
	Ball b2;
	
	Ball b1s;
	Ball b2s;
	
	@BeforeEach
	void setUp() throws Exception {
		p11 = new Point(1,1);
		p05 = new Point(0,5);
		p38 = new Point(3,8);
		pm14 = new Point(-1,4);
		r1138 = new Rect(p11,p38);
		rm1438 = new Rect(pm14,p38);
		c052 = new Circle(p05,2);
		c382 = new Circle(p38,2);
		c389 = new Circle(p38,9);
		v1010 = new Vector(10,10);
		v55 = new Vector(5, 5);
		b1 = new NormalBall(c052, v1010);
		b2 = new NormalBall(c382, v55);
		b1s = new SuperchargedBall(c052, v1010, 10000);
		b2s = new SuperchargedBall(c382, v55, 10000);
	}

	@Test
	void testBall() {
		assertEquals(p05, b1.getCenter());
		assertEquals(2, b1.getDiameter());
		assertEquals(v1010, b1.getVelocity());
		b1.setLocation(c389);
		b1.setVelocity(v55);
		assertEquals(p38, b1.getCenter());
		assertEquals(9, b1.getDiameter());
		assertEquals(v55, b1.getVelocity());
	}

	@Test
	void testBounceOn() {
		assertEquals(new Vector(-10,10),b1.bounceOn(r1138));
		b1.setVelocity(new Vector(-10, 10));
		assertEquals(null,b1.bounceOn(r1138));
		assertEquals(null,b1.bounceOn(new Rect(new Point(10, 10), new Point(20, 20))));
	}
	
	@Test
	void testReplicateBalls() {
		BreakoutState dummyGame = new BreakoutState(new Ball[] {b2, b2s}, new BlockState[0], new Point(10000, 10000), new NormalPaddle(new Point(5000, 5000)));
		b2.replicateBalls(2, dummyGame);
		assertEquals(4, dummyGame.getBalls().length);
		b2s.replicateBalls(3, dummyGame);
		assertEquals(7, dummyGame.getBalls().length);
		
		b2.updateLifetime(1000, dummyGame); //Does nothing
		b2s.updateLifetime(5000, dummyGame);
		assertEquals(b2s.getLifetime(), 5000);
		b2s.updateLifetime(5025, dummyGame);
	}

	@Test
	void testColor() {
		assertEquals(Color.yellow, b1.getColor());
		assertEquals(new Color(Color.HSBtoRGB(0, 1, 1)), b1s.getColor());
	}
	
	@Test
	void testHitBlock() {
		b1.hitBlock(r1138, true);
		assertEquals(b1.getVelocity(), new Vector(-10, 10));
		b1s.hitBlock(r1138, true);
		assertEquals(b1s.getVelocity(), new Vector(10, 10));
		b1s.hitBlock(r1138, false);
		assertEquals(b1s.getVelocity(), new Vector(-10, 10));
	}
}
