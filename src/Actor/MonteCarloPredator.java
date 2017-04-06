package Actor;

import Enum.AgentType;
import Enum.Direction;
import Main.State;
import MonteCarlo.BayesAgentsIdentity;
import MonteCarlo.MonteCarloTree;

public class MonteCarloPredator extends Agent {
	MonteCarloTree monteCarloTree;
	boolean allOtherAgentsGreedy = true;
	private final int MAX_ITERATION = 2000;
	private final int TREE_THRESHOLD = 10;
	private BayesAgentsIdentity bayesAgentsIdentity;
	private Agent[] generatedAgents;
	private Direction previousComputedDirection;

	public MonteCarloPredator(int x, int y, int agentIndex, long randSeed) {
		super(x, y, agentIndex, randSeed);
		type = AgentType.MonteCarlo;
	}

	@Override
	public Direction iterate(State state) {
		bayesAgentsIdentity.newStateInformation(state.clone(),previousComputedDirection);
		// TODO See what bayesAgentsIdentity tells us
		// right now it's 100% sure it's greedy
		monteCarloTree = new MonteCarloTree(state.clone(), generatedAgents, MAX_ITERATION, TREE_THRESHOLD, rand);
		//System.out.println(monteCarloTree);
		previousComputedDirection = monteCarloTree.getBaseNode().computeBestDirection();
		//monteCarloTree.getBaseNode().getChild(resultDirection).getNodeState().printMapHelper();
		return previousComputedDirection;
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
