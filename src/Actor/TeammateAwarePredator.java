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
		return Direction.LEFT;
	}
}
