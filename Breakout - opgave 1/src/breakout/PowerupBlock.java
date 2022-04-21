package breakout;

import java.awt.Color;

public class PowerupBlock extends BlockState {

	public PowerupBlock(Rect location) {
		super(location);
	}
	
	@Override
	public boolean handleCollision(BreakoutState game, Ball ball) {
		game.removeBlock(this);
		game.makeBallSupercharged(ball);
		return false; // So that ball bounces when it hits powerup block
	}
	
	@Override
	public Color getColor() {
		return Color.magenta;
	}

}
