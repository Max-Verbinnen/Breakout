package breakout;

public class ReplicatorPaddle extends PaddleState {
	private int times = 3;
	
	public ReplicatorPaddle(Point center) {
		super(center);
	}
	
	public ReplicatorPaddle createPaddle(Point center) {
		return new ReplicatorPaddle(center);
	}
	
	public int handleCollision(BreakoutState game) {
		if (times == 0) {
			game.makePaddleNormal();
		}
		
		return times--;
	}

}
