import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Simulation {
	
	private int mapHeight;
	private int mapWidth;
	private int nbrPredator;
	private State initialState;
	private ArrayList<Agent> agents = new ArrayList<Agent>();
	private RandomSeededDouble rand = new RandomSeededDouble(123456789);
	
	public Simulation(int mapSizeHeight, int mapSizeWidth, int numberPredator){
		initialState = new State(mapSizeHeight,mapSizeWidth,numberPredator);
		this.mapHeight = mapSizeHeight;
		this.mapWidth = mapSizeWidth;
		this.nbrPredator = numberPredator;
		
		//initialize the agents 
		int pos,posX,posY;
		int i=1;
		int finishedNumberAgents = nbrPredator+1;
		while(i<=finishedNumberAgents){
			pos = (int)(mapHeight*mapWidth*rand.generateDouble());
			posX = pos%mapWidth;
			posY = pos/mapWidth;
			if(initialState.setAgentI(posX,posY,i)){
				if(i == 1){
				agents.add(new Prey(posX,posY));	
				}
				else{
				agents.add(new GreedyPredator(posX,posY));
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
		for(int i=0;i<mapWidth;i++){
			for(int j=0;j<mapHeight;j++){
				g.setColor(Color.WHITE);
				g.fillRect(i*squareSize+r, j*squareSize+r, squareSize-2*r, squareSize-2*r);
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

	public void iterate() {
		for(Agent a:agents)
		{
			a.iterate(initialState, rand);
		}
	}
}
