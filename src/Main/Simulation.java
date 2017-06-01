package Main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import Actor.*;
import Enum.AgentType;
import Enum.Direction;

public class Simulation {

	private int mapHeight;
	private int mapWidth;
	private int nbrPredator;
	private int nbrOfIteration = 0;
	private State initialState;
	private ArrayList<Agent> agents = new ArrayList<Agent>();
	private RandomSeededDouble rand;

	public Simulation(int mapSizeHeight, int mapSizeWidth, long seed, AgentType[] agentsList,
			boolean defaultBayesianMode) {
		this.mapHeight = mapSizeHeight;
		this.mapWidth = mapSizeWidth;
		this.nbrPredator = agentsList.length;
		initialState = new State(mapSizeHeight, mapSizeWidth, nbrPredator);
		rand = new RandomSeededDouble(seed);

		// initialize the agents at random position
		int pos, posX, posY;
		int i = 1;
		int finishedNumberAgents = nbrPredator + 1;
		if (mapSizeHeight * mapSizeWidth < finishedNumberAgents) {
			throw new AssertionError("more agents than cells");
		}
		while (i <= finishedNumberAgents) {
			pos = (int) (mapHeight * mapWidth * rand.generateDouble());
			posX = pos % mapWidth;
			posY = pos / mapWidth;
			if (initialState.setAgentI(posX, posY, i)) {
				if (i == 1) {
					agents.add(new Prey(posX, posY, i, rand.generateLong()));
				} else {
					if (agentsList[i - 2] == AgentType.MonteCarlo) {
						int modelIndex = -1;
						// we compute the identifier of our model if we don't
						// use the bayesian model
						if (!defaultBayesianMode) {
							modelIndex = computeModelIndex(agentsList);
						}
						agents.add(new MonteCarloPredator(posX, posY, i, rand.generateLong(), defaultBayesianMode,
								modelIndex));
					} else if (agentsList[i - 2] == AgentType.Greedy) {
						agents.add(new GreedyPredator(posX, posY, i, rand.generateLong()));
					} else if (agentsList[i - 2] == AgentType.TeammateAware) {
						agents.add(new TeammateAwarePredator(posX, posY, i, rand.generateLong()));
					} else {
						throw new AssertionError("predatorList is in a wrong State");
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

	// compute the unique integer representing the models of the others agents
	// in the simulation
	private int computeModelIndex(AgentType[] agentsList) {
		int result = 0;
		for (int i = 0; i < nbrPredator - 1; i++) {
			if (agentsList[i + 1] == AgentType.TeammateAware) {
				result += Math.pow(2, i);
			}
		}
		return result;
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
		g.setColor(Color.BLACK);
		for (Agent a : agents) {
			int j = a.getAgentIndex();
			g.drawString(" " + j + " " + a.getType(), a.getPosX() * squareSize + squareSize / 4,
					a.getPosY() * squareSize + squareSize / 2);
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