
public class Prey implements Agent {

	private int posX,posY;
	
	public Prey(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}

	public int getPosY() {
		return posY;
	}

	public int getPosX() {
		return posX;
	}

	public void iterate(int[][] map,int height,int width, RandomSeededDouble r) {
		int i = map[posX][posY];
		map[posX][posY] = 0;
		double randomDouble = r.generateDouble();
		int newPosX = posX;
		int newPosY = posY;
		if(randomDouble<0.25){
			newPosX = posX-1 + width;
		}
		else if(randomDouble < 0.5){
			newPosY = posY-1 + height;
		}
		else if(randomDouble < 0.75){
			newPosX = posX+1;
		}
		else{
			newPosY = posY+1;
		}
		
		newPosX = newPosX % width;
		newPosY = newPosY % height;
		//check if the new spot is free
		if(map[newPosX][newPosY]==0){
			posX = newPosX;
			posY = newPosY;
		}
		map[posX][posY] = i;
	}
}
