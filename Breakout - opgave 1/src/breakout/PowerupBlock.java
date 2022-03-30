package breakout;

public class PowerupBlock extends BlockState {

	public PowerupBlock(Rect location) {
		super(location);
	}
	
	@Override
	public void handleCollision(BreakoutState game) {
		game.removeBlock(this);
		game.makeBallSupercharged();
	}
}
