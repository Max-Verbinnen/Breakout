package breakout;

public class NormalBlock extends BlockState {

	public NormalBlock(Rect location) {
		super(location);
	}
	
	@Override
	public boolean handleCollision(BreakoutState game, Ball ball) {
		game.removeBlock(this);
		return true;
	}
}
