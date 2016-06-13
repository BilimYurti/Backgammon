package states;

import static states.MoveValidationMethods.*;

import game.Constant;

public class RedBearOffState implements GameState {

	public int testMove(int fromPoint, int toPoint) {
		if (ownChecker(fromPoint, getColor()) && direction(fromPoint, toPoint, getColor())) {
			if (inBearOffRange(toPoint, getColor())) {
				if (emptyOrOwnPoint(toPoint, getColor())) {
					return 1;
				} else if (hit(toPoint, getColor())) {
					return 0;
				} else
					return -1;
			} else if (toPoint > Constant.RED && !forcedMoves(toPoint, fromPoint, getColor())) {
				return 2;
			} else {
				return -1;
			}
		} else {
			return -1;
		}
	}

	public int getColor() {
		return Constant.RED;
	}
	
	@Override
	public String toString(){
		return "Red Bear-Off State";
	}

}
