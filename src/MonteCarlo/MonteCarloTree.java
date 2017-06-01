package MonteCarlo;

import Main.RandomSeededDouble;
import Main.State;

import Actor.Agent;
import Enum.AgentType;
import Enum.Direction;

public class MonteCarloTree {
	private State baseState;
	private MonteCarloNodeS baseNode;
	private final int depthThreshold;
	private Agent[] agentsList;
	private RandomSeededDouble rand;

	public MonteCarloTree(State initialState, int threshold, RandomSeededDouble rand, Agent[] agentsList) {
		this.rand = rand;
		depthThreshold = threshold;
		baseState = initialState.clone();
		baseNode = new MonteCarloNodeS(baseState, null, rand);
		this.agentsList = agentsList;
	}

	public MonteCarloNodeS getBaseNode() {
		return baseNode;
	}

	public void computeMCT(int nbrIteration, BayesAgentsIdentity bayesAgentsIdentity, boolean defaultBayesianMode,
			int modelIndex) {
		MonteCarloNodeS currentStateNode = baseNode;
		int selection = 0;
		if (currentStateNode.hasNoChild()) {
			selection = 1;
		}
		for (int i = 0; i < nbrIteration; i++) {
			currentStateNode = baseNode;
			int currentDepth = 0;
			// the currentNode didn't win or lose
			while (!((currentStateNode.hasWon() && currentDepth != 0) || currentDepth >= depthThreshold)) {
				// choose the model in function of our probability table
				int childIndex;
				if (defaultBayesianMode) {
					childIndex = computeNextModelIndex(bayesAgentsIdentity);
				} else {
					childIndex = modelIndex;
				}
				// set the good agents in function of our model and put them on
				// the right position
				Agent[] generatedAgents = setModelAgents(childIndex);
				// selection mode
				if (selection < 2) {
					// the next node is the UTC selected child of currentNode
					Direction nextDirection = currentStateNode.computeBestUTC();
					// generate the next NodeG
					MonteCarloNodeG childNodeG = currentStateNode.computeChild(nextDirection);
					setAgentsOnPosition(currentStateNode.getNodeState(), generatedAgents);
					// generate the next stateNode
					currentStateNode = childNodeG.computeChild(generatedAgents, childIndex);
					if (selection == 1) {
						selection = 2;
					} else if (selection == 0 && currentStateNode.hasNoChild()) {
						selection = 1;
					}
				}
				// rollout
				else {
					MonteCarloNodeG childNodeG = currentStateNode.computeChildRollout();
					setAgentsOnPosition(currentStateNode.getNodeState(), generatedAgents);
					// generate the next stateNode
					currentStateNode = childNodeG.computeChild(generatedAgents, childIndex);
				}
				currentDepth++;
			}
			selection = 0;
			// the currentNode has won
			if (currentStateNode.hasWon()) {
				currentStateNode.setWinner();
			}
			// the currentNode has lost
			else {
				currentStateNode.setLoser();
			}
		}
	}

	private int computeNextModelIndex(BayesAgentsIdentity bayesAgentsIdentity) {
		int result = 0;
		for (int i = 0; i < 3; i++) {
			int agentIndex = i + 3;
			if (bayesAgentsIdentity.modelProbablityOfAgent(AgentType.TeammateAware, agentIndex) > rand
					.generateDouble()) {
				result += Math.pow(2, i);
			}
		}
		return result;
	}

	private Agent[] setModelAgents(int childIndex) {
		Agent[] modelAgentsList = new Agent[3];
		if (childIndex % 2 == 0) {
			modelAgentsList[0] = agentsList[0];
		} else {
			modelAgentsList[0] = agentsList[3];
			childIndex--;
		}
		childIndex /= 2;
		if (childIndex % 2 == 0) {
			modelAgentsList[1] = agentsList[1];
		} else {
			modelAgentsList[1] = agentsList[4];
			childIndex--;
		}
		childIndex /= 2;
		if (childIndex % 2 == 0) {
			modelAgentsList[2] = agentsList[2];
		} else {
			modelAgentsList[2] = agentsList[5];
			childIndex--;
		}
		return modelAgentsList;
	}

	private void setAgentsOnPosition(State nodeState, Agent[] generatedAgents) {
		int[][] CoordList = nodeState.getAgentsCoordinateList();
		for (int i = 0; i < generatedAgents.length; i++) {
			generatedAgents[i].setPos(CoordList[i + 2][0], CoordList[i + 2][1]);
		}

	}

	@Override
	public String toString() {
		String result = "MontecarloTree: baseNode = " + baseNode + "\n";
		for (Direction d : Direction.values()) {
			if (baseNode.getChild(d) != null) {
				result = result + "	=> D:" + d + " " + baseNode.getChild(d);
			}
		}
		return result;
	}

	public void chooseNextParent(Direction previousComputedDirection, int indexToChild) {
		baseNode = (MonteCarloNodeS) ((MonteCarloNodeG) baseNode.getChild(previousComputedDirection))
				.getChild(indexToChild);
		baseNode.setAsNewParent();
	}
}
