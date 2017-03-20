package MonteCarlo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Enum.Direction;
import Main.State;

public class MonteCarloNode {

	private State nodeState;
	private MonteCarloNode parentNode;
	private final int depth;
	private MonteCarloNode[] childsNode = new MonteCarloNode[4];
	private double pointsEarned = 0;
	private int nodeTry = 0;

	public MonteCarloNode(State state, MonteCarloNode parentNode, int depth) {
		nodeState = state.clone();
		this.depth = depth;
		this.parentNode = parentNode;
	}

	public MonteCarloNode getChild(Direction d) {
		switch (d) {
		case LEFT:
			return childsNode[0];
		case TOP:
			return childsNode[1];
		case RIGHT:
			return childsNode[2];
		case BOTTOM:
			return childsNode[3];
			// unreachable
		default:
			return null;
		}
	}

	public MonteCarloNode computeChild(Direction d, boolean areAllAgentGreedy) {
		MonteCarloNode currentChild = getChild(d);
		// already computed
		if (currentChild != null) {
			return currentChild;
		} else {
			State nextState = nodeState.clone();
			// TODO MODIFY THE STATE TO THE NEXT NODE
			nextState.computeNextMTCNodeState(d);
			MonteCarloNode newChild = new MonteCarloNode(nextState, this, depth + 1);
			switch (d) {
			case LEFT:
				childsNode[0] = newChild;
				break;
			case TOP:
				childsNode[1] = newChild;
				break;
			case RIGHT:
				childsNode[2] = newChild;
				break;
			case BOTTOM:
				childsNode[3] = newChild;
				break;
				// unreachable
			default:
				break;
			}
			return newChild;
		}
	}

	public State getNodeState() {
		return nodeState;
	}

	public boolean hasWon() {
		return nodeState.isPreyCaptured();
	}

	public boolean hasLost(int depthThreshold) {
		return depth >= depthThreshold;
	}

	public void propagateWin() {
		nodeTry++;
		pointsEarned++;
		if (parentNode != null) {
			parentNode.propagateWin();
		}
	}

	public void propagateLose() {
		nodeTry++;
		pointsEarned--;
		if (parentNode != null) {
			parentNode.propagateLose();
		}
	}

	public Direction computeBestUTC() {
		double maxUTCValue = -Double.MAX_VALUE;
		ArrayList<Direction> bestDirection = new ArrayList<>();
		for (Direction d : Direction.values()) {
			// TODO Whatsupp with null?
			double wi = pointsEarned;
			double ni = nodeTry;
			if(ni == 0){
				ni=1;
			}
			double t = 1;
			if (getChild(d) != null) {
				t = getChild(d).nodeTry;
			}
			double UTCValue = wi / ni + Math.sqrt(2 * Math.log(t) / ni);
			if(UTCValue > maxUTCValue){
				bestDirection.clear();
				bestDirection.add(d);
				maxUTCValue = UTCValue;
			}
			else if(UTCValue == maxUTCValue){
				bestDirection.add(d);
			}
		}
		//return the direction with the bigger UTC
		int index = (int) (Math.random()*bestDirection.size());
		//System.out.println(bestDirection + " " + maxUTCValue+" "+bestDirection.get(index));
		return bestDirection.get(index);
	}

	@Override
	public String toString() {
		String childNodeString = "";
		for (Direction d : Direction.values()) {
			if (getChild(d) != null) {
				childNodeString = childNodeString + "\n	=> D:"+ d +" "+ getChild(d).toString();
			}
		}
		return "Node[w:" + pointsEarned + " t:" + nodeTry + " depth:" + depth + "]" + childNodeString;
	}
}
