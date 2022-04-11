package breakout;

import java.awt.Color;

public class NormalPaddle extends PaddleState {

	public NormalPaddle(Point center) {
		super(center);
	}
	
	public NormalPaddle createPaddle(Point center) {
		return new NormalPaddle(center);
	}
	
	
	/**
	 * @post | result == 0
	 */
	public int handleCollision(BreakoutState game) {
		return 0;
	}
	
	@Override
	public Color getColor() {
		return new Color(0x99,0xff,0xff);
	}
	
}
