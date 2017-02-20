package Main;

import Enum.Direction;

public class State {
	private int mapHeight;
	private int mapWidth;
	private int nbrAgents;
	private int[][] cellsMap;
	private int[][] agentsCoordinate;
	
	public State(int height, int width, int nbrPredator){
		nbrAgents = nbrPredator+1;
		mapHeight=height;
		mapWidth=width;
		cellsMap = new int[mapWidth][mapHeight];
		for(int i=0;i<mapWidth;i++){
			for(int j=0;j<mapHeight;j++){
				cellsMap[i][j] = 0;
			}
		}
		agentsCoordinate = new int[nbrAgents][2];
	}
	
	public int[][] getMap(){
		return cellsMap;
	}

	public boolean setAgentI(int posX, int posY, int i) {
		if(i>nbrAgents){
			throw new IllegalArgumentException("the index is bigger than the number of agent");
		}
		if(cellsMap[posX][posY]==0){
			cellsMap[posX][posY]=i;
			agentsCoordinate[i-1][0] = posX;
			agentsCoordinate[i-1][1] = posY;
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
	
	public int[][] getAgentsCoordinateList(){
		return agentsCoordinate;
	}
	
	public int getPreyPosX(){
		return agentsCoordinate[0][0];
	}
	
	public int getPreyPosY(){
		return agentsCoordinate[0][1];
	}
	
	public boolean isPreyCaptured(){
		int x = getPreyPosX();
		int y = getPreyPosY();
		return isDirectionBlocked(x,y,Direction.LEFT) &&
			isDirectionBlocked(x,y,Direction.TOP) &&
			isDirectionBlocked(x,y,Direction.RIGHT) &&
			isDirectionBlocked(x,y,Direction.BOTTOM);
	}
	
	public boolean isDirectionBlocked(int x, int y, Direction direction) {
		switch(direction){
		case LEFT:
			int leftPosX = (x + mapWidth - 1) % mapWidth;
			return cellsMap[leftPosX][y] != 0;
		case TOP:
			int topPosY = (y + mapHeight - 1) % mapHeight;
			return cellsMap[x][topPosY] != 0;
		case RIGHT:
			int rightPosX = (x + 1) % mapWidth;
			return cellsMap[rightPosX][y] != 0;
		case BOTTOM:
			int bottomPosY = (y + 1) % mapHeight;
			return cellsMap[x][bottomPosY] != 0;
		default:
			return false;
		}
	}

	public void printMapHelper(){
		for(int i=0;i<mapHeight;i++){
			for(int j=0;j<mapWidth;j++){
				System.out.print(cellsMap[j][i]+" ");
			}
			System.out.println();
		}
	}
	
	public void printAgentCoordinateHelper(){
		for(int i=0;i<2;i++){
			for(int j=0;j<nbrAgents;j++){
				System.out.print(agentsCoordinate[j][i]+" ");
			}
			System.out.println();
		}
	}
}
