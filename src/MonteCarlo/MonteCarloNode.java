package MonteCarlo;

public abstract class MonteCarloNode {

	protected MonteCarloNode parentNode;
	protected double pointsEarned = 0;
	protected int nodeTry = 0;
	protected MonteCarloNode[] childsNode;

	public MonteCarloNode(MonteCarloNode parentNode) {
		this.parentNode = parentNode;
	}

	public double getPointsEarned() {
		return pointsEarned;
	}

	public int getNodeTry() {
		return nodeTry;
	}

	public void setWinner() {
		nodeTry++;
		pointsEarned++;
		if (parentNode != null) {
			parentNode.propagateWin();
		}
	}

	public void setLoser() {
		nodeTry++;
		pointsEarned--;
		if (parentNode != null) {
			parentNode.propagateLose();
		}
	}
	/*
	 * public void propagateWin() { nodeTry++; pointsEarned++; if (parentNode !=
	 * null) { parentNode.propagateWin(); } }
	 * 
	 * public void propagateLose() { nodeTry++; pointsEarned--; if (parentNode
	 * != null) { parentNode.propagateLose(); } }
	 */

	public void propagateWin() {
		nodeTry=0;
		double stack = 0;
		for (MonteCarloNode child : childsNode) {
			if (child != null) {
				stack += child.nodeTry * child.pointsEarned;
				nodeTry += child.nodeTry;
			}
		}
		pointsEarned = stack / nodeTry;
		if (parentNode != null) {
			parentNode.propagateWin();
		}
	}

	public void propagateLose() {
		nodeTry=0;
		double stack = 0;
		for (MonteCarloNode child : childsNode) {
			if (child != null) {
				stack += child.nodeTry * child.pointsEarned;
				nodeTry += child.nodeTry;
			}
		}
		pointsEarned = stack / nodeTry;
		if (parentNode != null) {
			parentNode.propagateLose();
		}
	}

	@Override
	public String toString() {
		if (this instanceof MonteCarloNodeS) {
			return "NodeS[w:" + pointsEarned + " t:" + nodeTry + "]";

		} else if (this instanceof MonteCarloNodeG) {
			return "NodeG[w:" + pointsEarned + " t:" + nodeTry + "]";

		}
		return "Node[w:" + pointsEarned + " t:" + nodeTry + "]";
	}
}
