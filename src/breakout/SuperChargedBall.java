package breakout;

import java.awt.Color;
//import java.util.Arrays;

import breakout.radioactivity.Ball;
import breakout.utils.Circle;
import breakout.utils.Rect;
import breakout.utils.Vector;

public class SuperChargedBall extends NormalBall {

	private int lifetime;
	private static Color COLOR = Color.red;

	public SuperChargedBall(Circle location, Vector velocity, int lifetime) {
		super(location, velocity);
		this.lifetime = lifetime;
	}

	/**
	 * Update the BallState after hitting a block at a given location.
	 * 
	 * @pre | rect != null
	 * @pre | collidesWith(rect)
	 * @post | getLocation().equals(old(getLocation()))
	 * @post | (getLifetime() < 0 || !destroyed) || getVelocity().equals(old(getVelocity()))
	 * @mutates this
	 */
	@Override
	public void hitBlock(Rect rect, boolean destroyed) {
		if(lifetime < 0 || !destroyed) {
			super.hitBlock(rect, destroyed);
		}
	}

	@Override
	public void hitPaddle(Rect loc, Vector paddleVel) {
		super.hitPaddle(loc, paddleVel);
	}

	@Override
	public void hitWall(Rect rect) {
		super.hitWall(rect);
	}

	@Override
	public Color getColor() {
		if (lifetime >= 0) {
			float hue = (float) ((getLifetime() % 2500) / 2500.0);
			int rgb = Color.HSBtoRGB(hue, 1, 1);
			return new Color(rgb);
		} else {
			return super.getColor();
		}
	}

	public int getLifetime() {
		return lifetime;
	}
	
	@Override
	public void move(Vector v, int elapsedTime) {
		if(lifetime >= 0) {
			lifetime -= elapsedTime;
		}
		setLocation(new Circle(getLocation().getCenter().plus(v), getLocation().getDiameter()));
	}

	@Override
	public Ball cloneWithVelocity(Vector v) {
		return new SuperChargedBall(getLocation(), v, lifetime);
	}

}
