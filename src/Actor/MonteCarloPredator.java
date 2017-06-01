package Actor;

import Enum.AgentType;
import Enum.Direction;
import Main.State;
import MonteCarlo.*;

//extension of an agent, the Monte Carlo predator create a tree of possible future outcome and compute the best probabilistic move in function of the tree
public class MonteCarloPredator extends Agent {
	MonteCarloTree monteCarloTree;
	private final int MAX_ITERATION;
	private final int TREE_THRESHOLD;
	private BayesAgentsIdentity bayesAgentsIdentity;
	private Direction previousComputedDirection;
	private Agent[] agentsList;
	private boolean defaultBayesianMode;
	private boolean debug = false;
	private int modelIndex;

	public MonteCarloPredator(int x, int y, int agentIndex, long randSeed, boolean defaultBayesianMode, int modelIndex,
			int maxIteration, int depthThreshold) {
		super(x, y, agentIndex, randSeed);
		type = AgentType.MonteCarlo;
		// if false it means we know perfectly the models of the others agent,
		// so we don't need to compute the bayesAgentsIdentity
		// if true, each model is equiprobable, so we need to get information
		// from states
		this.defaultBayesianMode = defaultBayesianMode;
		this.modelIndex = modelIndex;
		MAX_ITERATION = maxIteration;
		TREE_THRESHOLD = depthThreshold;

	}

	@Override
	public Direction iterate(State state) {
		// update the table of model probability of the agents
		if (defaultBayesianMode) {
			bayesAgentsIdentity.newStateInformation(state.clone(), previousComputedDirection);
		}
		// update the MCT if some nodes are still useful, or create a new tree
		monteCarloTree = updateMCT(state);
		monteCarloTree.computeMCT(MAX_ITERATION, bayesAgentsIdentity, defaultBayesianMode, modelIndex);
		if (debug) {
			System.out.println(monteCarloTree);
		}
		previousComputedDirection = monteCarloTree.getBaseNode().computeBestDirection();
		if (debug) {
			// ((MonteCarloNodeG)
			// monteCarloTree.getBaseNode().getChild(previousComputedDirection)).getChild(0).getNodeState().printMapHelper();
			state.printMapHelper();
			System.out.println();
		}
		return previousComputedDirection;
	}

	// update the tree in function of the current state
	private MonteCarloTree updateMCT(State state) {
		if (previousComputedDirection != null) {
			// The head of the tree is the new node
			MonteCarloNodeG gNode = (MonteCarloNodeG) monteCarloTree.getBaseNode().getChild(previousComputedDirection);
			MonteCarloNode[] childsList = gNode.getChilds();
			MonteCarloNode sNode;
			int indexToChild = -1;
			int nodeTryMax = 0;
			// choose the optimal child similar to the current state
			for (int i = 0; i < childsList.length; i++) {
				sNode = childsList[i];
				if (sNode != null && state.toLong() == ((MonteCarloNodeS) sNode).getNodeState().toLong()) {
					int nodeTryValue = sNode.getNodeTry();
					if (nodeTryValue > nodeTryMax) {
						indexToChild = i;
						nodeTryMax = nodeTryValue;
					}
				}
			}
			// no childs computed are actually computed the real state
			if (indexToChild == -1) {
				return new MonteCarloTree(state.clone(), TREE_THRESHOLD, rand, agentsList);
			}
			// one child is the actual state, so we can use him as the startNode
			// of our new tree
			else {
				monteCarloTree.chooseNextParent(previousComputedDirection, indexToChild);
				return monteCarloTree;
			}
		} else {
			return new MonteCarloTree(state.clone(), TREE_THRESHOLD, rand, agentsList);
		}
	}

	@Override
	public void initiate(State initialState) {
		// create the object containing the probability of each model on each
		// agent
		bayesAgentsIdentity = new BayesAgentsIdentity(initialState);
		// save fake agents into the base state, we will use them to generate
		// the future State in the MonteCarlo Tree
		initiateAgentsList(initialState);
	}

	// initiate the agents we will use for simulating future state in function
	// of their probability table
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
