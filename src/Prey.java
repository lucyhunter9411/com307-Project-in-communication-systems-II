public class Prey implements Agent {

	private int posX,posY;
	private final int agentIndex;
	public Prey(int posX, int posY, int agentIndex) {
		this.posX = posX;
		this.posY = posY;
		this.agentIndex = agentIndex;
	}

	public int getPosY() {
		return posY;
	}

	public int getPosX() {
		return posX;
	}

	public int iterate(State currentState, RandomSeededDouble r) {
		return findNextMove(r);
	}
	
	//return 1 to 4 with each a probability 1/4
	public int findNextMove(RandomSeededDouble r){
		double randomDouble = r.generateDouble();
		return (int)(randomDouble * 4) + 1;
	}
	
	public void setPos(int newPosX, int newPosY) {
		posX = newPosX;
		posY = newPosY;	
	}
	
	public int getAgentIndex() {
		return agentIndex;
	}
}