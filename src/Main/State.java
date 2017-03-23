package Main;

import java.util.ArrayList;
import java.util.Arrays;

import Actor.Agent;
import Enum.Direction;

public class State {
	private int mapHeight;
	private int mapWidth;
	private int nbrAgents;
	private int[][] cellsMap;
	private int[][] agentsCoordinate;

	public State(int height, int width, int nbrPredator) {
		nbrAgents = nbrPredator + 1;
		mapHeight = height;
		mapWidth = width;
		cellsMap = new int[mapWidth][mapHeight];
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				cellsMap[i][j] = 0;
			}
		}
		agentsCoordinate = new int[nbrAgents][2];
	}

	// new state using deep copy of its parameters
	private State(int mapHeight, int mapWidth, int nbrAgents, int[][] cellsMap, int[][] agentsCoordinate) {
		this.mapHeight = mapHeight;
		this.mapWidth = mapWidth;
		this.nbrAgents = nbrAgents;
		this.cellsMap = new int[mapWidth][mapHeight];
		for (int i = 0; i < cellsMap.length; i++) {
			this.cellsMap[i] = Arrays.copyOf(cellsMap[i], cellsMap[i].length);
		}
		this.agentsCoordinate = new int[nbrAgents][2];
		for (int i = 0; i < cellsMap.length; i++) {
			this.agentsCoordinate[i] = Arrays.copyOf(agentsCoordinate[i], agentsCoordinate[i].length);
		}

	}

	public int[][] getMap() {
		return cellsMap;
	}

	public boolean setAgentI(int posX, int posY, int i) {
		if (i > nbrAgents) {
			throw new IllegalArgumentException("the index is bigger than the number of agent");
		}
		if (cellsMap[posX][posY] == 0) {
			cellsMap[posX][posY] = i;
			agentsCoordinate[i - 1][0] = posX;
			agentsCoordinate[i - 1][1] = posY;
			return true;
		}
		return false;
	}

	public int getPos(int posX, int posY) {
		return cellsMap[posX][posY];
	}

	public void setPos(int posX, int posY, int i) {
		cellsMap[posX][posY] = i;
	}

	public int getMapWidth() {
		return mapWidth;
	}

	public int getMapHeight() {
		return mapHeight;
	}

	public int[][] getAgentsCoordinateList() {
		return agentsCoordinate;
	}

	public int getPreyPosX() {
		return agentsCoordinate[0][0];
	}

	public int getPreyPosY() {
		return agentsCoordinate[0][1];
	}

	public boolean isPreyCaptured() {
		int x = getPreyPosX();
		int y = getPreyPosY();
		return isDirectionBlocked(x, y, Direction.LEFT) && isDirectionBlocked(x, y, Direction.TOP)
				&& isDirectionBlocked(x, y, Direction.RIGHT) && isDirectionBlocked(x, y, Direction.BOTTOM);
	}

	public boolean isDirectionBlocked(int x, int y, Direction direction) {
		switch (direction) {
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

	public void printMapHelper() {
		for (int i = 0; i < mapHeight; i++) {
			for (int j = 0; j < mapWidth; j++) {
				System.out.print(cellsMap[j][i] + " ");
			}
			System.out.println();
		}
	}

	public void printAgentCoordinateHelper() {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < nbrAgents; j++) {
				System.out.print(agentsCoordinate[j][i] + " ");
			}
			System.out.println();
		}
	}

	public int getNbrAgents() {
		return nbrAgents;
	}

	public void modifyState(ArrayList<Direction> directionOfAgents, ArrayList<Agent> agents) {
		boolean[] didAgentMoved = new boolean[directionOfAgents.size()];
		for (int i = 0; i < directionOfAgents.size(); i++) {
			didAgentMoved[i] = false;
		}
		boolean changmentAppliedThisLoop = true;
		while (changmentAppliedThisLoop) {
			changmentAppliedThisLoop = false;
			for (int i = 0; i < directionOfAgents.size(); i++) {
				if (!didAgentMoved[i]) {
					boolean hasAgentMoved = modifyStateForAgent(agents.get(i), directionOfAgents.get(i));
					if (hasAgentMoved) {
						changmentAppliedThisLoop = true;
						didAgentMoved[i] = hasAgentMoved;
					}
				}
			}
		}
	}

	/*
	 * @param currentAgent: the actual Agent that we move the position
	 * 
	 * @param agentNextDirection: the direction in which the Agent will move
	 * 
	 * @return boolean: true if the agent moved, false if the agent was blocked
	 * by another agent in the destination cell
	 */
	private boolean modifyStateForAgent(Agent currentAgent, Direction agentNextDirection) {
		int posX = currentAgent.getPosX();
		int posY = currentAgent.getPosY();
		int i = getPos(posX, posY);
		if (i != currentAgent.getAgentIndex()) {
			throw new IllegalStateException(" the agent " + currentAgent + "is not rightly registered on the map (i="
					+ i + " agentIndex=" + currentAgent.getAgentIndex() + " )");
		}
		int newPosX = posX;
		int newPosY = posY;
		switch (agentNextDirection) {
		case LEFT:
			newPosX = posX - 1 + mapWidth;
			break;
		case TOP:
			newPosY = posY - 1 + mapHeight;
			break;
		case RIGHT:
			newPosX = posX + 1;
			break;
		case BOTTOM:
			newPosY = posY + 1;
			break;
		default:
			break;
		}
		newPosX = newPosX % mapWidth;
		newPosY = newPosY % mapHeight;
		// check if the new spot is free and set the new position of the agent
		if (setAgentI(newPosX, newPosY, i)) {
			setPos(posX, posY, 0);
			currentAgent.setPos(newPosX, newPosY);
			return true;
		}
		return false;
	}

	// compute the minimum distance for the agent tagged agentIndex1 to reach
	// agent tagged agentIndex2
	// direction if not null, compute the distance as if agent2 moved in this
	// direction first
	public int getDistance(int agentIndex1, int agentIndex2, Direction direction) {
		assert (agentIndex1 > 0 && agentIndex1 <= nbrAgents);
		assert (agentIndex2 > 0 && agentIndex2 <= nbrAgents);
		int posXAgent1 = agentsCoordinate[agentIndex1 - 1][0];
		int posYAgent1 = agentsCoordinate[agentIndex1 - 1][1];
		int posXAgent2 = agentsCoordinate[agentIndex2 - 1][0];
		int posYAgent2 = agentsCoordinate[agentIndex2 - 1][1];

		switch (direction) {
		case LEFT:
			posXAgent2 = (posXAgent2 - 1 + mapWidth) % mapWidth;
			break;
		case TOP:
			posYAgent2 = (posYAgent2 - 1 + mapHeight) % mapHeight;
			break;
		case RIGHT:
			posXAgent2 = (posXAgent2 + 1) % mapWidth;
			break;
		case BOTTOM:
			posYAgent2 = (posYAgent2 + 1) % mapHeight;
			break;
		default:
			break;
		}

		return getDistance(posXAgent1, posYAgent1, posXAgent2, posYAgent2);
	}

	public int getDistance(int posX1, int posY1, int posX2, int posY2) {
		int dx = (posX1 - posX2 + mapWidth) % mapWidth;
		int dy = (posY1 - posY2 + mapHeight) % mapHeight;
		// format the dx and dy to be a relative position around the prey: [0 :
		// width-1] -> [-width/2 : width/2]
		if (dx > mapWidth / 2) {
			dx = dx - mapWidth;
		}
		if (dy > mapHeight / 2) {
			dy = dy - mapHeight;
		}
		return Math.abs(dx) + Math.abs(dy);
	}

	/*
	 * Perform a deep copy of this state
	 */
	public State clone() {
		State clonedState = new State(mapWidth, mapHeight, nbrAgents, cellsMap, agentsCoordinate);
		if (this == clonedState || cellsMap == clonedState.getMap()
				|| agentsCoordinate == clonedState.getAgentsCoordinateList()) {
			throw new IllegalArgumentException("bad cloning");
		}
		return clonedState;
	}
}
