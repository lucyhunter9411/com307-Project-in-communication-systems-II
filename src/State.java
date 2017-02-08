
public class State {
	private int mapHeight;
	private int mapWidth;
	//private int nbrPredator;
	private int[][] cellsMap;
	
	public State(int height, int width, int nbrPredator){
		mapHeight=height;
		mapWidth=width;
		cellsMap = new int[mapWidth][mapHeight];
		for(int i=0;i<mapWidth;i++){
			for(int j=0;j<mapHeight;j++){
				cellsMap[i][j] = 0;
			}
		}
	}
	
	public int[][] getMap(){
		return cellsMap;
	}

	public boolean setAgentI(int posX, int posY, int i) {
		if(cellsMap[posX][posY]==0){
			cellsMap[posX][posY]=i;
		return true;
		}
		return false;
	}
	
	public int getPos(int posX,int posY){
		return cellsMap[posX][posY];
	}
	
	public void setPos(int posX,int posY,int i){
		cellsMap[posX][posY] = i;
	}

	public int getMapWidth() {
		return mapWidth;
	}

	public int getMapHeight() {
		return mapHeight;
	}
	
	public void printMapHelper(){
	
		for(int i=0;i<mapHeight;i++){
			for(int j=0;j<mapWidth;j++){
				System.out.print(cellsMap[j][i]+" ");
			}
			System.out.println();
		}
	}
}
