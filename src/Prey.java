
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

	public int iterate(State currentState, RandomSeededDouble r) {
		return findNextMove(r);
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
	
	public void setPos(int newPosX, int newPosY) {
		posX = newPosX;
		posY = newPosY;	
	}
}
