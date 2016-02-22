package game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
			rolled = true;
			notifyDiceStatus();
			checkPossibleMoves();
			return true;
		}
		return false;
	}
	
	public void checkPossibleMoves(){
		if(!MoveTestMethods.possibleMoves(dice)){
			notifyNoMoves();
			board.nextPlayer();
			rolled = false;
		}
	}

	public boolean move(int fromPos, int toPos){
		int steps;
		if(fromPos == Constant.REDBAR || fromPos == Constant.BLACKBAR){
			steps = (fromPos == Constant.REDBAR) ? toPos : 25-toPos;
		}else{
			steps = Math.abs(fromPos - toPos);
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
						rolled = false;
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
							rolled = false;
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

	public boolean checkWinner(){
		List<Checker> testList =  board.getState().getColor() == Constant.BLACK ? board.getBlackCheckers() : board.getRedCheckers();
		int home = board.getState().getColor() == Constant.BLACK ? Constant.BLACK : Constant.RED;
		for (Checker c : testList) {
			if (c.getPosition() != home) {
				return false;
			}
		}
		notifyWinner();
		return true;
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
