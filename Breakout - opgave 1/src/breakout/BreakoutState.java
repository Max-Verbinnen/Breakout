package breakout;

// TODO: implement, document
public class BreakoutState {

	
	public BreakoutState(BallState[] balls, BlockState[] blocks, Point bottomRight, PaddleState paddle) {
	}
	
	public BallState[] getBalls() {
		return null;
	}

	public BlockState[] getBlocks() {
		return null;
	}

	public PaddleState getPaddle() {
		return null;
	}

	public Point getBottomRight() {
		return null;
	}

	public void tick(int paddleDir) {
	}

	public void movePaddleRight() {
	}

	public void movePaddleLeft() {
	}
	
	public boolean isWon() {
		return false;
	}

	public boolean isDead() {
		return false;
	}
}
