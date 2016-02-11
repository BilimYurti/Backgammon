package game;

public interface GameSubject {
	public void registerObserver(Observer o);
	public void removeObserver(Observer o);
	public void notifyDiceStatus();
}
