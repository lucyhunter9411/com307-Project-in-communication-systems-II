package Actor;

import Enum.Direction;
import Main.State;
import MonteCarlo.MonteCarloTree;

public class MonteCarloPredator extends Agent {
	MonteCarloTree monteCarloTree;
	boolean allOtherAgentsGreedy = true;
	private final int MAX_ITERATION = 200;
	private final int TREE_THRESHOLD = 12;
	public MonteCarloPredator(int x, int y, int agentIndex, long randSeed) {
		super(x, y, agentIndex, randSeed);
	}

	@Override
	public Direction iterate(State state) {
		System.out.println(monteCarloTree);
		return Direction.LEFT;
	}

	@Override
	public void initiate(State initialState) {
		monteCarloTree = new MonteCarloTree(initialState, TREE_THRESHOLD, allOtherAgentsGreedy);
		monteCarloTree.computePath(MAX_ITERATION);
	}

}
