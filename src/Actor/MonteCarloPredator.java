package Actor;

import Enum.AgentType;
import Enum.Direction;
import Main.State;
import MonteCarlo.*;

public class MonteCarloPredator extends Agent {
	MonteCarloTree monteCarloTree;
	boolean allOtherAgentsGreedy = true;
	private final int MAX_ITERATION = 2000;
	private final int TREE_THRESHOLD = 10;
	private BayesAgentsIdentity bayesAgentsIdentity;
	private Direction previousComputedDirection;
	private Agent[] agentsList;

	public MonteCarloPredator(int x, int y, int agentIndex, long randSeed) {
		super(x, y, agentIndex, randSeed);
		type = AgentType.MonteCarlo;
	}

	@Override
	public Direction iterate(State state) {
		bayesAgentsIdentity.newStateInformation(state.clone(), previousComputedDirection);
		// update the MCT if some nodes are still useful, or create a new tree
		monteCarloTree = updateMCT(state);
		// state.printMapHelper();
		// System.out.println();
		monteCarloTree.computeMCT(MAX_ITERATION, bayesAgentsIdentity);
		// System.out.println(monteCarloTree);
		previousComputedDirection = monteCarloTree.getBaseNode().computeBestDirection();
		// ((MonteCarloNodeG)
		// monteCarloTree.getBaseNode().getChild(previousComputedDirection)).getChild(0).getNodeState().printMapHelper();
		// System.out.println();
		return previousComputedDirection;
	}

	private MonteCarloTree updateMCT(State state) {
		// TODO
		if (previousComputedDirection != null) {
			// The head of the tree is the new node and we change the depth of
			// the nodes
			MonteCarloNodeG aNode = (MonteCarloNodeG) monteCarloTree.getBaseNode().getChild(previousComputedDirection);
			return new MonteCarloTree(state.clone(), TREE_THRESHOLD, rand, agentsList);
		} else {
			return new MonteCarloTree(state.clone(), TREE_THRESHOLD, rand, agentsList);
		}
	}

	@Override
	public void initiate(State initialState) {
		bayesAgentsIdentity = new BayesAgentsIdentity(initialState);
		// save fake agents into the base state, we will use them to generate
		// the future State in the MonteCarlo Tree
		initiateAgentsList(initialState);
	}

	private void initiateAgentsList(State initialState) {
		agentsList = new Agent[6];
		for (int i = 0; i < 3; i++) {
			agentsList[i] = (new GreedyPredator(initialState, i + 3, rand.generateLong()));
		}
		for (int i = 0; i < 3; i++) {
			agentsList[i + 3] = (new TeammateAwarePredator(initialState, i + 3, rand.generateLong()));
		}
	}
}
