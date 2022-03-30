package breakout;

public class ReplicationBlock extends BlockState {

	public ReplicationBlock(Rect location) {
		super(location);
	}
	
	@Override
	public void handleCollision(BreakoutState game) {
		game.removeBlock(this);
		game.makePaddleReplicative();
	}
}
