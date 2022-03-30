package breakout;

public class SturdyBlock extends BlockState {
	private int lives = 3;

	public SturdyBlock(Rect location) {
		super(location);
	}
	
	@Override
	public void handleCollision(BreakoutState game) {
		lives--;
		if (lives == 0) game.removeBlock(this);
	}
}
