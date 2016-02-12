package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import gui.GameController;
import states.BlackBarState;
import states.BlackBearOffState;
import states.BlackState;
import states.GameState;
import states.RedBarState;
import states.RedState;
import states.RedBearOffState;

public class Board implements BoardSubject{
	private static Board instance = new Board();
	private Stack<Checker>[] points;
	
	private GameController gc = null;

	private GameState redState = new RedState();
	private GameState redBarState = new RedBarState();
	private GameState redBearOffState = new RedBearOffState();
	private GameState blackState = new BlackState();
	private GameState blackBarState = new BlackBarState();
	private GameState blackBearOffState = new BlackBearOffState();
	
	private List<Checker> redCheckers = new ArrayList<Checker>();
	private List<Checker> blackCheckers = new ArrayList<Checker>();
	
	private ArrayList<Observer> observers = new ArrayList<>();

	@Override
	public void registerObserver(Observer o) {
		observers.add(o);
	}

	@Override
	public void removeObserver(Observer o) {
		observers.remove(o);
	}

	@Override
	public void notifyNewChecker(Checker checker){
		for(Observer o: observers){
			o.drawChecker(checker, checker.color);
		}
	}

	@Override
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
		System.out.println(state.toString());
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
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < noOfRedCheckers[i]; j++) {
				Checker r = new Checker(Constant.RED);
				r.setPosition(redSetupPoints[i]);
				notifyNewChecker(r);
				points[redSetupPoints[i]].add(r);
				redCheckers.add(r);
			}
		}

		for (int i = 0; i < 4; i++) {
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
		detectBearOffState();
		System.out.println("from:" +fromPoint+"  to:"+toPoint);
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
			outOfBar();
			notifyPlayer(getState().getColor());
			return true;
		// valid move
		case 1:
			checker = points[fromPoint].pop();
			points[toPoint].add(checker);
			checker.setPosition(toPoint);
			outOfBar();
			return true;
		// Bear-off move exeeding bounds	
		case 2:
			checker = points[fromPoint].pop();
			points[state.getColor()].add(checker);
			checker.setPosition(toPoint);
			notifyMove(checker, toPoint);
			return true;
		default:
			return false;
		}

	}

	private void outOfBar() {
		if(state == getBlackBarstate() && points[Constant.BLACKBAR].isEmpty()){
			setState(getBlackState());
		}else if(state == getRedBarState() && points[Constant.REDBAR].isEmpty()){
			setState(getRedState());
		}
		
	}

	private void detectBearOffState() {
		boolean bearOff = true;
		if (state.getColor() == Constant.BLACK) {
			for(Checker c: blackCheckers){
				if(     c.getPosition()!=Constant.BLACK && 
						c.getPosition()!=Constant.BLACKBAR &&
						c.getPosition()>6){
					bearOff = false;
				}
			}
			if(bearOff){
				setState(getBlackBearOffState());
			}
		} else{
			for(Checker c: redCheckers){
				if(		c.getPosition()!=Constant.RED && 
						c.getPosition()!=Constant.REDBAR &&
						c.getPosition()<19 ){
					bearOff = false;
				}
			}
			if(bearOff){
				setState(getRedBearOffState());
			}
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
		if(state == getBlackBarstate()){
			return move(fromPoint, Constant.RED-die.getValue());
		}
		if(state == getRedBarState()){
			return move(fromPoint, die.getValue());
		}
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
		notifyPlayer(getState().getColor());
	}

	public boolean areThereValidMoves(Die die) {
		if(state == getBlackBarstate() && state.testMove(Constant.BLACKBAR, 25-die.getValue())!=-1){
			return true;
		}if(state == getRedBarState() && state.testMove(Constant.REDBAR, die.getValue())!=-1){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public void notifyPlayer(int player) {
		for(Observer o: observers){
			o.updatePlayer(player);
		}
		
	}
}
