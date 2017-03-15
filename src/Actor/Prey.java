package Actor;
import Enum.Direction;
import Main.State;

public class Prey extends Agent {
	public Prey(int x, int y, int agentIndex, long randSeed) {
		super(x, y, agentIndex, randSeed);
	}

	@Override
	public Direction iterate(State currentState) {
		//return one of the 4 direction with each a probability 1/4
		double randomDouble = rand.generateDouble();
		return Direction.values()[(int)(randomDouble * 4)];
	}

	@Override
	public void initiate(State initialState) {}
}