package MonteCarlo;

import java.util.ArrayList;
import Actor.Agent;
import Actor.GreedyPredator;
import Actor.Prey;
import Enum.Direction;
import Main.RandomSeededDouble;
import Main.State;

public class MonteCarloNode {

	private State nodeState;
	private MonteCarloNode parentNode;
	private final int depth;
	private MonteCarloNode[] childsNode = new MonteCarloNode[4];
	private double pointsEarned = 0;
	private int nodeTry = 0;
	private RandomSeededDouble rand;
	public MonteCarloNode(State state, MonteCarloNode parentNode, int depth, RandomSeededDouble rand) {
		nodeState = state.clone();
		this.depth = depth;
		this.parentNode = parentNode;
		this.rand = rand;
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

	public MonteCarloNode computeChild(Agent[] generatedAgents, Direction d) {
		MonteCarloNode currentChild = getChild(d);
		// already computed
		if (currentChild != null) {
			return currentChild;
		} else {
			State nextState = nodeState.clone();
			// TODO MODIFY THE STATE TO THE NEXT NODE
			ArrayList<Direction> directionOfAgents = new ArrayList<Direction>();
			ArrayList<Agent> agents = new ArrayList<Agent>();
			//compute a random object which the seed is the previous state
			//so the prey will do the same action for the 4 child of the same parent
			
			directionOfAgents.add(Direction.values()[(int) (new RandomSeededDouble(nodeState.toLong()).generateDouble() * 4)]);
			agents.add(new Prey(nextState, 1, 0));
			// value for the MTC agent
			directionOfAgents.add(d);
			// we add a useless agent
			agents.add(new GreedyPredator(nextState, 2, 0));
			for (Agent a : generatedAgents) {
				directionOfAgents.add(a.iterate(nodeState));
				agents.add(a);
			}
			nextState.modifyState(directionOfAgents, agents);
			MonteCarloNode newChild = new MonteCarloNode(nextState, this, depth + 1, rand);
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
			double t = nodeTry;
			if (t == 0) {
				t = 1;
			}
			double wi=1;
			double ni=1;
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
		// System.out.println(bestDirection + " " + maxUTCValue+"
		// "+bestDirection.get(index));
		return bestDirection.get(index);
	}

	@Override
	public String toString() {
		return "Node[w:" + pointsEarned + " t:" + nodeTry + " depth:" + depth + "]";
	}

	//return the direction with the best expected value
	public Direction computeBestDirection() {
		double maxExpValue = -Double.MAX_VALUE;
		ArrayList<Direction> bestDirection = new ArrayList<>();
		for (Direction d : Direction.values()) {
			double wi=1;
			double ni=1;
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
