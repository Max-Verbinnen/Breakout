package breakout;

import java.awt.Color;

public class ReplicationBlock extends BlockState {

	public ReplicationBlock(Rect location) {
		super(location);
	}
	
	@Override
	public boolean handleCollision(BreakoutState game, Ball ball) {
		game.removeBlock(this);
		game.makePaddleReplicative();
		return true;
	}
	
	@Override
	public Color getColor() {
		return new Color(0xfa, 0xff, 0x7a);
	}

}
