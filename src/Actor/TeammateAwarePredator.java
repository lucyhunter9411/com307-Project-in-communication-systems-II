package Actor;

import java.util.ArrayList;
import java.util.Collections;
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
		System.out.println(directionPerPredator[0]+" "+directionPerPredator[1]+" "+directionPerPredator[2]+" "+directionPerPredator[3]+" ");

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
		//check if the predator is already on the destination
		switch(attribuedPreyNeighbor){
			case LEFT:
				if(posX == (preyX - 1 + width) % width && posY == preyY){
					return Direction.RIGHT;
				}
				break;
			case TOP: 
				if(posY == (preyY - 1 + height) % height && posX ==preyX){
					return Direction.BOTTOM;
				}; 
				break;
			case RIGHT:
				if(posX == (preyX + 1) % width && posY == preyY){
					return Direction.LEFT;
				};
				break;
			case BOTTOM:
				if(posY == (preyY + 1) % height && posX == preyX){
					return Direction.TOP;
				}; break;
			default: break;
		}
		
		/*
		//TODO
		 *  Otherwise, use A* path planning to select a path, treating other agents as static obstacles.
		*/
		//return one of the 4 direction with each a probability 1/4
		double randomDouble = r.generateDouble();
		return Direction.values()[(int)(randomDouble * 4)];
	}
	

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

	private ArrayList<ArrayList<Integer>> computePredatorDistanceOrder(State state) {
		int nbrPredator = state.getNbrAgents()-1;
		ArrayList<ArrayList<Integer>> result = new ArrayList<>();
		Direction[] listDirections = Direction.values();
		for(int j =0; j<nbrPredator; j++){
			//the direction is applied on the second agent
			int predatorAgentIndex = j+2;
			ArrayList<Integer> line = new ArrayList<>();
			for(int i = 0; i<listDirections.length; i++){
				line.add(state.getDistance(predatorAgentIndex,1,listDirections[i]));
			}
			result.add(line);
		}
		return result;
	}
}
