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
		}
		return false;
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
					if(dice.isEmpty()){
						board.nextPlayer();
					}
					return true;
				}
			}
		}
		
		if(board.getState() == board.getBlackBearOffState() || board.getState() == board.getRedBearOffState()){
			dice.sort(new Comparator<Die>() {

				@Override
				public int compare(Die o1, Die o2) {
					return o1.getValue()-o2.getValue();
				}
			});
			
			for(int i = 0; i<dice.size(); i++){
				if(steps < dice.get(i).getValue() && board.move(fromPos, toPos)){
					dice.remove(dice.get(i));
					notifyDiceStatus();
					if(dice.isEmpty()){
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
