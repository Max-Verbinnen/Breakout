package breakout;

public class SturdyBlock extends BlockState {
	private int lives = 3;

	public SturdyBlock(Rect location) {
		super(location);
	}
	
	@Override
	public boolean handleCollision(BreakoutState game, Ball ball) {
		lives--;
		if (lives == 0) game.removeBlock(this);
		
		return lives == 0;
	}
}
