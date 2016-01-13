package game;

import java.util.Stack;

import states.BlackBarState;
import states.BlackBearOffState;
import states.BlackState;
import states.GameState;
import states.RedBarState;
import states.RedState;
import states.RedBearOffState;

public class Board {
	private static Board instance = new Board();
	private Stack<Checker>[] points;

	private GameState redState = new RedState();
	private GameState redBarState = new RedBarState();
	private GameState redBearOffState = new RedBearOffState();
	private GameState blackState = new BlackState();
	private GameState blackBarState = new BlackBarState();
	private GameState blackBearOffState = new BlackBearOffState();

	public GameState getRedState() {
		return redState;
	}

	public GameState getRedBarState() {
		return redBarState;
	}

	public GameState getRedBearOffState() {
		return redBearOffState;
	}

	public GameState getBlackState() {
		return blackState;
	}

	public GameState getBlackBarstate() {
		return blackBarState;
	}

	public GameState getBlackBearOffState() {
		return blackBearOffState;
	}

	private GameState state;

	public static Board getInstance() {
		return instance;
	}

	private Board() {
		points = new Stack[28];
		for (int i = 0; i < points.length; i++) {
			points[i] = new Stack<Checker>();
		}
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public Stack<Checker>[] getPoint() {
		return points;
	}

	public void setUp() {
		for (Stack<Checker> s : points) {
			s.clear();
		}
		int[] redSetupPoints = new int[] { 1, 12, 17, 19 };
		int[] redCheckers = new int[] { 2, 5, 3, 5 };
		int[] blackSetupPoints = new int[] { 6, 8, 13, 24 };
		int[] blackCheckers = new int[] { 5, 3, 5, 2 };

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < redCheckers[i]; j++) {
				points[redSetupPoints[i]].add(new Checker(Constant.RED));
			}
		}

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < blackCheckers[i]; j++) {
				points[blackSetupPoints[i]].add(new Checker(Constant.BLACK));
			}
		}
	}

	public boolean move(int fromPoint, int toPoint) {
		switch (state.testMove(fromPoint, toPoint)) {
		// invalid move
		case -1:
			return false;
		// hit
		case 0:
			points[opponentBar()].add(points[toPoint].pop());
			points[toPoint].add(points[fromPoint].pop());
			return true;
		// valid move
		case 1:
			points[toPoint].add(points[fromPoint].pop());
			return true;
		// Bear-off move exeeding bounds	
		case 2:
			points[state.getColor()].add(points[fromPoint].pop());
			return true;
		default:
			return false;
		}

	}

	private int opponentBar() {
		if (state.getColor() == Constant.BLACK) {
			return Constant.REDBAR;
		} else
			return Constant.BLACKBAR;
	}

	public boolean move(int fromPoint, Die die) {
		int toPoint;
		if (state.getColor() == Constant.BLACK) {
			toPoint = fromPoint - die.getValue();
		} else {
			toPoint = fromPoint + die.getValue();
		}
		return move(fromPoint, toPoint);
	}

	public void nextPlayer() {
		if (state.getColor() == Constant.RED) {
			if(!points[Constant.BLACKBAR].empty()){
				setState(blackBarState);
			}else{
				setState(blackState);
			}
		} else {
			if(!points[Constant.REDBAR].empty()){
				setState(redBarState);
			}else{
				setState(redState);
			}
		}
	}
}
