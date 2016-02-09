package states;

public interface GameState {
	
	/**
	 * Test a move from point A to point B
	 * @param fromPoint
	 * @param toPoint
	 * @return -1 for invalid move, 0 for hit, 1 for valid move
	 */
	public int testMove(int fromPoint, int toPoint);
	
	public int getColor();
	
	public String toString();

}
