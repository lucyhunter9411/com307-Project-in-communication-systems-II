package Main;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import Actor.*;
import Enum.Direction;

public class Simulation {

	private int mapHeight;
	private int mapWidth;
	private int nbrPredator;
	private State initialState;
	private ArrayList<Agent> agents = new ArrayList<Agent>();
	private RandomSeededDouble rand;

	public Simulation(int mapSizeHeight, int mapSizeWidth, int numberPredator, long seed, boolean agentType){
		initialState = new State(mapSizeHeight,mapSizeWidth,numberPredator);
		this.mapHeight = mapSizeHeight;
		this.mapWidth = mapSizeWidth;
		this.nbrPredator = numberPredator;
		rand = new RandomSeededDouble(seed);

		//initialize the agents at random position 
		int pos,posX,posY;
		int i=1;
		int finishedNumberAgents = nbrPredator+1;
		while(i<=finishedNumberAgents){
			pos = (int)(mapHeight*mapWidth*rand.generateDouble());
			posX = pos % mapWidth;
			posY = pos / mapWidth;
			if(initialState.setAgentI(posX,posY,i)){
				if(i == 1){
					agents.add(new Prey(posX,posY,i));	
				}
				else{
					if(agentType){
						agents.add(new GreedyPredator(posX,posY,i));
					}
					else{
						agents.add(new TeammateAwarePredator(posX,posY,i));
					}
				}
				i++;
			}
		}	
	}

	public void draw(Graphics g, int windowHeight) {
		g.setColor(Color.BLACK);
		g.fillRect(0,0,windowHeight,windowHeight*mapHeight/mapWidth);

		int squareSize = windowHeight/mapWidth;
		int r = 2;
		//draw each square of the grid
		for(int i=0;i<mapWidth;i++){
			for(int j=0;j<mapHeight;j++){
				g.setColor(Color.WHITE);
				g.fillRect(i*squareSize+r, j*squareSize+r, squareSize-2*r, squareSize-2*r);
				//draw the agent, the prey is yellow and red others.
				if(initialState.getPos(i, j) != 0){
					if(initialState.getPos(i, j) == 1){
						g.setColor(Color.YELLOW);
					}
					else{
						g.setColor(Color.RED);
					}
					g.fillOval(i*squareSize+2*r,j*squareSize+2*r, squareSize-4*r, squareSize-4*r);	

				}
			}
		}
	}

	public boolean iterate() {
		//compute all the agents' next move
		ArrayList<Direction> directionOfAgents = new ArrayList<Direction>();
		for(Agent a:agents){
			directionOfAgents.add(a.iterate(initialState, rand));
		}
		
		//apply the agents' next move to compute the new state
		for(int i=0;i<directionOfAgents.size();i++){
			modifyState(initialState,agents.get(i),directionOfAgents.get(i));
		}
		//check if the prey is captured
		if(initialState.isPreyCaptured()){
			System.out.println("captured");
			return true;
		}
		return false;
	}

	public void modifyState(State currentState, Agent currentAgent, Direction agentNextDirection){
		int posX = currentAgent.getPosX();
		int posY = currentAgent.getPosY();
		int i = currentState.getPos(posX, posY);
		int newPosX = posX;
		int newPosY = posY;
		switch(agentNextDirection)
		{
		case LEFT: newPosX = posX - 1 + mapWidth; break;
		case TOP: newPosY = posY - 1 + mapHeight; break;
		case RIGHT: newPosX = posX + 1; break;
		case BOTTOM: newPosY = posY + 1; break;
		default: break;
		}
		newPosX = newPosX % mapWidth;
		newPosY = newPosY % mapHeight;
		//check if the new spot is free and set the new position of the agent
		if(currentState.setAgentI(newPosX, newPosY, i)){
			currentState.setPos(posX, posY, 0);
			currentAgent.setPos(newPosX,newPosY);
		}

	}
}