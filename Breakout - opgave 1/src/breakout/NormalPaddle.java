package breakout;

public class NormalPaddle extends PaddleState {

	public NormalPaddle(Point center) {
		super(center);
	}
	
	public NormalPaddle createPaddle(Point center) {
		return new NormalPaddle(center);
	}
	
	public int handleCollision(BreakoutState game) {
		return 0;
	}
	
}
