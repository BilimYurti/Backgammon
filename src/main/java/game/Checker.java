package game;

public class Checker {

	public final int color;
	private int position;
	
	public Checker(int color) {
		this.color = color;
	}
	
	public void setPosition(int pos){
		System.out.println("Checker change position from "+getPosition()+" to "+pos);
		position = pos;
	}

	public int getPosition(){
		return position;
	}
}
