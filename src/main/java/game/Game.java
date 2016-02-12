package game;

import java.util.ArrayList;
import java.util.Comparator;

import com.sun.glass.ui.TouchInputSupport;

import states.SharedMoveTests;

public class Game implements GameSubject{
	
	private ArrayList<Observer> observers = new ArrayList<>();
	private ArrayList<Die> dice = new ArrayList<>();
	private int player = Constant.BLACK;
	private boolean rolled = false;
	private Board board = Board.getInstance();
	
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
	
	public void removeDice(){
		dice.clear();
		notifyDiceStatus();
	}
	
	private void notifyNoMoves() {
		for(Observer o: observers){
			o.notifyNoMoves();
		}
	}
	
	public void checkPossibleMoves(){
		if(!possibleMoves()){
			notifyNoMoves();
			board.nextPlayer();
		}
	}

	private boolean possibleMoves() {
		if(board.getState() == board.getBlackBarstate()){
			for(Die d:dice){
				if(board.getState().testMove(Constant.BLACKBAR, 25-d.getValue()) != -1){
					return true;
				}
			}
		}else if(board.getState() == board.getRedBarState()){
			for(Die d:dice){
				if(board.getState().testMove(Constant.REDBAR, d.getValue()) != -1){
					return true;
				}
			}
		}else if(board.getState() == board.getBlackState()){
			for(Checker c: board.getBlackCheckers()){
				for(Die d: dice){
					if(board.getState().testMove(c.getPosition(), c.getPosition()-d.getValue()) != -1){
						return true;
					}
				}
			}
		}else if(board.getState() == board.getRedState()){
			for(Checker c: board.getRedCheckers()){
				for(Die d: dice){
					if(board.getState().testMove(c.getPosition(), d.getValue()+c.getPosition()) != -1){
						return true;
					}
				}
			}
		}else if(board.getState() == board.getBlackBearOffState()){
			for(Checker c: board.getBlackCheckers()){
				for(Die d: dice){
					if(board.getState().testMove(c.getPosition(), c.getPosition()-d.getValue())!= -1){
						return true;
					}
				}
			}
		}else if(board.getState() == board.getRedBearOffState()){
			for(Checker c: board.getRedCheckers()){
				for(Die d: dice){
					if(board.getState().testMove(c.getPosition(), c.getPosition()+d.getValue()) != -1){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private void checkWinner(){
		System.out.println("Checkwinner");
		if(board.getState().getColor() == Constant.BLACK){
			for(Checker c: board.getBlackCheckers()){
				if(c.getPosition() != Constant.BLACK){
					return;
				}
			}
		}else if(board.getState().getColor() == Constant.RED){
			for(Checker c: board.getRedCheckers()){
				if(c.getPosition() != Constant.RED){
					System.out.println(c.getPosition());
					return;
				}
			}
		}
		notifyWinner();
		
	}
	
	private void notifyWinner() {
		for(Observer o: observers){
			o.notifyWinner(board.getState().getColor());
		}
		
	}

	public boolean move(int fromPos, int toPos){
		if(!SharedMoveTests.direction(fromPos, toPos, board.getState().getColor())){
			return false;
		}
		System.out.println("\n"+fromPos+" "+toPos);
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
		
		boolean availableMovesInBearOff = false;
		for(Die d: dice){
			int toPoint = (board.getState().getColor() == Constant.BLACK)? fromPos -d.getValue() : fromPos+d.getValue();
			if(SharedMoveTests.forcedMoves(toPoint, fromPos, board.getState().getColor())){
				availableMovesInBearOff = true;
			}
		}
		System.out.println(availableMovesInBearOff);
		if((board.getState() == board.getBlackBearOffState() && !availableMovesInBearOff) || 
				(board.getState() == board.getRedBearOffState() && !availableMovesInBearOff)){
			dice.sort(new Comparator<Die>() {

				@Override
				public int compare(Die o1, Die o2) {
					return o1.getValue()-o2.getValue();
				}
			});
			int outermostChecker = SharedMoveTests.positionOfOuterMostChecker(board.getState().getColor());
			System.out.println(outermostChecker);
			for(Checker c: board.getBlackCheckers())
			for(int i = 0; i<dice.size(); i++){
				if(steps < dice.get(i).getValue() && fromPos == outermostChecker && board.move(fromPos, toPos)){
					dice.remove(dice.get(i));
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
		return false;
	}

	@Override
	public void registerObserver(Observer o) {
		observers.add(o);
	}

	@Override
	public void removeObserver(Observer o) {
		observers.remove(0);
	}

	@Override
	public void notifyDiceStatus() {
		System.out.print("Dice: ");
		for(Die d: dice){
			System.out.print(d.getValue()+" ");
		}
		System.out.flush();
		for(Observer o: observers){
			o.drawDice(dice);
		}
		
	}

}
