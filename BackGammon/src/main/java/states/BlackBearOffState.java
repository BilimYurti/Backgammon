package states;

import game.Constant;
import static states.SharedMoveTests.*;

public class BlackBearOffState implements GameState {

	public int testMove(int fromPoint, int toPoint) {
		if (ownChecker(fromPoint, getColor())) {
			if (inBearOffRange(toPoint, getColor())) {
				if (emptyOrOwnPoint(toPoint, getColor())) {
					return 1;
				} else if (hit(toPoint, getColor())) {
					return 0;
				} else
					return -1;
			} else if (toPoint < Constant.BLACK && !forcedMoves(toPoint, fromPoint, getColor())) {
				return 2;
			} else {
				return -1;
			}
		} else {
			return -1;
		}
	}

	public int getColor() {
		return Constant.BLACK;
	}

}
