package breakout;

import java.util.Timer;
import java.util.TimerTask;

public class SuperchargedBall extends Ball {
	private int lifetime; // In milliseconds
	private Timer timer;

	public SuperchargedBall(Circle location, Vector velocity, int lifetime, BreakoutState game) {
		super(location, velocity);
		this.lifetime = lifetime;
		timer(game);
	}
	
	private void timer(BreakoutState game) {
		timer = new Timer();
		
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				lifetime -= 1000;
				
				if (lifetime <= 0) {
					timer.cancel();
					game.makeBallNormal(SuperchargedBall.this);
				}
			}
		}, 0, 1000);
	}
	
	@Override
	public SuperchargedBall createBall(Circle location, Vector velocity, BreakoutState game) {
		return new SuperchargedBall(location, velocity, 10000, game);
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

}
