package breakout;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

public class SuperchargedBall extends Ball {
	private int lifetime; // In milliseconds

	public SuperchargedBall(Circle location, Vector velocity, int lifetime, BreakoutState game) {
		super(location, velocity);
		this.lifetime = lifetime;
	}
	
	@Override
	public SuperchargedBall copyBall(Ball ball, Vector newVelocity, BreakoutState game) {
		return new SuperchargedBall(ball.getLocation(), newVelocity, ball.getLifetime(), game);
	}

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
		return Color.magenta;
	}
	
	public int getLifetime() {
		return lifetime;
	}
	
	public void updateLifetime(int elapsedTime, BreakoutState game) {
		lifetime -= elapsedTime;
		if (lifetime <= 0) game.makeBallNormal(this);
	}

}
