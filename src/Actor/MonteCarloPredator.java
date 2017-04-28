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
	private Direction previousComputedDirection;
	private Agent[] agentsList;
	
	public MonteCarloPredator(int x, int y, int agentIndex, long randSeed) {
		super(x, y, agentIndex, randSeed);
		type = AgentType.MonteCarlo;
	}

	@Override
	public Direction iterate(State state) {
		bayesAgentsIdentity.newStateInformation(state.clone(),previousComputedDirection);
		// TODO See what bayesAgentsIdentity tells us
		//state.printMapHelper();
		//System.out.println();
		monteCarloTree = new MonteCarloTree(state.clone(), MAX_ITERATION, TREE_THRESHOLD, rand, agentsList);
		//System.out.println(monteCarloTree);
		previousComputedDirection = monteCarloTree.getBaseNode().computeBestDirection();
		//monteCarloTree.getBaseNode().getChild(previousComputedDirection).getChild(7).getNodeState().printMapHelper();
		//System.out.println();
		return previousComputedDirection;
	}

	@Override
	public void initiate(State initialState) {
		bayesAgentsIdentity = new BayesAgentsIdentity(initialState);
		initiateAgentsList(initialState);
	}
	
	private void initiateAgentsList(State initialState) {
		agentsList = new Agent[6];
		for (int i = 0; i < 3; i++) {
			agentsList[i] = (new GreedyPredator(initialState, i + 3, rand.generateLong()));
		}
		for (int i = 0; i < 3; i++) {
			//TODO need to initiate to the REAL baseState, not the baseState of each iteration
			agentsList[i+3] = (new TeammateAwarePredator(initialState, i + 3, rand.generateLong()));
		}
	}
}
