
public class Map {
	
	private int height;
	private int width;
	private int[][] cellsMap;
	
	public Map(int height, int width, Agent agent1, Agent agent2, Agent agent3, Agent agent4, Agent prey){
		this.height = height;
		this.width = width;
		cellsMap = new int[height][width];
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				cellsMap[i][j] = 0;
			}
		}
		cellsMap[agent1.getPosX()][agent1.getPosY()] = 1;
		cellsMap[agent2.getPosX()][agent2.getPosY()] = 2;
		cellsMap[agent3.getPosX()][agent3.getPosY()] = 3;
		cellsMap[agent4.getPosX()][agent4.getPosY()] = 4;
		cellsMap[prey.getPosX()][prey.getPosY()] = 5;
	}
}
