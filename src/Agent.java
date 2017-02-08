
public interface Agent {

	public abstract int getPosY();
	public abstract int getPosX();
	
	public abstract int iterate(State state, RandomSeededDouble r);
	public abstract void setPos(int newPosX, int newPosY);
}
