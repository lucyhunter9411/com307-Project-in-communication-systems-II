package Actor;

import Enum.AgentType;
import Enum.Direction;
import Main.State;

public class Prey extends Agent {
	public Prey(int x, int y, int agentIndex, long randSeed) {
		super(x, y, agentIndex, randSeed);
		type = AgentType.Prey;
	}

	public Prey(State s, int agentIndex, long randSeed) {
		super(s.getAgentsCoordinateList()[agentIndex - 1][0], s.getAgentsCoordinateList()[agentIndex - 1][1],
				agentIndex, randSeed);
		type = AgentType.Prey;
	}

	@Override
	public Direction iterate(State currentState) {
		// return one of the 4 direction with each a probability 1/4
		double randomDouble = rand.generateDouble();
		return Direction.values()[(int) (randomDouble * 4)];
	}

	@Override
	public void initiate(State initialState) {
	}
}