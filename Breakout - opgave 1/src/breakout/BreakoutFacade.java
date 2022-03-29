package breakout;

import java.awt.Color;

//No documentation required for this class
public class BreakoutFacade {
	public PaddleState createNormalPaddleState(Point center) {
		// TODO
		return null;
	}

	public BallState createNormalBall(Point center, int diameter, Vector initBallVelocity) {
		// TODO
		return null;
	}

	public BallState createSuperchargedBall(Point center, int diameter, Vector initBallVelocity, int lifetime) {
		// TODO
		return null;
	}

	public BreakoutState createBreakoutState(BallState[] balls, BlockState[] blocks, Point topRight,
			PaddleState paddle) {
		// TODO
		return null;
	}

	public BlockState createNormalBlockState(Point topLeft, Point bottomRight) {
		// TODO
		return null;
	}

	public BlockState createSturdyBlockState(Point topLeft, Point bottomRight, int i) {
		// TODO
		return null;
	}

	public BlockState createReplicatorBlockState(Point topLeft, Point bottomRight) {
		// TODO
		return null;
	}

	public BlockState createPowerupBallBlockState(Point topLeft, Point bottomRight) {
		// TODO
		return null;
	}

	public Color getColor(PaddleState paddle) {
		// TODO
		return null;
	}

	public Color getColor(BallState ball) {
		// TODO
		return null;
	}

	public Rect getLocation(PaddleState paddle) {
		// TODO
		return null;
	}

	public Point getCenter(BallState ball) {
		// TODO
		return null;
	}

	public int getDiameter(BallState ball) {
		// TODO
		return -1;
	}

	public BallState[] getBalls(BreakoutState breakoutState) {
		// TODO
		return null;
	}

	public Color getColor(BlockState block) {
		// TODO
		return null;
	}

	public Rect getLocation(BlockState block) {
		// TODO
		return null;
	}
}