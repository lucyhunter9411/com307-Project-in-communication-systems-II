public class GreedyPredator implements Agent{

	private final int agentIndex;
	private int posX, posY;
	public GreedyPredator(int x, int y, int agentIndex){
		posX = x;
		posY = y;
		this.agentIndex = agentIndex;
	}
	
	public int getPosY() {
		return posY;
	}

	public int getPosX() {
		return posX;
	}
	
	public int iterate(State state, RandomSeededDouble r) {
		int width = state.getMapWidth();
		int height = state.getMapHeight();
		int preyX = state.getPreyPosX();
		int preyY = state.getPreyPosY();
		int dx = (preyX - posX + width) % width;
		int dy = (preyY - posY + height) % height;
		//If already neighboring the prey, try to move onto the prey so that if it moves, the predator will follow.
		if(dx==0 && dy==1){
			return 4;
		}
		if(dx==1 && dy==0){
			return 3;
		}
		if(dx==0 && dy==height-1){
			return 2;
		}
		if(dx==width-1 && dy==0){
			return 1;
		}
		// Choose the nearest unoccupied cell neighboring the prey as the destination.
		if(!state.isPreyCaptured()){
			int destinationX = preyX;
			int destinationY = preyY;
			int nearestEmptyCellDirection = findNearestEmptyCell(state,dx,dy,width,height);
			switch(nearestEmptyCellDirection){
			case 1: destinationX = (preyX - 1 + width) % width; break;
			case 2: destinationY = (preyY - 1 + height) % height; break;
			case 3: destinationX = (preyX + 1) % width; break;
			case 4: destinationY = (preyY + 1) % height; break;
			default: break;
			}
			dx = (destinationX - posX + width) % width;
			dy = (destinationY - posY + height) % height;
			if(dx>width/2){
				dx = dx - width;
			}
			if(dy>height/2){
				dy = dy - height;
			}
			//System.out.println("dx "+dx+" dy "+dy+" posX "+posX+" posY "+posY );
			// Let d = dimmax. If md is not blocked, take it.
			if(Math.abs(dy)>Math.abs(dx)){
				if(dy<0){
					if(!state.isTopBlocked(posX, posY)){
						return 2;
					}
				}
				else{
					if(!state.isBottomBlocked(posX, posY)){
						return 4;
					}
				}
			}
			else{
				if(dx<0){
					if(!state.isLeftBlocked(posX, posY)){
						return 1;
					}
				}
				else{
					if(!state.isRightBlocked(posX, posY)){
						return 3;
					}
				}
			}
			// Let d = dimmin. If md is not blocked, take it.
			if(Math.abs(dy)<Math.abs(dx)){
				if(dy<0){
					if(!state.isTopBlocked(posX, posY)){
						return 2;
					}
				}
				else{
					if(!state.isBottomBlocked(posX, posY)){
						return 4;
					}
				}
			}
			else{
				if(dx<0){
					if(!state.isLeftBlocked(posX, posY)){
						return 1;
					}
				}
				else{
					if(!state.isRightBlocked(posX, posY)){
						return 3;
					}
				}
			}
		}
		// Otherwise, move randomly.
		double randomDouble = r.generateDouble();
		return (int)(randomDouble * 4) + 1;
	}
	//TODO
	private int findNearestEmptyCell(State state, int dx, int dy, int width, int height) {
		int x = state.getPreyPosX();
		int y = state.getPreyPosY();
		if(!state.isLeftBlocked(x, y)){
			return 1;
		}
		if(!state.isTopBlocked(x, y)){
			return 2;
		}
		if(!state.isRightBlocked(x, y)){
			return 3;
		}
		if(!state.isBottomBlocked(x, y)){
			return 4;
		}
		return 0;
	}

	public void setPos(int newPosX, int newPosY) {
		posX = newPosX;
		posY = newPosY;		
	}

	public int getAgentIndex() {
		return agentIndex;
	}
}