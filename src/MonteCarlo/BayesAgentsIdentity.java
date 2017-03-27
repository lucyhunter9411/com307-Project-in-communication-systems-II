package MonteCarlo;

import Enum.PredatorType;
import Main.State;

public class BayesAgentsIdentity {
	private final double NAMBLA = 0.5;
	private int nbrPredator;
	private State baseState;
	private State previousState;
	private double probabilityModelOfAction[][];
	public BayesAgentsIdentity(State initialState) {
		nbrPredator = initialState.getNbrAgents()-1;
		baseState = initialState.clone();
		previousState = initialState.clone();
		probabilityModelOfAction = new double[nbrPredator-1][PredatorType.values().length];
		
	}
	
	public void newStateInformation(State newState){
		
	}
	
	//agentIndex is between 3 and nbrPredator+1
	public double modelProbablityOfAgent(PredatorType predatorType, int agentIndex){
		//TODO
		return probabilityModelOfAction[agentIndex-3][0];
	}
}
