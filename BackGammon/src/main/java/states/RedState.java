package states;

import game.Constant;
import static states.SharedMoveTests.*;

public class RedState implements GameState {

	public int testMove(int fromPoint, int toPoint) {
		if(basicMoveTests(fromPoint, toPoint, getColor())){
			if(emptyOrOwnPoint(toPoint, getColor())){
				return 1;
			}else if(hit(toPoint, getColor())){
				return 0;
			}else{
				return -1;
			}
		}
		return -1;
	}

	public int getColor() {
		return Constant.RED;
	}

}
