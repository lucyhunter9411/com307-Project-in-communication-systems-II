package MonteCarlo;

public abstract class MonteCarloNode {

	private MonteCarloNode parentNode;
	private final int depth;
	protected double pointsEarned = 0;
	protected int nodeTry = 0;

	public MonteCarloNode( MonteCarloNode parentNode, int depth) {
		this.depth = depth;
		this.parentNode = parentNode;
	}
	

	public void setWinner(){
		nodeTry++;
		pointsEarned++;
		if (parentNode != null) {
			parentNode.propagateWin();
		}
	}
	
	public void setLoser(){
		nodeTry++;
		pointsEarned--;
		if (parentNode != null) {
			parentNode.propagateLose();
		}
	}
	
	public void propagateWin() {
		nodeTry++;
		pointsEarned++;
		if (parentNode != null) {
			parentNode.propagateWin();
		}
	}

	public void propagateLose() {
		nodeTry++;
		pointsEarned--;
		if (parentNode != null) {
			parentNode.propagateLose();
		}
	}
	/*
	 * public void propagateWin() {
		nodeTry++;
		double stack=0;
		for(MonteCarloNodeS child: childsNode){
			if(child!=null){
				stack+=child.nodeTry*child.pointsEarned;
			}
		}
		pointsEarned = stack/nodeTry;
		if (parentNode != null) {
			parentNode.propagateWin();
		}
	}

	public void propagateLose() {
		nodeTry++;
		double stack=0;
		for(MonteCarloNodeS child: childsNode){
			if(child!=null){
				stack+=child.nodeTry*child.pointsEarned;
			}
		}
		pointsEarned = stack/nodeTry;
		if (parentNode != null) {
			parentNode.propagateLose();
		}
	}
	 */
	
	@Override
	public String toString() {
		return "Node[w:" + pointsEarned + " t:" + nodeTry + " depth:" + depth + "]";
	}

	public int getDepth() {
		return depth;
	}
}
