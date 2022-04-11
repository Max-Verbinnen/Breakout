package breakout;

import java.awt.Color;

public class SturdyBlock extends BlockState {
	private int lives = 3;
	private final Color[] COLORS = {Color.LIGHT_GRAY, Color.GRAY, Color.DARK_GRAY};

	public SturdyBlock(Rect location) {
		super(location);
	}
	
	/**
	 * @mutates | this
	 */
	@Override
	public boolean handleCollision(BreakoutState game, Ball ball) {
		lives--;
		if (lives == 0) game.removeBlock(this);
		
		return lives == 0;
	}
	
	@Override
	public Color getColor() {
		return COLORS[lives - 1];
	}

}
