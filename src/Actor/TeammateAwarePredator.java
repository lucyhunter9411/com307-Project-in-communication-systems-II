package Actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.IntStream;

import Enum.Direction;
import Enum.AgentType;
import Main.State;

public class TeammateAwarePredator extends Agent {

	private Direction attribuedPreyNeighbor;
	public final AgentType predatorType = AgentType.TeammateAware; 

	public TeammateAwarePredator(int x, int y, int agentIndex, long randSeed) {
		super(x, y, agentIndex, randSeed);
		type = AgentType.TeammateAware;
	}

	@Override
	// in the case of a teammate aware predator, we precompute the
	// destination(prey's neighbor cell) attributed for this predator
	public void initiate(State initialState) {
		// TODO the number of predator is 4 atm for this algorithm, but state
		// can have a bigger number
		if (initialState.getNbrAgents() - 1 != 4) {
			throw new IllegalStateException("This agent work only in team of 4 predator");
		}
		// Calculate the distance from each predator to each cell neighboring
		// the prey.
		ArrayList<ArrayList<Integer>> distancePredatorsNeighbors = computePredatorDistanceOrder(initialState);
		// System.out.println(distancePredatorsNeighbors);
		// Order the predators based on worst shortest distance to a cell
		// neighboring the prey.
		int[] orderedWorstPredator = computeWorstPredator(distancePredatorsNeighbors, 4);

		/*
		 * In order, the predators are assigned the unchosen destination that is
		 * closest to them (without communication), breaking ties by a mutually
		 * known ordering of the predators.
		 */
		Direction[] directionPerPredator = computeDirectionPerPredator(distancePredatorsNeighbors,
				orderedWorstPredator);
		// System.out.println(directionPerPredator[0]+"
		// "+directionPerPredator[1]+" "+directionPerPredator[2]+"
		// "+directionPerPredator[3]+" ");

		assert (agentIndex > 1 && agentIndex <= 5);
		attribuedPreyNeighbor = directionPerPredator[agentIndex - 2];
	}

	@Override
	public Direction iterate(State state) {
		/*
		 * If the predator is already at the destination, try to move onto the
		 * prey so that if it moves, the predator will follow.
		 */
		int preyX = state.getPreyPosX();
		int preyY = state.getPreyPosY();
		int width = state.getMapWidth();
		int height = state.getMapHeight();
		int destinationX = preyX;
		int destinationY = preyY;
		// compute the destination coordinate
		// check if the predator is already on the destination
		switch (attribuedPreyNeighbor) {
		case LEFT:
			destinationX = (preyX - 1 + width) % width;
			if (posX == destinationX && posY == destinationY) {
				return Direction.RIGHT;
			}
			break;
		case TOP:
			destinationY = (preyY - 1 + height) % height;
			if (posX == destinationX && posY == destinationY) {
				return Direction.BOTTOM;
			}
			;
			break;
		case RIGHT:
			destinationX = (preyX + 1) % width;
			if (posX == destinationX && posY == destinationY) {
				return Direction.LEFT;
			}
			;
			break;
		case BOTTOM:
			destinationY = (preyY + 1) % height;
			if (posX == destinationX && posY == destinationY) {
				return Direction.TOP;
			}
			;
			break;
		default:
			break;
		}

		// Otherwise, use A* path planning to select a path, treating other
		// agents as static obstacles.
		return getNextAStarPathStep(state, destinationX, destinationY);
	}

	/*
	 * Compute the minimum distance of each predator with each neighboring cell
	 * of the prey
	 * 
	 * @param state, the current state
	 * 
	 * @return a table (NbrPredator x 4) containing all the minimum distance
	 * computed
	 */
	private ArrayList<ArrayList<Integer>> computePredatorDistanceOrder(State state) {
		int nbrPredator = state.getNbrAgents() - 1;
		ArrayList<ArrayList<Integer>> result = new ArrayList<>();
		Direction[] listDirections = Direction.values();
		for (int j = 0; j < nbrPredator; j++) {

			int predatorAgentIndex = j + 2;
			ArrayList<Integer> line = new ArrayList<>();
			for (int i = 0; i < listDirections.length; i++) {
				// the direction is applied on the second agent
				line.add(state.getDistance(predatorAgentIndex, 1, listDirections[i]));
			}
			result.add(line);
		}
		return result;
	}

	/*
	 * Order the worst predator based on their worst shortest minimum distance
	 * 
	 * @param distancePredatorsNeighbors: table of the minimum distance between
	 * each predators and each neighbor cells of the prey
	 * 
	 * @param nbrPredator: the number of agent and the size X of the table
	 * distancePredatorsNeighbors (nbrPredator x 4)
	 * 
	 * @return int[] the ordered list of the predator's index
	 */
	private int[] computeWorstPredator(ArrayList<ArrayList<Integer>> distancePredatorsNeighbors, int nbrPredator) {
		// compute the shortest distance for each predator
		Integer[] mappedShortestDistance = new Integer[4];
		for (int i = 0; i < nbrPredator; i++) {
			int shortestDistancePredator = Collections.min(distancePredatorsNeighbors.get(i));
			mappedShortestDistance[i] = shortestDistancePredator;
		}
		// Order the predators based on worst shortest distance to a cell
		// neighboring the prey
		int[] sortedPredatorIndex = IntStream.range(0, nbrPredator).boxed()
				.sorted((i, j) -> mappedShortestDistance[j].compareTo(mappedShortestDistance[i])).mapToInt(e -> e)
				.toArray();
		return sortedPredatorIndex;
	}

	/*
	 * Compute the attribution of a cell neighboring the prey for each predator
	 * Direction indicate where the neighbor is located in function of the prey
	 * 
	 * @param distancePredatorsNeighbors: table of the minimum distance between
	 * each predators and each neighbor cells of the prey
	 * 
	 * @param orderedWorstPredator the order of the predator to consider
	 * 
	 * @return Direction[] the final cell attibution for each predator
	 */
	private Direction[] computeDirectionPerPredator(ArrayList<ArrayList<Integer>> distancePredatorsNeighbors,
			int[] orderedWorstPredator) {
		Direction[] result = new Direction[orderedWorstPredator.length];
		for (int i = 0; i < orderedWorstPredator.length; i++) {
			int predatorIndex = orderedWorstPredator[i];
			ArrayList<Integer> listDistance = distancePredatorsNeighbors.get(predatorIndex);
			int directionIndex = listDistance.indexOf(Collections.min(listDistance));
			// set already used index to max value, so the minimum will be an
			// unused index
			for (int j = 0; j < distancePredatorsNeighbors.size(); j++) {
				distancePredatorsNeighbors.get(j).set(directionIndex, Integer.MAX_VALUE);
			}
			result[predatorIndex] = Direction.values()[directionIndex];
		}
		return result;
	}

	// compute the A* path and return the next Direction of the agent to follow
	// the path algorithm comments are from
	// https://en.wikipedia.org/wiki/A%2a_search_algorithm
	private Direction getNextAStarPathStep(State state, int destinationX, int destinationY) {
		// The set of nodes already evaluated.
		// closedSet := {}
		HashSet<Node> closedSet = new HashSet<Node>();
		// The set of currently discovered nodes that are not evaluated yet.
		// Initially, only the start node is known.
		// openSet := {start}
		Node startNode = new Node(posX, posY, state.getMapHeight(), state.getMapWidth());
		Node goalNode = new Node(destinationX, destinationY, state.getMapHeight(), state.getMapWidth());
		HashSet<Node> openSet = new HashSet<Node>();
		openSet.add(startNode);
		// For each node, which node it can most efficiently be reached from.
		// If a node can be reached from many nodes, cameFrom will eventually
		// contain the
		// most efficient previous step.
		Map<Node, Node> cameFrom = new HashMap<Node, Node>();
		// For each node, the cost of getting from the start node to that node.
		Map<Node, Integer> gScore = new HashMap<Node, Integer>();
		// The cost of going from start to start is zero.
		// gScore[start] := 0
		gScore.put(startNode, 0);
		// For each node, the total cost of getting from the start node to the
		// goal
		// by passing by that node. That value is partly known, partly
		// heuristic.
		Map<Node, Integer> fScore = new HashMap<Node, Integer>();
		// For the first node, that value is completely heuristic.
		fScore.put(startNode, state.getDistance(posX, posY, destinationX, destinationY));
		// while openSet is not empty
		while (!openSet.isEmpty()) {
			// current := the node in openSet having the lowest fScore[] value
			Node currentNode = getNodeWithMinValue(fScore, openSet);
			// System.out.println(fScore+" "+openSet);
			// if current = goal
			if (currentNode.equals(goalNode)) {
				// System.out.println("found a path "+startNode+" to
				// "+goalNode);
				Direction dir = reconstructPath(cameFrom, startNode, currentNode);
				// System.out.println("went "+dir);
				// return reconstruct_path(cameFrom, current)
				return dir;
			}
			// openSet.Remove(current)
			openSet.remove(currentNode);
			// closedSet.Add(current)
			closedSet.add(currentNode);
			// for each neighbor of current
			Direction[] directionList = Direction.values();
			// System.out.println("entered the for loop "+currentNode);
			// state.printAgentCoordinateHelper();
			for (Direction d : directionList) {
				Node neighborNode = getMovedNode(currentNode, d);
				// neighborNode is not an obstacle
				if (!state.isDirectionBlocked(currentNode.posX, currentNode.posY, d) || neighborNode.equals(goalNode)) {
					// System.out.println(currentNode+" "+d+" "+neighborNode);
					// Ignore the neighbor which is already evaluated.
					if (!closedSet.contains(neighborNode)) {
						// System.out.println(neighborNode+" "+closedSet);
						// The distance from start to a neighbor
						// tentative_gScore := gScore[current] +
						// dist_between(current, neighbor)
						int tentative_gScore = gScore.getOrDefault(currentNode, Integer.MAX_VALUE) + 1;
						// System.out.println(tentative_gScore);
						// Discover a new node
						if (!openSet.contains(neighborNode)) {
							openSet.add(neighborNode);
						}
						// This is a better path.
						if (tentative_gScore < gScore.getOrDefault(neighborNode, Integer.MAX_VALUE)) {
							// This path is the best until now. Record it!
							// cameFrom[neighbor] := current
							cameFrom.put(neighborNode, currentNode);
							// gScore[neighbor] := tentative_gScore
							gScore.put(neighborNode, tentative_gScore);
							// fScore[neighbor] := gScore[neighbor] +
							// heuristic_cost_estimate(neighbor, goal)
							fScore.put(neighborNode, tentative_gScore + state.getDistance(neighborNode.posX,
									neighborNode.posY, destinationX, destinationY));
						}
					}
				}
			}
			// System.out.println("OpenSet: "+openSet);
		}
		// return failure
		// failure happened if the agent is captured by 3 predators and the
		// prey, so the A* fail
		// instead of an error, just move randomly
		// throw new IllegalStateException("A* didn't found a path from
		// "+startNode+" to "+goalNode);
		double randomDouble = rand.generateDouble();
		return Direction.values()[(int) (randomDouble * 4)];
	}

	private Direction reconstructPath(Map<Node, Node> cameFrom, Node startNode, Node currentNode) {
		Node previousNode = currentNode;
		// System.out.println("PATH:");
		// System.out.print(previousNode+" -> ");
		while (!cameFrom.get(previousNode).equals(startNode)) {
			previousNode = cameFrom.get(previousNode);
			// System.out.print(previousNode+" -> ");
		}
		// System.out.println(startNode);
		// previousNode is now the node accessed by startNode
		return computeDirection(startNode, previousNode);
	}

	// compute the direction from the first node to the second node
	// assuming they are neighbors
	private Direction computeDirection(Node startingNode, Node destinationNode) {
		if (getMovedNode(startingNode, Direction.LEFT).equals(destinationNode)) {
			return Direction.LEFT;
		}
		if (getMovedNode(startingNode, Direction.RIGHT).equals(destinationNode)) {
			return Direction.RIGHT;
		}
		if (getMovedNode(startingNode, Direction.TOP).equals(destinationNode)) {
			return Direction.TOP;
		}
		if (getMovedNode(startingNode, Direction.BOTTOM).equals(destinationNode)) {
			return Direction.BOTTOM;
		}
		return null;
	}

	private Node getMovedNode(Node n, Direction d) {
		int newPosX = n.posX;
		int newPosY = n.posY;
		switch (d) {
		case LEFT:
			newPosX = n.posX - 1 + n.mapWidth;
			break;
		case TOP:
			newPosY = n.posY - 1 + n.mapHeight;
			break;
		case RIGHT:
			newPosX = n.posX + 1;
			break;
		case BOTTOM:
			newPosY = n.posY + 1;
			break;
		default:
			break;
		}
		newPosX = newPosX % n.mapWidth;
		newPosY = newPosY % n.mapHeight;
		return new Node(newPosX, newPosY, n.mapHeight, n.mapWidth);
	}

	private static Node getNodeWithMinValue(Map<Node, Integer> map, HashSet<Node> openSet) {
		Node minNode = null;
		int minValue = Integer.MAX_VALUE;
		for (Node key : openSet) {
			int value = map.getOrDefault(key, Integer.MAX_VALUE);
			if (value < minValue) {
				minValue = value;
				minNode = key;
			}
		}
		return minNode;
	}

	private class Node {
		public final int posX;
		public final int posY;
		public final int mapWidth;
		public final int mapHeight;

		public Node(int posX, int posY, int mapHeight, int mapWidth) {
			this.posX = posX;
			this.posY = posY;
			this.mapWidth = mapWidth;
			this.mapHeight = mapHeight;
		}

		@Override
		public int hashCode() {
			return posX + posY * mapWidth;
		}

		@Override
		public boolean equals(Object obj) {
			Node n = (Node) obj;
			return hashCode() == n.hashCode();
		}

		@Override
		public String toString() {
			return "NODE[posX: " + posX + " posY: " + posY + "]";
		}

	}
}
