package game;

import java.util.ArrayList;

public interface Observer {
	
	public void drawChecker(Checker checker, int color);
	
	public void moveChecker(Checker checker, int toPoint);
	
	public void drawDice(ArrayList<Die> dice);
	
	public void updatePlayer(int player);

	public void notifyNoMoves();
	
	public void notifyWinner(int player);
	
	public void countBearOff();
	
}
