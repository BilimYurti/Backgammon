import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import game.Die;

public class DieTest {

	@Test
	public void testDieReturnsValidRandomValue() {
		Die die = new Die();
		die.roll();
		int value = die.getValue();
		
		assertTrue(value >= 1 && value <= 6);
	}
	
	@Test
	public void testDieContructorWithDieParameter(){
		Die d1 = new Die();
		d1.roll();
		Die d2 = new Die(d1);
		assertEquals(d1.getValue(), d2.getValue());
	}
}
