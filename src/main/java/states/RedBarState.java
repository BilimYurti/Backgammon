package states;
import static states.SharedMoveTests.*;

import game.Constant;

public class RedBarState implements GameState {

	public int testMove(int fromPoint, int toPoint) {
		if(checkerFromBar(fromPoint, toPoint, getColor())){
			if(emptyOrOwnPoint(toPoint, getColor())){
				return 1;
			}
			else if(hit(toPoint, getColor())){
				return 0;
			}else
				return -1;
		}else
		return -1;
	}

	public int getColor() {
		return Constant.RED;
	}
	
	@Override
	public String toString(){
		return "Red Bar State";
	}
}
