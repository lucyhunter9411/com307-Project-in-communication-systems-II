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
		double initialRatio = 1 / NBR_TYPE_PREDATOR;
		for (int i = 0; i < nbrPredator - 1; i++) {
			for (int j = 0; j < NBR_TYPE_PREDATOR; j++) {
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
			previousState.printMapHelper();
			newState.printMapHelper();
			State currentState = newState.clone();
			// and compare if the agent moved to the right place
			ArrayList<Agent> agentTested = new ArrayList<Agent>();
			ArrayList<Direction> directionTested = new ArrayList<Direction>();
			
			// find the models of agents which could generate this new state
			// generate the 2^(nbrPredator-1) States
			final int agentTypePossibility = (int) Math.pow(2, nbrPredator - 1);
			for (int i = 0; i < agentTypePossibility; i++) {
				// update the agentsGreddy and agentsTeam to the last state
				for(int k = 0; k<nbrPredator-1;k++){
					int newPosX = previousState.getAgentsCoordinateList()[k+2][0];
					int newPosY = previousState.getAgentsCoordinateList()[k+2][1];
					agentsGreedy.get(k).setPos(newPosX, newPosY);
					agentsTeam.get(k).setPos(newPosX, newPosY);
				}
				State copiedState = previousState.clone();
				agentTested.clear();
				directionTested.clear();
				// select the current agents in function of i to cover all possibility, set them to position
				//using the binary value of i
				final boolean[] ret = new boolean[nbrPredator-1];
			    for (int j = 0; j < nbrPredator-1; j++) {
			        ret[nbrPredator - 2 - j] = (1 << j & i) != 0;
			    }
			    for(int j = 0; j < nbrPredator-1;j++){
			    	if(ret[j]){
			    		agentTested.add(agentsGreedy.get(j));
			    	}
			    	else{
			    		agentTested.add(agentsTeam.get(j));
			    	}
			    }
			    //iterate the agent
				for (Agent a : agentTested) {
					directionTested.add(a.iterate(copiedState));
				}
				// add the dummy agent which replaces the MTC
				agentTested.add(0, new GreedyPredator(previousState, 2, 0));
				directionTested.add(0, previousMTCDirection);
				// TODO add a fake prey
				agentTested.add(0, new Prey(previousState, 1, 0));
				directionTested.add(0, Direction.LEFT);
				System.out.println();
				System.out.println(agentTested);
				System.out.println(directionTested);
				copiedState.modifyState(directionTested, agentTested);
				copiedState.printMapHelper();
				// TODO result working?
			}
			previousState = currentState;
		}
	}

	// agentIndex is between 3 and nbrPredator+1
	public double modelProbablityOfAgent(AgentType predatorType, int agentIndex) {
		// TODO
		return probabilityModelOfAction[agentIndex - 3][0];
	}
}
