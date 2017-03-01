package Actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import Enum.Direction;
import Main.RandomSeededDouble;
import Main.State;

public class TeammateAwarePredator extends Agent{

	private Direction attribuedPreyNeighbor;
	public TeammateAwarePredator(int x,int y, int agentIndex){
		super(x,y,agentIndex);
	}

	@Override
	//in the case of a teammate aware predator, we precompute the destination(prey's neighbor cell) attributed for this predator
	public void initiate(State initialState) {
		//TODO the number of predator is 4 atm for this algorithm, but state can have a bigger number
		if(initialState.getNbrAgents()-1!=4){
			throw new IllegalStateException("This agent work only in team of 4 predator");
		}
		//Calculate the distance from each predator to each cell neighboring the prey.		
		ArrayList<ArrayList<Integer>> distancePredatorsNeighbors = computePredatorDistanceOrder(initialState);
		//System.out.println(distancePredatorsNeighbors);
		// Order the predators based on worst shortest distance to a cell neighboring the prey.
		int[] orderedWorstPredator = computeWorstPredator(distancePredatorsNeighbors,4);

		/*
		 *  In order, the predators are assigned the unchosen destination
		 *  that is closest to them (without communication), breaking
		 *	ties by a mutually known ordering of the predators.
		 */
		Direction[] directionPerPredator = computeDirectionPerPredator(distancePredatorsNeighbors,orderedWorstPredator); 
		//System.out.println(directionPerPredator[0]+" "+directionPerPredator[1]+" "+directionPerPredator[2]+" "+directionPerPredator[3]+" ");

		assert(agentIndex>1 && agentIndex<=5);
		attribuedPreyNeighbor = directionPerPredator[agentIndex-2];
	}

	@Override
	public Direction iterate(State state, RandomSeededDouble r) {
		/*
		 *  If the predator is already at the destination, try to move onto
		 *	the prey so that if it moves, the predator will follow.
		 */
		int preyX = state.getPreyPosX();
		int preyY = state.getPreyPosY();
		int width = state.getMapWidth();
		int height = state.getMapHeight();
		int destinationX = preyX;
		int destinationY = preyY;
		//initiate(state);
		//compute the destionation coordinate
		//check if the predator is already on the destination
		switch(attribuedPreyNeighbor){
		case LEFT:
			destinationX = (preyX - 1 + width) % width;
			if(posX == destinationX && posY == destinationY){
				return Direction.RIGHT;
			}
			break;
		case TOP: 
			destinationY = (preyY - 1 + height) % height;
			if(posX == destinationX && posY == destinationY){
				return Direction.BOTTOM;
			}; 
			break;
		case RIGHT:
			destinationX = (preyX + 1) % width;
			if(posX == destinationX && posY == destinationY){
				return Direction.LEFT;
			};
			break;
		case BOTTOM:
			destinationY = (preyY + 1) % height;
			if(posX == destinationX && posY == destinationY){
				return Direction.TOP;
			}; 
			break;
		default: break;
		}

		//  Otherwise, use A* path planning to select a path, treating other agents as static obstacles.
		return getNextAStarPathStep(state,destinationX,destinationY);
	}

	/*
	 * Compute the minimum distance of each predator with each neighboring cell of the prey
	 * @param state, the current state
	 * @return a table (NbrPredator x 4) containing all the minimum distance computed
	 */
	private ArrayList<ArrayList<Integer>> computePredatorDistanceOrder(State state) {
		int nbrPredator = state.getNbrAgents()-1;
		ArrayList<ArrayList<Integer>> result = new ArrayList<>();
		Direction[] listDirections = Direction.values();
		for(int j =0; j<nbrPredator; j++){

			int predatorAgentIndex = j+2;
			ArrayList<Integer> line = new ArrayList<>();
			for(int i = 0; i<listDirections.length; i++){
				//the direction is applied on the second agent
				line.add(state.getDistance(predatorAgentIndex,1,listDirections[i]));
			}
			result.add(line);
		}
		return result;
	}

	/*
	 * Order the worst predator based on their worst shortest minimum distance
	 * @param distancePredatorsNeighbors: table of the minimum distance between each predators and each neighbor cells of the prey
	 * @param nbrPredator: the number of agent and the size X of the table distancePredatorsNeighbors (nbrPredator x 4)
	 * @return int[] the ordered list of the predator's index
	 */
	private int[] computeWorstPredator(ArrayList<ArrayList<Integer>> distancePredatorsNeighbors,int nbrPredator) {
		//compute the shortest distance for each predator
		Integer[] mappedShortestDistance = new Integer[4];
		for(int i = 0;i<nbrPredator;i++){
			int shortestDistancePredator = Collections.min(distancePredatorsNeighbors.get(i));
			mappedShortestDistance[i] = shortestDistancePredator;
		}
		//Order the predators based on worst shortest distance to a cell neighboring the prey
		int[] sortedPredatorIndex = IntStream.range(0, nbrPredator)
				.boxed().sorted((i, j) -> mappedShortestDistance[j].compareTo(mappedShortestDistance[i]))
				.mapToInt(e -> e).toArray();
		return sortedPredatorIndex;
	}

	/*
	 * Compute the attribution of a cell neighboring the prey for each predator
	 * Direction indicate where the neighbor is located in function of the prey 
	 * @param distancePredatorsNeighbors: table of the minimum distance between each predators and each neighbor cells of the prey
	 * @param orderedWorstPredator the order of the predator to consider
	 * @return Direction[] the final cell attibution for each predator
	 */
	private Direction[] computeDirectionPerPredator(ArrayList<ArrayList<Integer>> distancePredatorsNeighbors, int[] orderedWorstPredator) {
		Direction[] result = new Direction[orderedWorstPredator.length];
		for(int i=0;i<orderedWorstPredator.length;i++){
			int predatorIndex = orderedWorstPredator[i];
			ArrayList<Integer> listDistance = distancePredatorsNeighbors.get(predatorIndex);
			int directionIndex = listDistance.indexOf(Collections.min(listDistance));
			//set already used index to max value, so the minimum will be an unused index
			for(int j=0;j<distancePredatorsNeighbors.size();j++){
				distancePredatorsNeighbors.get(j).set(directionIndex, Integer.MAX_VALUE);
			}
			result[predatorIndex] = Direction.values()[directionIndex];
		}
		return result;
	}

	//compute the A* path and return the next Direction of the agent to follow the path
	private Direction getNextAStarPathStep(State state, int destinationX, int destinationY) {

		// The set of nodes already evaluated.
		//closedSet := {}
		ArrayList<Node> closedSet = new ArrayList<Node>();
		// The set of currently discovered nodes that are not evaluated yet.
		// Initially, only the start node is known.
		//openSet := {start}
		Node startNode = new Node(posX,posY,state.getMapWidth());
		Node goalNode = new Node(destinationX,destinationY,state.getMapWidth());
		ArrayList<Node> openSet = new ArrayList<Node>();
		openSet.add(startNode);
		// For each node, which node it can most efficiently be reached from.
		// If a node can be reached from many nodes, cameFrom will eventually contain the
		// most efficient previous step.
		//cameFrom := the empty map
		Map<Node,Node> cameFrom = new HashMap<Node,Node>();

		// For each node, the cost of getting from the start node to that node.
		//gScore := map with default value of Infinity
		Map<Node,Integer> gScore = new HashMap<Node,Integer>();
		// The cost of going from start to start is zero.
		//gScore[start] := 0 
		gScore.put(startNode,0);
		// For each node, the total cost of getting from the start node to the goal
		// by passing by that node. That value is partly known, partly heuristic.
		//fScore := map with default value of Infinity
		Map<Node,Integer> fScore = new HashMap<Node,Integer>();
 		// For the first node, that value is completely heuristic.
		//fScore[start] := heuristic_cost_estimate(start, goal)
		fScore.put(startNode, state.getDistance(posX,posY,destinationX,destinationY));
		//while openSet is not empty
		while(!openSet.isEmpty()){
			//  current := the node in openSet having the lowest fScore[] value
			Node currentNode = getNodeWithMinValue(fScore);
			//if current = goal
			if(currentNode.equals(goalNode)){
			//  return reconstruct_path(cameFrom, current)
				
			}
			// openSet.Remove(current)
			openSet.remove(currentNode);
			//closedSet.Add(current)
			closedSet.add(currentNode);
			//for each neighbor of current
			
			//  if neighbor in closedSet
			//    continue		// Ignore the neighbor which is already evaluated.
			// The distance from start to a neighbor
			//tentative_gScore := gScore[current] + dist_between(current, neighbor)
			//if neighbor not in openSet	// Discover a new node
			//  openSet.Add(neighbor)
			//else if tentative_gScore >= gScore[neighbor]
			//  continue		// This is not a better path.

			// This path is the best until now. Record it!
			//cameFrom[neighbor] := current
			//gScore[neighbor] := tentative_gScore
			//fScore[neighbor] := gScore[neighbor] + heuristic_cost_estimate(neighbor, goal)

			//return failure
			throw new IllegalStateException("A* didn't found a path");
		}

		//function reconstruct_path(cameFrom, current)
		//total_path := [current]
		//while current in cameFrom.Keys:
		//current := cameFrom[current]
		//total_path.append(current)
		//return total_path

		return Direction.LEFT;
	}
	
	private static Node getNodeWithMinValue(Map<Node,Integer> map) {
	    Node minKey = null;
	    int minValue = Integer.MAX_VALUE;
	    for (Node key : map.keySet()) {
	        int value = map.get(key);
	        if (value < minValue) {
	            minValue = value;
	            minKey = key;
	        }
	    }
	    return minKey;
	}
	
	private class Node{
		private int posX;
		private int posY;
		private int mapWidth;
		public Node(int posX, int posY, int mapWidth) {
			this.posX = posX;
			this.posY = posY;
			this.mapWidth = mapWidth;
		}

		@Override
		public int hashCode(){
			return posX + posY * mapWidth;
		}

	}
}
