package breakout;

public class SuperchargedBall extends Ball {
	private int lifetime;

	public SuperchargedBall(Circle location, Vector velocity, int lifetime) {
		super(location, velocity);
		this.lifetime = lifetime;
	}
}
