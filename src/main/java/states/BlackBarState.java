package states;

import static states.MoveValidationMethods.*;

import game.Constant;

public class BlackBarState implements GameState {

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
		return Constant.BLACK;
	}
	
	@Override
	public String toString(){
		return "Black Bar State";
	}

}
