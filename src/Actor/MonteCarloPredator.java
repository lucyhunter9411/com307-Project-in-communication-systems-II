package Actor;

import Enum.Direction;
import Main.State;
import MonteCarlo.BayesAgentsIdentity;
import MonteCarlo.MonteCarloTree;

public class MonteCarloPredator extends Agent {
	MonteCarloTree monteCarloTree;
	boolean allOtherAgentsGreedy = true;
	private final int MAX_ITERATION = 300;
	private final int TREE_THRESHOLD = 20;
	private BayesAgentsIdentity bayesAgentsIdentity;
	private Agent[] generatedAgents;

	public MonteCarloPredator(int x, int y, int agentIndex, long randSeed) {
		super(x, y, agentIndex, randSeed);
	}

	@Override
	public Direction iterate(State state) {
		bayesAgentsIdentity.newStateInformation(state);
		// TODO See what bayesAgentsIdentity tells us
		// right now it's 100% sure it's greedy
		monteCarloTree = new MonteCarloTree(state, generatedAgents, MAX_ITERATION, TREE_THRESHOLD, rand);
		//System.out.println(monteCarloTree);
		Direction resultDirection = monteCarloTree.getBaseNode().computeBestUTC();
		return resultDirection;
	}

	@Override
	public void initiate(State initialState) {
		bayesAgentsIdentity = new BayesAgentsIdentity(initialState);
		generatedAgents = createPredator(initialState, initialState.getNbrAgents() - 2);
	}

	private Agent[] createPredator(State state, int nbrOtherPredator) {
		if (nbrOtherPredator < 0) {
			throw new IllegalArgumentException(
					"must be a positive value (i.e: there must be at least a prey and this MCPredator, so should be at least 0)");
		}
		Agent[] generatedPredatorList = new Agent[nbrOtherPredator];
		for (int i = 0; i < nbrOtherPredator; i++) {
			generatedPredatorList[i] = (new GreedyPredator(state, i + 3, rand.generateLong()));
		}
		return generatedPredatorList;
	}

}
