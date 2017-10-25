package pl.piterpti.toolkit;

import org.junit.Test;

import junit.framework.TestCase;

public class OtherTests extends TestCase {

	@Test
	public void testMathFloorTest() {
		long l = 101;
		final int MAX_OUTCOMES_TO_DISPLAY = 10;
		
		String a = String.valueOf(((long)(Math.ceil((double)l / MAX_OUTCOMES_TO_DISPLAY))));
		assertEquals("11", a);
	}
	
}
