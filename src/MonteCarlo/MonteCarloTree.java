package MonteCarlo;

import Main.RandomSeededDouble;
import Main.State;

import Actor.Agent;
import Enum.Direction;

public class MonteCarloTree {
	private State baseState;
	private MonteCarloNode baseNode;
	private final int depthThreshold;

	public MonteCarloTree(State initialState, Agent[] generatedAgents, int nbrIteration, int threshold, RandomSeededDouble rand) {
		depthThreshold = threshold;
		baseState = initialState.clone();
		baseNode = new MonteCarloNode(baseState, null, 0, rand);
		computeMCT(nbrIteration, generatedAgents);
	}

	public MonteCarloNode getBaseNode() {
		return baseNode;
	}

	public void computeMCT(int nbrIteration, Agent[] generatedAgents) {
		MonteCarloNode currentNode;
		for (int i = 0; i < nbrIteration; i++) {
			currentNode = baseNode;
			// the currentNode didn't win or lose
			while (!(currentNode.hasWon() || currentNode.hasLost(depthThreshold))) {
				// TODO
				Direction nextDirection = currentNode.computeBestUTC();
				// the next node is the UTC selected child of currentNode
				// TODO change when agent are not known
				setAgentsOnPosition(currentNode.getNodeState(), generatedAgents);
				currentNode = currentNode.computeChild(generatedAgents, nextDirection);
			}
			// the currentNode has won
			if (currentNode.hasWon()) {
				currentNode.propagateWin();
			}
			// the currentNode has lost
			else {
				currentNode.propagateLose();
			}
		}
	}

	private void setAgentsOnPosition(State nodeState, Agent[] generatedAgents) {
		int[][] CoordList = nodeState.getAgentsCoordinateList();
		for (int i = 0; i < generatedAgents.length; i++) {
			generatedAgents[i].setPos(CoordList[i + 2][0], CoordList[i + 2][1]);
		}

	}

	@Override
	public String toString() {
		return "MontecarloTree: baseNode = " + baseNode.toString();
	}
}
