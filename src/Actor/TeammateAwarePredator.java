package Actor;

import Enum.Direction;
import Main.RandomSeededDouble;
import Main.State;

public class TeammateAwarePredator extends Agent{
	
	public TeammateAwarePredator(int x,int y, int agentIndex){
		super(x,y,agentIndex);
	}
	
	@Override
	public Direction iterate(State state, RandomSeededDouble r) {
		//Calculate the distance from each predator to each cell neighboring the prey.
		// Order the predators based on worst shortest distance to a cell neighboring the prey.
		//TODO		
		int[] worstOrderedPredator = computePredatorDistanceOrder(state);
		/*
		 *  In order, the predators are assigned the unchosen destination
		 *  that is closest to them (without communication), breaking
		 *	ties by a mutually known ordering of the predators.
		 */
		//TODO
		Direction attribuedPreyNeighbor = Direction.BOTTOM;
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
		 *  Otherwise, use A* path planning to select a path, treating other agents as static obstacles.
		*/
		//TODO
		//return one of the 4 direction with each a probability 1/4
		double randomDouble = r.generateDouble();
		return Direction.values()[(int)(randomDouble * 4)];
	}

	private int[] computePredatorDistanceOrder(State state) {
		int nbrPredator = state.getNbrAgents()-1;
		return null;
	}
}
