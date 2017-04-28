package MonteCarlo;

import java.util.ArrayList;
import Enum.Direction;
import Main.RandomSeededDouble;
import Main.State;

public class MonteCarloNodeS extends MonteCarloNode{

	private State nodeState;
	private final int depth;
	private MonteCarloNodeG[] childsNode = new MonteCarloNodeG[4];
	private RandomSeededDouble rand;

	public MonteCarloNodeS(State state, MonteCarloNodeG monteCarloNodeG, int depth, RandomSeededDouble rand) {
		super(monteCarloNodeG,depth);
		nodeState = state.clone();
		this.depth = depth;
		this.rand = rand;
	}

	public MonteCarloNodeG getChild(Direction d) {
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

	public MonteCarloNodeG computeChild(Direction d) {
		MonteCarloNodeG currentChild = getChild(d);
		// already computed
		if (currentChild != null) {
			return currentChild;
		} else {
			MonteCarloNodeG newChild = new MonteCarloNodeG(this, d , depth , rand);
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

	public Direction computeBestUTC() {
		double maxUTCValue = -Double.MAX_VALUE;
		ArrayList<Direction> bestDirection = new ArrayList<>();
		for (Direction d : Direction.values()) {
			// TODO Whatsupp with null?
			double t = nodeTry;
			if (t == 0) {
				t = 1;
			}
			double wi = 1;
			double ni = 1;
			if (getChild(d) != null) {
				wi = getChild(d).pointsEarned;
				ni = getChild(d).nodeTry;
			}
			double UTCValue = wi / ni + Math.sqrt(2 * Math.log(t) / ni);
			if (UTCValue > maxUTCValue) {
				bestDirection.clear();
				bestDirection.add(d);
				maxUTCValue = UTCValue;
			} else if (UTCValue == maxUTCValue) {
				bestDirection.add(d);
			}
		}
		// return the direction with the bigger UTC
		int index = (int) (rand.generateDouble() * bestDirection.size());
		//System.out.println(bestDirection + " " + maxUTCValue+" "+bestDirection.get(index));
		return bestDirection.get(index);
	}

	// return the direction with the best expected value
	public Direction computeBestDirection() {
		double maxExpValue = -Double.MAX_VALUE;
		ArrayList<Direction> bestDirection = new ArrayList<>();
		for (Direction d : Direction.values()) {
			double wi = 1;
			double ni = 1;
			if (getChild(d) != null) {
				wi = getChild(d).pointsEarned;
				ni = getChild(d).nodeTry;
			}
			double ExpValue = wi / ni;
			if (ExpValue > maxExpValue) {
				bestDirection.clear();
				bestDirection.add(d);
				maxExpValue = ExpValue;
			} else if (ExpValue == maxExpValue) {
				bestDirection.add(d);
			}
		}
		// return the direction with the bigger Expected Value
		int index = (int) (rand.generateDouble() * bestDirection.size());
		return bestDirection.get(index);
	}
}
