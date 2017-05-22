package Actor;

import Enum.AgentType;
import Enum.Direction;
import Main.RandomSeededDouble;
import Main.State;

//abstract representation of a movable agent on the grid
public abstract class Agent {

	protected int posY, posX;
	// specific index different of the other agent on the grid
	protected int agentIndex;
	protected RandomSeededDouble rand;
	protected AgentType type = AgentType.Unknow;

	public Agent(int x, int y, int agentIndex, long randSeed) {
		posX = x;
		posY = y;
		this.agentIndex = agentIndex;
		rand = new RandomSeededDouble(randSeed);
	}

	public int getPosY() {
		return posY;
	}

	public int getPosX() {
		return posX;
	}

	public int getAgentIndex() {
		return agentIndex;
	}

	public void setPos(int newPosX, int newPosY) {
		posX = newPosX;
		posY = newPosY;
	}

	public AgentType getType() {
		return type;
	}

	// return {left, top, right, bottom} in function of the previous state
	public abstract Direction iterate(State state);

	// called once for agents for pre-computed algorithm on the initialState of
	// the Simulation
	public abstract void initiate(State initialState);

	@Override
	public String toString() {
		return "[Agent: " + type + " index:" + agentIndex + " posX: " + posX + " posY: " + posY + "] ";
	}
}