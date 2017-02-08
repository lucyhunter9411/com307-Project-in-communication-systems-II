
public class GreedyPredator implements Agent{

	private int posX, posY;
	public GreedyPredator(int x, int y){
		posX = x;
		posY = y;
	}
	
	public int getPosY() {
		return posY;
	}

	public int getPosX() {
		return posX;
	}

	public int iterate(State state, RandomSeededDouble r) {
		return 1;
	}
	//check is one of his neighbor is the prey
	//0 no neighbor
	//1 the prey is on top
	//2 the prey is on left
	//3 the prey is on bottom
	//4 the prey is right
	public int isCloseToPrey(int[][] map, int height, int width){
		return 0;
	}

	public void setPos(int newPosX, int newPosY) {
		posX = newPosX;
		posY = newPosY;		
	}
	
}
