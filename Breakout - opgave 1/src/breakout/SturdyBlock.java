package breakout;

import java.awt.Color;

/**
 * Represents the state of a block in the breakout game.
 *
 * @immutable
 * @invar | getLocation() != null
 * @invar | getLives() >= 0
 */
public class SturdyBlock extends BlockState {
	/**
	 * @invar | lives >= 0
	 */
	private int lives;
	private final Color[] COLORS = {Color.LIGHT_GRAY, Color.GRAY, Color.DARK_GRAY};

	/**
	 * Construct a block occupying a given rectangle in the field.
	 * 
	 * @pre | location != null
	 * 
	 * @post | getLocation().equals(location)
	 * @post | getLives() == i
	 */
	public SturdyBlock(Rect location, int i) {
		super(location);
		lives = i;
	}
	
	/**
	 * Returns the current lives of the block.
	 * 
	 * @inspects | this
	 */
	public int getLives() {
		return lives;
	}
	
	/**
	 * Determine if block needs to be removed, return true if removed.
	 * 
	 * @pre | game != null
	 * @pre | ball != null
	 * 
	 * @mutates | this
	 */
	@Override
	public boolean handleCollision(BreakoutState game, Ball ball) {
		lives--;
		if (lives == 0) game.removeBlock(this);
		
		return lives == 0;
	}
	
	/**
	 * Return the color of the block.
	 * 
	 * @inspects | this
	 * 
	 * @creates | result
	 */
	@Override
	public Color getColor() {
		return COLORS[lives - 1];
	}

}
