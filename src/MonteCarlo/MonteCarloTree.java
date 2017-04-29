package MonteCarlo;

import Main.RandomSeededDouble;
import Main.State;

import Actor.Agent;
import Enum.Direction;

public class MonteCarloTree {
	private State baseState;
	private MonteCarloNodeS baseNode;
	private final int depthThreshold;
	private Agent[] agentsList;
	private RandomSeededDouble rand;

	public MonteCarloTree(State initialState, int nbrIteration, int threshold, RandomSeededDouble rand,
			Agent[] agentsList) {
		this.rand = rand;
		depthThreshold = threshold;
		baseState = initialState.clone();
		baseNode = new MonteCarloNodeS(baseState, null, 0, rand);
		this.agentsList = agentsList;
		computeMCT(nbrIteration);
	}

	public MonteCarloNodeS getBaseNode() {
		return baseNode;
	}

	public void computeMCT(int nbrIteration) {
		MonteCarloNodeS currentStateNode;
		for (int i = 0; i < nbrIteration; i++) {
			currentStateNode = baseNode;
			// the currentNode didn't win or lose
			while (!((currentStateNode.hasWon() && currentStateNode.getDepth() != 0)
					|| currentStateNode.hasLost(depthThreshold))) {
				// TODO
				// the next node is the UTC selected child of currentNode
				Direction nextDirection = currentStateNode.computeBestUTC();
				// generate the next NodeG
				MonteCarloNodeG childNodeG = currentStateNode.computeChild(nextDirection);
				// choose the model in function of our probability table
				int childIndex = childNodeG.computeBestUTC();
				// set the good agents in function of our model and put them on
				// the right position
				Agent[] generatedAgents = setModelAgents(childIndex);
				setAgentsOnPosition(currentStateNode.getNodeState(), generatedAgents);
				// if(currentStateNode.getDepth()==0 && i ==0){
				// System.out.println(generatedAgents[0]+""+((TeammateAwarePredator)generatedAgents[0]).attributedPreyNeighbor+"
				// "+generatedAgents[1]+""+((TeammateAwarePredator)generatedAgents[1]).attributedPreyNeighbor+"
				// "+generatedAgents[2]+""+((TeammateAwarePredator)generatedAgents[2]).attributedPreyNeighbor);}
				// generate the next stateNode
				currentStateNode = childNodeG.computeChild(generatedAgents, childIndex);
			}
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

	private Agent[] setModelAgents(int childIndex) {
		Agent[] modelAgentsList = new Agent[3];
		if (childIndex % 2 == 0) {
			modelAgentsList[0] = agentsList[0];
		} else {
			modelAgentsList[0] = agentsList[3];
		}
		childIndex /= 2;
		if (childIndex % 2 == 0) {
			modelAgentsList[1] = agentsList[1];
		} else {
			modelAgentsList[1] = agentsList[4];
		}
		childIndex /= 2;
		if (childIndex % 2 == 0) {
			modelAgentsList[2] = agentsList[2];
		} else {
			modelAgentsList[2] = agentsList[5];
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
}
