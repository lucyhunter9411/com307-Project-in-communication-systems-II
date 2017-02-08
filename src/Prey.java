
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

	public void iterate(State currentState, RandomSeededDouble r) {
		int width = currentState.getMapWidth();
		int height = currentState.getMapHeight();
		int i = currentState.getPos(posX, posY);
		currentState.setPos(posX,posY,0);
		int newPosX = posX;
		int newPosY = posY;
		switch(findNextMove(r))
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
		if(currentState.getPos(newPosX, newPosY)==0){
			posX = newPosX;
			posY = newPosY;
		}
		currentState.setPos(posX, posY, i);
	}
	//1 go top
	//2 go left
	//3 go bottom
	//4 go right
	public int findNextMove(RandomSeededDouble r){
		double randomDouble = r.generateDouble();
		if(randomDouble<0.25){
			return 1;
		}
		else if(randomDouble < 0.5){
			return 2;
		}
		else if(randomDouble < 0.75){
			return 3;
		}
		else{
			return 4;
		}
	}
}
