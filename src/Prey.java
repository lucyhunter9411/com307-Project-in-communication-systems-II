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

	public Direction iterate(State currentState, RandomSeededDouble r) {
		return findNextMove(r);
	}
	
	//return one of the 4 direction with each a probability 1/4
	public Direction findNextMove(RandomSeededDouble r){
		double randomDouble = r.generateDouble();
		return Direction.values()[(int)(randomDouble * 4)];
		}
	
	public void setPos(int newPosX, int newPosY) {
		posX = newPosX;
		posY = newPosY;	
	}
	
	public int getAgentIndex() {
		return agentIndex;
	}
}