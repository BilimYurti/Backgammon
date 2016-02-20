package states;

import static states.MoveTestMethods.basicMoveTests;
import static states.MoveTestMethods.emptyOrOwnPoint;
import static states.MoveTestMethods.hit;
import static states.MoveTestMethods.inBounds;

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
