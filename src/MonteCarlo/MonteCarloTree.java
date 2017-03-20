package MonteCarlo;

import Main.State;

import java.util.ArrayList;

import Enum.Direction;

public class MonteCarloTree {
	private State baseState;
	private MonteCarloNode baseNode;
	private boolean allAgentGreedy;
	private final int depthThreshold;
	public MonteCarloTree(State initialState,int threshold, boolean allAgentGreedy) {
		depthThreshold = threshold;
		baseState = initialState.clone();
		baseNode = new MonteCarloNode(baseState, null, 0);
		this.allAgentGreedy = allAgentGreedy;
		if (!allAgentGreedy) {
			throw new IllegalArgumentException("not yet implemented for teammate aware predator");
		}
	}

	public MonteCarloNode getBaseNode() {
		return baseNode;
	}
	
	public void computeMCT(int nbrIteration){
		MonteCarloNode currentNode;
		for(int i=0; i<nbrIteration; i++){
			currentNode = baseNode;
			//the currentNode didn't win or lose
			while(!(currentNode.hasWon()||currentNode.hasLost(depthThreshold))){
				//TODO
				Direction nextDirection = currentNode.computeBestUTC();
				//the next node is the UTC selected child of currentNode
				currentNode = currentNode.computeChild(nextDirection, allAgentGreedy);
			}
			//the currentNode has won
			if(currentNode.hasWon()){
				currentNode.propagateWin();
			}
			//the currentNode has lost
			else{
				currentNode.propagateLose();
			}
		}
	}
	
	public ArrayList<Direction> computePath(int iteration){
		computeMCT(iteration);
		ArrayList<Direction> path = new ArrayList<>();
		//TODO Find the path
		return path;
	}
	
	@Override
	public String toString(){
		return "MontecarloTree: baseNode = "+baseNode.toString();
	}
}
