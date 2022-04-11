package breakout;

import java.awt.Color;

public class NormalBall extends Ball {

	public NormalBall(Circle location, Vector velocity) {
		super(location, velocity);
	}
	
	@Override
	public NormalBall copyBall(Ball ball, Vector newVelocity) {
		return new NormalBall(ball.getLocation(), newVelocity);
	}

	/**
	 * @post | getVelocity().equals(old(bounceOn(rect)))
	 */
	@Override
	public void hitBlock(Rect rect, boolean destroyed) {
		Vector newVelocity = bounceOn(rect);
		setVelocity(newVelocity);
	}
	
	@Override
	public Color getColor() {
		return Color.yellow;
	}

	@Override
	public Ball clone() {
		return new NormalBall(getLocation(), getVelocity());
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof NormalBall otherBall) {
			if (this.getLocation().equals(otherBall.getLocation()) && this.getVelocity().equals(otherBall.getVelocity())) {
				return true;
			}
		}
		return false;
	}

}
