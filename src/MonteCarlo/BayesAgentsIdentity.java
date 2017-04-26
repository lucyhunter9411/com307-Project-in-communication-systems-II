package MonteCarlo;

import java.util.ArrayList;

import Actor.Agent;
import Actor.GreedyPredator;
import Actor.Prey;
import Actor.TeammateAwarePredator;
import Enum.AgentType;
import Enum.Direction;
import Main.State;

public class BayesAgentsIdentity {
	private final double NAMBLA = 0.5;
	private int nbrPredator;
	private final int NBR_TYPE_PREDATOR = AgentType.values().length - 2;
	private State baseState;
	private State previousState;
	private double probabilityModelOfAction[][];
	private ArrayList<Agent> agentsGreedy = new ArrayList<Agent>();
	private ArrayList<Agent> agentsTeam = new ArrayList<Agent>();

	public BayesAgentsIdentity(State initialState) {
		nbrPredator = initialState.getNbrAgents() - 1;
		baseState = initialState.clone();
		previousState = initialState.clone();
		// Initialize the table of probability
		probabilityModelOfAction = new double[nbrPredator - 1][NBR_TYPE_PREDATOR];
		double initialRatio = 1.0 / (NBR_TYPE_PREDATOR - 1);
		for (int i = 0; i < nbrPredator - 1; i++) {
			for (int j = 0; j < NBR_TYPE_PREDATOR - 1; j++) {
				probabilityModelOfAction[i][j] = initialRatio;
			}
		}
		// create the agents of each type
		for (int i = 0; i < nbrPredator - 1; i++) {
			int realAgentIndex = i + 3;
			agentsGreedy.add(new GreedyPredator(initialState, realAgentIndex, 0));
			agentsTeam.add(new TeammateAwarePredator(initialState, realAgentIndex, 0));
		}

	}

	public void newStateInformation(State newState, Direction previousMTCDirection) {
		// we don't gain any information on the first iteration so we filter the
		// case out (in this case previousMTCDirection is null)
		if (!(newState.toLongApproximation() == baseState.toLongApproximation()
				&& newState.toLongApproximation() == previousState.toLongApproximation())) {
			// previousState.printMapHelper();
			// System.out.println();
			// newState.printMapHelper();
			State currentState = newState.clone();
			// and compare if the agent moved to the right place
			ArrayList<Agent> agentTested = new ArrayList<Agent>();
			ArrayList<Direction> directionTested = new ArrayList<Direction>();

			// find the models of agents which could generate this new state
			// generate the 2^(nbrPredator-1) States
			final int agentTypePossibility = (int) Math.pow(2, nbrPredator - 1);
			// TODO change in function of nbrpredator
			int tot = 8;
			int[] valG = { 0, 0, 0 };
			int[] valT = { 0, 0, 0 };
			for (int i = 0; i < agentTypePossibility; i++) {
				// update the agentsGreddy and agentsTeam to the last state
				for (int k = 0; k < nbrPredator - 1; k++) {
					int newPosX = previousState.getAgentsCoordinateList()[k + 2][0];
					int newPosY = previousState.getAgentsCoordinateList()[k + 2][1];
					agentsGreedy.get(k).setPos(newPosX, newPosY);
					agentsTeam.get(k).setPos(newPosX, newPosY);
				}
				State copiedState = previousState.clone();
				agentTested.clear();
				directionTested.clear();
				// select the current agents in function of i to cover all
				// possibility, set them to position
				// using the binary value of i
				final boolean[] ret = new boolean[nbrPredator - 1];
				for (int j = 0; j < nbrPredator - 1; j++) {
					ret[nbrPredator - 2 - j] = (1 << j & i) != 0;
				}
				for (int j = 0; j < nbrPredator - 1; j++) {
					if (ret[j]) {
						agentTested.add(agentsGreedy.get(j));
					} else {
						agentTested.add(agentsTeam.get(j));
					}
				}
				// iterate the agent
				for (Agent a : agentTested) {
					directionTested.add(a.iterate(copiedState));
				}
				// add the dummy agent which replaces the MTC
				agentTested.add(0, new GreedyPredator(previousState, 2, 0));
				directionTested.add(0, previousMTCDirection);
				// TODO add a fake prey
				agentTested.add(0, new Prey(previousState, 1, 0));
				directionTested.add(0, Direction.LEFT);
				// System.out.println();
				// System.out.println(agentTested);
				// System.out.println(directionTested);
				copiedState.modifyState(directionTested, agentTested);
				// copiedState.printMapHelper();
				// the result with those agents return the same result as the
				// newState

				for (int j = 0; j < nbrPredator - 1; j++) {
					if (copiedState.hasSameAgentPosition(newState, j + 3)) {
						if (agentTested.get(j + 2).getType().equals(AgentType.Greedy)) {
							valG[j]++;
						} else if (agentTested.get(j + 2).getType().equals(AgentType.TeammateAware)) {
							valT[j]++;
						}
					}
				}
			}
			// System.out.println(valG[0]+" "+valG[1]+" "+valG[2]);
			// System.out.println(valT[0]+" "+valT[1]+" "+valT[2]);

			// compute the next probability using Bayes and the Polynomial
			// Weights Aglorithm
			for (int i = 0; i < nbrPredator - 1; i++) {
				double pActionKGreedy = 2.0 * valG[i] / (tot);
				double pActionKTeam = 2.0 * valT[i] / (tot);
				double pGreedy = probabilityModelOfAction[i][0];
				double pTeam = probabilityModelOfAction[i][1];
				double pAction = pActionKGreedy * pGreedy + pActionKTeam * pTeam;
				double lossGreedy = 1 - pActionKGreedy;
				double lossTeam = 1 - pActionKTeam;
				probabilityModelOfAction[i][0] = (1 - NAMBLA * lossGreedy) * pGreedy;
				probabilityModelOfAction[i][1] = (1 - NAMBLA * lossTeam) * pTeam;

				// normalize the table
				double pTot = probabilityModelOfAction[i][0] + probabilityModelOfAction[i][1];
				probabilityModelOfAction[i][0] /= pTot;
				probabilityModelOfAction[i][1] /= pTot;
			}

			printTable();
			previousState = currentState;
		}
	}

	// agentIndex is between 3 and nbrPredator+1
	public double modelProbablityOfAgent(AgentType predatorType, int agentIndex) {
		// TODO
		return probabilityModelOfAction[agentIndex - 3][0];
	}

	// helper
	public void printTable() {
		System.out.println();
		System.out.println("\t   G   T   U");
		for (int i = 0; i < nbrPredator - 1; i++) {
			System.out.print("[Agent " + (i + 3) + "] ");
			for (int j = 0; j < NBR_TYPE_PREDATOR; j++) {
				System.out.print(probabilityModelOfAction[i][j] + " ");
			}
			System.out.println();
		}
	}
}
