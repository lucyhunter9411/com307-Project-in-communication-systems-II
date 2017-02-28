package Actor;
import Enum.Direction;
import Main.RandomSeededDouble;
import Main.State;

public abstract class Agent {

	protected int posY, posX;
	protected int agentIndex;
	
	public Agent(int x,int y,int agentIndex){
		posX = x;
		posY = y;
		this.agentIndex = agentIndex;
	}
	
	public int getPosY(){
		return posY;
	}
	
	public int getPosX(){
		return posX;
	}
	
	public int getAgentIndex(){
		return agentIndex;
	}
	
	public void setPos(int newPosX, int newPosY){
		posX = newPosX;
		posY = newPosY;
	}
	
	//return {left, top, right, bottom} in function of the previous state
	public abstract Direction iterate(State state, RandomSeededDouble r);

	//called once for agents for precomputed algorithm on the initialState of the Simulation
	public abstract void initiate(State initialState);
}