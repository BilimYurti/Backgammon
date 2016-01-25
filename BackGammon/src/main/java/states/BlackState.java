package states;

import static states.SharedMoveTests.basicMoveTests;
import static states.SharedMoveTests.emptyOrOwnPoint;
import static states.SharedMoveTests.hit;
import static states.SharedMoveTests.inBounds;

import game.Constant;

public class BlackState implements GameState {

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
		return Constant.BLACK;
	}
	
	@Override
	public String toString(){
		return "Black State";
	}
}
