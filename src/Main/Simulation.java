package Main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.InputMismatchException;

import Actor.*;
import Enum.Direction;

public class Simulation {

	private int mapHeight;
	private int mapWidth;
	private int nbrPredator;
	private int nbrOfIteration = 0;
	private State initialState;
	private ArrayList<Agent> agents = new ArrayList<Agent>();
	private RandomSeededDouble rand;

	public Simulation(int mapSizeHeight, int mapSizeWidth, int numberPredator, long seed, int nbrGreedyPredator,
			boolean useOneMtcPredator) {
		initialState = new State(mapSizeHeight, mapSizeWidth, numberPredator);
		this.mapHeight = mapSizeHeight;
		this.mapWidth = mapSizeWidth;
		this.nbrPredator = numberPredator;
		rand = new RandomSeededDouble(seed);

		// initialize the agents at random position
		int pos, posX, posY;
		int i = 1;
		int finishedNumberAgents = nbrPredator + 1;
		if (mapSizeHeight * mapSizeWidth < finishedNumberAgents) {
			throw new AssertionError("more agents than cells");
		}
		if (nbrGreedyPredator > numberPredator) {
			throw new InputMismatchException("more greedy predator than the total of predator");
		}
		int greedyPredatorToAdd = nbrGreedyPredator;
		boolean addedMTCPredator = false;
		while (i <= finishedNumberAgents) {
			pos = (int) (mapHeight * mapWidth * rand.generateDouble());
			posX = pos % mapWidth;
			posY = pos / mapWidth;
			if (initialState.setAgentI(posX, posY, i)) {
				if (i == 1) {
					agents.add(new Prey(posX, posY, i, rand.generateLong()));
				} else {
					if (!addedMTCPredator && useOneMtcPredator) {
						agents.add(new MonteCarloPredator(posX, posY, i, rand.generateLong()));
						addedMTCPredator = true;
					} else if (greedyPredatorToAdd > 0) {
						agents.add(new GreedyPredator(posX, posY, i, rand.generateLong()));
						greedyPredatorToAdd--;
					} else {
						agents.add(new TeammateAwarePredator(posX, posY, i, rand.generateLong()));
					}
				}
				i++;
			}
		}
		// initiate the agents with the initialState
		for (Agent a : agents) {
			a.initiate(initialState);
		}
	}

	public void draw(Graphics g, int windowHeight) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, windowHeight, windowHeight * mapHeight / mapWidth);

		int squareSize = windowHeight / mapWidth;
		int r = 2;
		// draw each square of the grid
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				g.setColor(Color.WHITE);
				g.fillRect(i * squareSize + r, j * squareSize + r, squareSize - 2 * r, squareSize - 2 * r);
				// draw the agent, the prey is yellow and red others.
				if (initialState.getPos(i, j) != 0) {
					if (initialState.getPos(i, j) == 1) {
						g.setColor(Color.YELLOW);
					} else {
						g.setColor(Color.RED);
					}
					g.fillOval(i * squareSize + 2 * r, j * squareSize + 2 * r, squareSize - 4 * r, squareSize - 4 * r);
				}
			}
		}
		//
		g.setColor(Color.WHITE);
		for (int i = 0; i < nbrPredator; i++) {
			g.drawString(" " + i + " ", initialState.getAgentsCoordinateList()[i + 1][0] * squareSize + squareSize / 2,
					initialState.getAgentsCoordinateList()[i + 1][1] * squareSize + squareSize / 2);
		}
	}

	public boolean iterate() {
		// compute all the agents' next move
		ArrayList<Direction> directionOfAgents = new ArrayList<Direction>();
		for (Agent a : agents) {
			directionOfAgents.add(a.iterate(initialState));
		}
		// apply the agents' next move to compute the new state
		// done in a way that all the movements are done at the same time and
		// not sequentially, avoiding collisions
		initialState.modifyState(directionOfAgents, agents);

		// check if the prey is captured
		if (initialState.isPreyCaptured()) {
			System.out.println("captured in " + nbrOfIteration + " steps");
			return true;
		} else {
			nbrOfIteration++;
		}
		return false;
	}

	public int getNbrOfIteration() {
		return nbrOfIteration;
	}
}