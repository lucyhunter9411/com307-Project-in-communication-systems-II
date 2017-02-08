
public interface Agent {

	public abstract int getPosY();
	public abstract int getPosX();
	
	public abstract void iterate(int[][] map, int height, int width, RandomSeededDouble r);
}
