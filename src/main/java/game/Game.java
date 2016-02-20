package game;

import java.util.ArrayList;
import java.util.Comparator;

import states.MoveTestMethods;

public class Game implements GameSubject{
	
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	private ArrayList<Die> dice = new ArrayList<Die>();
	private int player = Constant.BLACK;
	private boolean rolled = false;
	private Board board = Board.getInstance();
	
	public void registerObserver(Observer o) {
		observers.add(o);
	}

	public void removeObserver(Observer o) {
		observers.remove(0);
	}

	public ArrayList<Die> getDice(){
		return dice;
	}
	
	public int getPlayer(){
		return player;
	}
	
	public boolean roll(){
		if(!rolled){
			dice.clear();
			dice.add(new Die());
			dice.add(new Die());
			dice.get(0).roll();
			dice.get(1).roll();
			if(dice.get(0).getValue() == dice.get(1).getValue()){
				dice.add(new Die(dice.get(0)));
				dice.add(new Die(dice.get(0)));
			}
			notifyDiceStatus();
			checkPossibleMoves();
		}
		return false;
	}
	
	public void checkPossibleMoves(){
		if(!MoveTestMethods.possibleMoves(dice)){
			notifyNoMoves();
			board.nextPlayer();
		}
	}

	public boolean move(int fromPos, int toPos){
		if(!MoveTestMethods.direction(fromPos, toPos, board.getState().getColor())){
			return false;
		}
		int steps = fromPos - toPos;
		if(steps<0){
			steps *= -1;
		}
		else if(fromPos == Constant.REDBAR){
			steps = toPos;
		}
		else if(fromPos == Constant.BLACKBAR){
			steps = 25-toPos;
		}
		for(Die d: dice){
			if(d.getValue() == steps){
				if(board.move(fromPos, toPos)){
					dice.remove(d);
					notifyDiceStatus();
					checkWinner();
					if(!dice.isEmpty()){
						checkPossibleMoves();
					}else{
						board.nextPlayer();
					}
					return true;
				}
			}
		}
		
		if (board.getState() == board.getBlackBearOffState() || board.getState() == board.getRedBearOffState()) {
			boolean availableMovesInBearOff = false;
			for (Die d : dice) {
				int toPoint = (board.getState().getColor() == Constant.BLACK) ? fromPos - d.getValue()
						: fromPos + d.getValue();
				if (MoveTestMethods.forcedMoves(toPoint, fromPos, board.getState().getColor())) {
					availableMovesInBearOff = true;
				}
			}
			if (!availableMovesInBearOff) {
				dice.sort(new Comparator<Die>() {

					public int compare(Die o1, Die o2) {
						return o1.getValue() - o2.getValue();
					}
				});
				int outermostChecker = MoveTestMethods.positionOfOuterMostChecker(board.getState().getColor());
				for (int i = 0; i < dice.size(); i++) {
					if (steps < dice.get(i).getValue() && fromPos == outermostChecker && board.move(fromPos, toPos)) {
						dice.remove(dice.get(i));
						notifyDiceStatus();
						checkWinner();
						if (!dice.isEmpty()) {
							checkPossibleMoves();
						} else {
							board.nextPlayer();
						}
						return true;
					}
				}
			}
		}
		return false;
	}

	public void removeDice(){
		dice.clear();
		notifyDiceStatus();
	}

	private void checkWinner(){
		if(board.getState().getColor() == Constant.BLACK){
			for(Checker c: board.getBlackCheckers()){
				if(c.getPosition() != Constant.BLACK){
					return;
				}
			}
		}else if(board.getState().getColor() == Constant.RED){
			for(Checker c: board.getRedCheckers()){
				if(c.getPosition() != Constant.RED){
					return;
				}
			}
		}
		notifyWinner();
	}

	public void notifyDiceStatus() {
		for(Observer o: observers){
			o.drawDice(dice);
			o.countBearOff();
		}
		
	}

	private void notifyNoMoves() {
		for(Observer o: observers){
			o.notifyNoMoves();
		}
	}

	private void notifyWinner() {
		for(Observer o: observers){
			o.notifyWinner(board.getState().getColor());
		}
		
	}

}
