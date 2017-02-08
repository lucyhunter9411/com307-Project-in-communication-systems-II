
public interface Agent {

	public abstract int getPosY();
	public abstract int getPosX();
	
	public abstract void iterate(State state, RandomSeededDouble r);
}
