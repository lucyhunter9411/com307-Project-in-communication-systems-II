package MonteCarlo;

import Enum.PredatorType;
import Main.State;

public class BayesAgentsIdentity {
	private int nbrPredator;
	public BayesAgentsIdentity(State initialState) {
		nbrPredator = initialState.getNbrAgents()-1;
	}
	
	public void newStateInformation(State newState){
		
	}
	
	public double modelProbablityOfAgent(PredatorType predatorType, int agentIndex){
		return 0;
	}
}
