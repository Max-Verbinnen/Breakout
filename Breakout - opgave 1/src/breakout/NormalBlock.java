package breakout;

import java.awt.Color;

public class NormalBlock extends BlockState {

	public NormalBlock(Rect location) {
		super(location);
	}
	
	/**
	 * @inspects | this
	 */
	@Override
	public boolean handleCollision(BreakoutState game, Ball ball) {
		game.removeBlock(this);
		return true;
	}
	
	@Override
	public Color getColor() {
		return new Color(0x80,0x00,0xff);
	}
}
