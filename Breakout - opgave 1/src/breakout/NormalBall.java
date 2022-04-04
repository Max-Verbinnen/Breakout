package breakout;

public class NormalBall extends Ball {

	public NormalBall(Circle location, Vector velocity) {
		super(location, velocity);
	}
	
	@Override
	public NormalBall createBall(Circle location, Vector velocity, BreakoutState game) {
		return new NormalBall(location, velocity);
	}

	@Override
	public void hitBlock(Rect rect, boolean destroyed) {
		Vector newVelocity = bounceOn(rect);
		setVelocity(newVelocity);
	}

}
