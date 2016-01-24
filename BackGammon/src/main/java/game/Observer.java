package game;

public interface Observer {
	
	public void drawChecker(Checker checker, int color);
	
	public void moveChecker(Checker checker, int toPoint);
	
}
