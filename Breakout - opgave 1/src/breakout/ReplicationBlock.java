package breakout;

public class ReplicationBlock extends BlockState {

	public ReplicationBlock(Rect location) {
		super(location);
	}
	
	@Override
	public boolean handleCollision(BreakoutState game, Ball ball) {
		game.removeBlock(this);
		game.makePaddleReplicative();
		return true;
	}
}
