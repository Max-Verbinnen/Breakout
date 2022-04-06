package breakout;

import java.awt.Color;

public class ReplicatorPaddle extends PaddleState {
	private int times;
	
	public ReplicatorPaddle(Point center, int times) {
		super(center);
		this.times = times;
	}
	
	public ReplicatorPaddle createPaddle(Point center) {
		return new ReplicatorPaddle(center, times);
	}
	
	public int handleCollision(BreakoutState game) {		
		if (times - 1 == 0) {
			game.makePaddleNormal();
		}
		
		return times--;
	}
	
	@Override
	public Color getColor() {
		return new Color(0xfa, 0xff, 0x7a);
	}

}
