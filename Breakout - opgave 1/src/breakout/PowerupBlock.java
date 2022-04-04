package breakout;

public class PowerupBlock extends BlockState {

	public PowerupBlock(Rect location) {
		super(location);
	}
	
	@Override
	public boolean handleCollision(BreakoutState game, Ball ball) {
		game.removeBlock(this);
		game.makeBallSupercharged(ball);
		return true;
	}
}
