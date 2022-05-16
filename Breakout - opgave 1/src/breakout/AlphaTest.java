package breakout;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import radioactivity.Alpha;
import utils.Circle;
import utils.Point;
import utils.Rect;
import utils.Vector;

class AlphaTest {
	Circle c1;
	Circle c2;
	Vector v1;
	Vector v2;
	Alpha a1;
	
	@BeforeEach
	void setUp() throws Exception {
		c1 = new Circle(new Point(0, 5), 2);
		c2 = new Circle(new Point(60, 30), 10);
		v1 = new Vector(2, 2);
		v2 = new Vector(2, 5);
		a1 = new Alpha(c1, v1);
	}

	@Test
	void testAlpha() {
		assertEquals(a1.getLocation(), c1);
		assertEquals(a1.getVelocity(), v1);
		
		a1.setLocation(c2);
		assertEquals(a1.getLocation(), c2);
		a1.setVelocity(v2);
		assertEquals(a1.getVelocity(), v2);
	}
	
	@Test
	void testMove() {
		a1.move(new Vector(1, 1));
		assertEquals(c1.getCenter().plus(new Vector(1, 1)), a1.getLocation().getCenter());
		assertEquals(c1.getDiameter(), a1.getLocation().getDiameter());
	}
	
	@Test
	void testClone() {
		Alpha a = a1.clone();
		assertTrue(a != a1);
		assertEquals(a.getLocation(), c1);
		assertEquals(a.getVelocity(), v1);
	}
	
	@Test
	void testGetColor() {
		assertEquals(a1.getColor(), Color.cyan);
	}
	
	@Test
	void testCollidesWith() {
		boolean res = a1.collidesWith(new Rect(new Point(50, 50), new Point(100, 100)));
		assertEquals(res, false);
	}
	
	@Test
	void testHitWall() {
		a1.hitWall(new Rect(new Point(1, 1), new Point(3, 8)));
		assertEquals(a1.getVelocity(), new Vector(-2, 2));
		assertEquals(a1.getLocation(), c1);
	}
	
	@Test
	void hitBounceOn() {
		Vector res = a1.bounceOn(new Rect(new Point(102, 34), new Point(400, 500)));
		assertEquals(res, null);
		Vector res2 = a1.bounceOn(new Rect(new Point(1, 1), new Point(3, 8)));
		assertEquals(res2, new Vector(-2, 2));
	}
	
	@Test
	void testHitPaddle() {
		a1.hitPaddle(new Rect(new Point(1, 1), new Point(3, 8)), new Vector(0, -10));
		assertEquals(a1.getVelocity(), new Vector(-2, 0));
		assertEquals(a1.getLocation(), c1);
	}
	
	@Test
	void equalsContent() {
		assertEquals(true, a1.equalsContent(new Alpha(a1.getLocation(), a1.getVelocity())));
		assertEquals(true, a1.equalsContent(a1));
		assertEquals(false, a1.equalsContent(null));
		assertEquals(false, a1.equalsContent(c1));
		assertEquals(false, a1.equalsContent(new Alpha(a1.getLocation(), a1.getVelocity().plus(new Vector(1, 1)))));
		assertEquals(false, a1.equalsContent(new Alpha(new Circle(a1.getLocation().getCenter(), a1.getLocation().getDiameter() + 1), a1.getVelocity())));
		assertEquals(false, a1.equalsContent(new Alpha(new Circle(new Point(42, 6), a1.getLocation().getDiameter()), a1.getVelocity())));
	}

}
