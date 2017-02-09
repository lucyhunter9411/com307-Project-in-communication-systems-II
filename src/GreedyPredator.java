public class GreedyPredator implements Agent{

	private final int agentIndex;
	private int posX, posY;
	public GreedyPredator(int x, int y, int agentIndex){
		posX = x;
		posY = y;
		this.agentIndex = agentIndex;
	}
	
	public int getPosY() {
		return posY;
	}

	public int getPosX() {
		return posX;
	}
	
	public int iterate(State state, RandomSeededDouble r) {
		return 3;
	}
	
	public int isCloseToPrey(int[][] map, int height, int width){
		return 0;
	}

	public void setPos(int newPosX, int newPosY) {
		posX = newPosX;
		posY = newPosY;		
	}

	public int getAgentIndex() {
		return agentIndex;
	}
	
}