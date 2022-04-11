package breakout;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

public class SuperchargedBall extends Ball {
	private int lifetime; // In milliseconds

	public SuperchargedBall(Circle location, Vector velocity, int lifetime) {
		super(location, velocity);
		this.lifetime = lifetime;
	}
	
	@Override
	public SuperchargedBall copyBall(Ball ball, Vector newVelocity) {
		return new SuperchargedBall(ball.getLocation(), newVelocity, ball.getLifetime());
	}

	/**
	 * @post | (destroyed) ? getVelocity().equals(old(getVelocity())) : getVelocity().equals(old(bounceOn(rect)))
	 */
	@Override
	public void hitBlock(Rect rect, boolean destroyed) {
		if (destroyed) {
			// Ball should maintain its velocity
		} else {
			Vector newVelocity = bounceOn(rect);
			setVelocity(newVelocity);
		}
	}
	
	@Override
	public Color getColor() {
		float hue = (float) ((getLifetime() % 2500) / 2500.0);
		int rgb = Color.HSBtoRGB(hue, 1, 1);
		return new Color(rgb);
	}
	
	public int getLifetime() {
		return lifetime;
	}
	
	public void updateLifetime(int elapsedTime, BreakoutState game) {
		lifetime -= elapsedTime;
		if (lifetime <= 0) game.makeBallNormal(this);
	}

	@Override
	public Ball clone() {
		return new SuperchargedBall(getLocation(), getVelocity(), lifetime);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof SuperchargedBall otherBall) {
			if (this.getLocation().equals(otherBall.getLocation()) && this.getVelocity().equals(otherBall.getVelocity()) && this.getLifetime() == otherBall.getLifetime()) {
				return true;
			}
		}
		return false;
	}

}
