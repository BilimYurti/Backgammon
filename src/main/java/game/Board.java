package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import states.BlackBarState;
import states.BlackBearOffState;
import states.BlackState;
import states.GameState;
import states.RedBarState;
import states.RedBearOffState;
import states.RedState;

public class Board implements BoardSubject{
	private static Board instance = new Board();
	private Stack<Checker>[] points;
	
	private GameState redState = new RedState();
	private GameState redBarState = new RedBarState();
	private GameState redBearOffState = new RedBearOffState();
	private GameState blackState = new BlackState();
	private GameState blackBarState = new BlackBarState();
	private GameState blackBearOffState = new BlackBearOffState();
	
	private List<Checker> redCheckers = new ArrayList<Checker>();
	private List<Checker> blackCheckers = new ArrayList<Checker>();
	
	private ArrayList<Observer> observers = new ArrayList<Observer>();

	public void registerObserver(Observer o) {
		observers.add(o);
	}

	public void removeObserver(Observer o) {
		observers.remove(o);
	}

	public void notifyNewChecker(Checker checker){
		for(Observer o: observers){
			o.drawChecker(checker, checker.color);
		}
	}

	public void notifyMove(Checker checker,int toPoint) {
		for(Observer o: observers){
			o.moveChecker(checker, toPoint);
		}
	}

	public List<Checker> getRedCheckers() {
		return redCheckers;
	}
	
	public List<Checker> getBlackCheckers() {
		return blackCheckers;
	}

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
		notifyPlayer(state.getColor());
		this.state = state;
	}

	public Stack<Checker>[] getPoint() {
		return points;
	}

	public void setUp() {
		int[] redSetupPoints = new int[] { 1, 12, 17, 19 };
		int[] noOfRedCheckers = new int[] { 2, 5, 3, 5 };
		int[] blackSetupPoints = new int[] { 6, 8, 13, 24 };
		int[] noOfBlackCheckers = new int[] { 5, 3, 5, 2 };
		createAndPlaceCheckers(redSetupPoints, noOfRedCheckers, blackSetupPoints, noOfBlackCheckers);
	}
	
	public void createAndPlaceCheckers(int[] redSetupPoints, int[] noOfRedCheckers, int[] blackSetupPoints, int[] noOfBlackCheckers){
		redCheckers.clear();
		blackCheckers.clear();
		for (Stack<Checker> s : points) {
			s.clear();
		}
		for (int i = 0; i < redSetupPoints.length; i++) {
			for (int j = 0; j < noOfRedCheckers[i]; j++) {
				Checker r = new Checker(Constant.RED);
				r.setPosition(redSetupPoints[i]);
				notifyNewChecker(r);
				points[redSetupPoints[i]].add(r);
				redCheckers.add(r);
			}
		}

		for (int i = 0; i < blackSetupPoints.length; i++) {
			for (int j = 0; j < noOfBlackCheckers[i]; j++) {
				Checker b = new Checker(Constant.BLACK);
				b.setPosition(blackSetupPoints[i]);
				notifyNewChecker(b);
				points[blackSetupPoints[i]].add(b);
				blackCheckers.add(b);
			}
		}
	}

	public boolean move(int fromPoint, int toPoint) {
		Checker checker;
		switch (state.testMove(fromPoint, toPoint)) {
		// invalid move
		case -1:
			return false;
		// hit
		case 0:
			Checker hitChecker = points[toPoint].pop();
			notifyMove(hitChecker, opponentBar());
			points[opponentBar()].add(hitChecker);
			hitChecker.setPosition(opponentBar());
			checker = points[fromPoint].pop();
			checker.setPosition(toPoint);
			notifyMove(checker, toPoint);
			points[toPoint].add(checker);
			offBar();
			notifyPlayer(getState().getColor());
			detectBearOffState();
			return true;
		// valid move
		case 1:
			checker = points[fromPoint].pop();
			points[toPoint].add(checker);
			checker.setPosition(toPoint);
			offBar();
			detectBearOffState();
			return true;
		// Bear-off move exeeding bounds	
		case 2:
			checker = points[fromPoint].pop();
			points[state.getColor()].add(checker);
			checker.setPosition(toPoint);
			notifyMove(checker, toPoint);
			detectBearOffState();
			return true;
		default:
			return false;
		}

	}

	private void offBar() {
		if(state == getBlackBarstate() && points[Constant.BLACKBAR].isEmpty()){
			setState(getBlackState());
		}else if(state == getRedBarState() && points[Constant.REDBAR].isEmpty()){
			setState(getRedState());
		}
		
	}

	private void detectBearOffState() {
		boolean bearOff = true;
		List<Checker> testList = state.getColor() == Constant.BLACK ? blackCheckers : redCheckers;
		int min = state.getColor() == Constant.BLACK ? 0 : 19;
		int max = state.getColor() == Constant.BLACK ? 6 : 25;
		for(Checker c: testList){
			if(c.getPosition()<min || c.getPosition()>max){
				bearOff = false;
			}
		}
		if(bearOff){
			GameState st = state.getColor() == Constant.BLACK ? blackBearOffState : redBearOffState;
			setState(st);
		}
	}

	private int opponentBar() {
		return (state.getColor() == Constant.BLACK) ? Constant.REDBAR : Constant.BLACKBAR;
	}

	public boolean move(int fromPoint, Die die) {
		int toPoint;
		if (state == getBlackBarstate() || state == getRedBarState()) {
			return state == getBlackBarstate() ? move(fromPoint, Constant.RED - die.getValue())
					: move(fromPoint, die.getValue());
		} else {
			toPoint = (state.getColor() == Constant.BLACK) ? (fromPoint - die.getValue())
					: (fromPoint + die.getValue());
			return move(fromPoint, toPoint);
		}
	}

	public void nextPlayer() {
		GameState nextState;
		if (state.getColor() == Constant.RED) {
			nextState = (!points[Constant.BLACKBAR].empty()) ? blackBarState : blackState;
		} else {
			nextState = (!points[Constant.REDBAR].empty()) ? redBarState : redState;
		}
		setState(nextState);
		detectBearOffState();
		notifyPlayer(getState().getColor());
	}

	public void notifyPlayer(int player) {
		for(Observer o: observers){
			o.updatePlayer(player);
		}
		
	}
}
