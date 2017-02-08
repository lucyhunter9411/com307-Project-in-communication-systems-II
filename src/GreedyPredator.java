
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

	public void iterate(int[][] map, int height, int width, RandomSeededDouble r) {
		int i = map[posX][posY];
		map[posX][posY] = 0;
		int newPosX = posX;
		int newPosY = posY;
		switch(isCloseToPrey(map,height,width))
		{
		case 1: newPosX = posX-1+width; break;
		case 2: newPosY = posY-1+height; break;
		case 3: newPosX = posX+1; break;
		case 4: newPosY = posY+1; break;
		default: break;
		}
		newPosX = newPosX % width;
		newPosY = newPosY % height;
		//check if the new spot is free
		if(map[newPosX][newPosY]==0){
			posX = newPosX;
		}
		map[posX][posY] = i;
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
	
}
