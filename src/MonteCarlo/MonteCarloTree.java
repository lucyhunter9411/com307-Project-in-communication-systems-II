package MonteCarlo;

import Main.State;
import Enum.Direction;

public class MonteCarloTree {
	private State baseState;
	private MonteCarloNode baseNode;
	public MonteCarloTree(State initialState){
		baseState = initialState.clone();
		baseNode = new MonteCarloNode(baseState);
	}
	
	public MonteCarloNode getBaseNode(){
		return baseNode;
	}
}
