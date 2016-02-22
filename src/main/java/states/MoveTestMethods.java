package states;

import java.util.List;
import java.util.Stack;

import game.Board;
import game.Checker;
import game.Constant;
import game.Die;

public class MoveTestMethods {
	private static Board board = Board.getInstance();
	private static Stack<Checker>[] points = board.getPoint();

	public static boolean basicMoveTests(int fromPoint, int toPoint, int stateColor) {
		boolean test = (ownChecker(fromPoint, stateColor) && 
						dieValue(fromPoint, toPoint) && 
						inBounds(toPoint) && 
						direction(fromPoint, toPoint, stateColor));
		return test ? true : false;
	}

	public static boolean dieValue(int fromPoint, int toPoint) {
		int value = Math.abs(toPoint - fromPoint);
		return (value > 0 && value < 7) ? true : false;
	}

	public static boolean inBounds(int toPoint) {
		boolean test = (toPoint > Constant.BLACK && toPoint < Constant.RED);
		return test ? true : false; 
	}

	public static boolean ownChecker(int fromPoint, int stateColor) {
		boolean test = (!points[fromPoint].empty() && points[fromPoint].peek().color == stateColor);
		return test ? true : false;
	}

	public static boolean direction(int fromPoint, int toPoint, int stateColor) {
		int steps = (fromPoint == Constant.REDBAR) ? toPoint : (toPoint - fromPoint);
		boolean test = (stateColor == Constant.RED && steps > 0 || stateColor == Constant.BLACK && steps < 0);
		return test ? true : false;
	}

	public static boolean emptyOrOwnPoint(int toPoint, int stateColor) {
		boolean test = (points[toPoint].isEmpty() || points[toPoint].peek().color == stateColor);
		return test ? true : false;
	}

	public static boolean hit(int toPoint, int stateColor) {
		boolean test = (points[toPoint].size() == 1 && points[toPoint].peek().color != stateColor);
		return test ? true : false;
	}

	public static boolean checkerFromBar(int fromPoint, int toPoint, int stateColor) {
		boolean test = (stateColor == Constant.RED && fromPoint == Constant.REDBAR && 
				        toPoint <= 6 && toPoint < Constant.REDBAR
				        || 
				        stateColor == Constant.BLACK && fromPoint == Constant.BLACKBAR && 
				        toPoint >= 19 && toPoint < Constant.BLACKBAR);
		return test ? true : false;
	}

	public static boolean inBearOffRange(int toPoint, int stateColor) {
		boolean test = (stateColor == Constant.RED && toPoint >= 20 && toPoint <= 25
						|| 
						stateColor == Constant.BLACK && toPoint >= 0 && toPoint <= 5);
		return test ? true : false;
	}

	public static boolean forcedMoves(int toPoint, int fromPoint, int stateColor) {
		int steps = Math.abs(toPoint - fromPoint);
		boolean toReturn = false;
		if (stateColor == Constant.RED) {
			for (int i = 25 - steps; i > 18; i--) {
				if (!points[i].empty() && points[i].peek().color == Constant.RED) {
					toReturn = true;
				}
			}
		} else {
			for (int i = steps; i < 7; i++) {
				if (!points[i].empty() && points[i].peek().color == Constant.BLACK) {
					toReturn = true;
				}
			}
		}
		return toReturn;
	}

	public static int positionOfOuterMostChecker(int stateColor) {
		int toReturn;
		if (stateColor == Constant.RED) {
			toReturn = 25;
			for (Checker c : board.getRedCheckers()) {
				if (c.getPosition() < toReturn) {
					toReturn = c.getPosition();
				}
			}
		} else {
			toReturn = 0;
			for (Checker c : board.getBlackCheckers()) {
				if (c.getPosition() > toReturn) {
					toReturn = c.getPosition();
				}
			}
		}
		return toReturn;
	}

	public static boolean possibleMoves(List<Die> dice) {
		if (board.getState() == board.getBlackBarstate()) {
			for (Die d : dice) {
				if (board.getState().testMove(Constant.BLACKBAR, 25 - d.getValue()) != -1) {
					return true;
				}
			}
		} else if (board.getState() == board.getRedBarState()) {
			for (Die d : dice) {
				if (board.getState().testMove(Constant.REDBAR, d.getValue()) != -1) {
					return true;
				}
			}
		} else if (board.getState() == board.getBlackState()) {
			for (Checker c : board.getBlackCheckers()) {
				for (Die d : dice) {
					if (board.getState().testMove(c.getPosition(), c.getPosition() - d.getValue()) != -1) {
						return true;
					}
				}
			}
		} else if (board.getState() == board.getRedState()) {
			for (Checker c : board.getRedCheckers()) {
				for (Die d : dice) {
					if (board.getState().testMove(c.getPosition(), d.getValue() + c.getPosition()) != -1) {
						return true;
					}
				}
			}
		} else if (board.getState() == board.getBlackBearOffState()) {
			for (Checker c : board.getBlackCheckers()) {
				for (Die d : dice) {
					if (board.getState().testMove(c.getPosition(), c.getPosition() - d.getValue()) != -1) {
						return true;
					}
				}
			}
		} else if (board.getState() == board.getRedBearOffState()) {
			for (Checker c : board.getRedCheckers()) {
				for (Die d : dice) {
					if (board.getState().testMove(c.getPosition(), c.getPosition() + d.getValue()) != -1) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
