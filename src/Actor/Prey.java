package Actor;
import Enum.Direction;
import Main.RandomSeededDouble;
import Main.State;

public class Prey extends Agent {

	public Prey(int x, int y, int agentIndex) {
		super(x,y,agentIndex);
	}

	@Override
	public Direction iterate(State currentState, RandomSeededDouble r) {
		//return one of the 4 direction with each a probability 1/4
		double randomDouble = r.generateDouble();
		return Direction.values()[(int)(randomDouble * 4)];
	}

	@Override
	public void initiate(State initialState) {}
}