public interface Agent {

	public abstract int getPosY();
	public abstract int getPosX();
	
	//return 1,2,3,4 {go left, go top, go right, go bottom} in function of the previous state
	public abstract Direction iterate(State state, RandomSeededDouble r);
	public abstract int getAgentIndex();
	public abstract void setPos(int newPosX, int newPosY);
}