package breakout;

import java.awt.Color;

public class NormalBall extends Ball {

	public NormalBall(Circle location, Vector velocity) {
		super(location, velocity);
	}
	
	@Override
	public NormalBall copyBall(Ball ball, Vector newVelocity, BreakoutState game) {
		return new NormalBall(ball.getLocation(), newVelocity);
	}

	@Override
	public void hitBlock(Rect rect, boolean destroyed) {
		Vector newVelocity = bounceOn(rect);
		setVelocity(newVelocity);
	}
	
	@Override
	public Color getColor() {
		return Color.yellow;
	}

}
