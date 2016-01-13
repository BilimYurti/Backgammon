package game;
import java.util.Random;

public class Die {
	private int value;

	public Die(Die d1) {
		this.value = d1.getValue();
	}

	public Die() {}

	public int getValue() {
		return value;
	}

	public void roll() {
		value = new Random().nextInt(6)+1;
	}
	
	

}
