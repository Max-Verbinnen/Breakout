package breakout;

public class NormalBlock extends BlockState {

	public NormalBlock(Rect location) {
		super(location);
	}
	
	@Override
	public void handleCollision(BreakoutState game) {
		game.removeBlock(this);
	}
}
