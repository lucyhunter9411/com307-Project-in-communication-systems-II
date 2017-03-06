package MonteCarlo;

import Enum.Direction;
import Main.State;

public class MonteCarloNode {

	private State nodeState;
	private MonteCarloNode[] childsNode = new MonteCarloNode[4];
	private double pointsEarned = 0;
	private int nodeTry = 0;
	public MonteCarloNode(State state) {
		nodeState = state.clone();
	}

	public MonteCarloNode getChild(Direction d){
		switch(d){
		case LEFT: return childsNode[0];
		case TOP: return childsNode[1];
		case RIGHT: return childsNode[2];
		case BOTTOM: return childsNode[3];
		//unreachable
		default: return null;
		}
	}
	
	public MonteCarloNode computeChild(Direction d){
		MonteCarloNode currentChild = getChild(d);
		//already computed
		if(currentChild!=null){
			return currentChild;
		}
		else{
			State nextState = nodeState.clone();
			//TODO MODIFY THE STATE TO THE NEXT NODE
			//the problem is the RNG
			//nextState.
			MonteCarloNode newChild = new MonteCarloNode(nextState);
			switch(d){
				case LEFT: childsNode[0] = newChild; break;
				case TOP: childsNode[1] = newChild; break;
				case RIGHT: childsNode[2] = newChild; break;
				case BOTTOM: childsNode[3] = newChild; break;
				//unreachable
				default: break;
			}
			return newChild;
		}
	}
	
	public State getNodeState(){
		return nodeState;
	}
}
