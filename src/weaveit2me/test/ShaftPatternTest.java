package weaveit2me.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import weaveit2me.core.ShaftPattern;

public class ShaftPatternTest {

	ShaftPattern sp1, sp2, sp3;
	
	@Before
	public void setUp() throws Exception {
		sp1 = new ShaftPattern("odds");
		sp2 = new ShaftPattern("evens");
		sp2.addShaft(2);
		sp2.addShaft(4);
		sp3 = new ShaftPattern("empty");
	}

	@After
	public void tearDown() throws Exception {
		sp1 = null;
		sp2 = null;
		sp3 = null;
	}

	@Test
	public void testShaftPattern() {
		assertNotNull("Didn't construct", sp1);
		assertNotNull("Didn't construct", sp2);
		assertNotNull("Didn't construct", sp3);
	}

	@Test
	public void testGetName() {
		assertEquals("Name check", sp1.getName(), "odds");
		assertEquals("Name check", sp2.getName(), "evens");
		assertEquals("Name check", sp3.getName(), "empty");
	}

	@Test
	public void testAddShaft() {
		assertEquals("Beginning length", sp1.getIncludedShafts().size(), 0);
		sp1.addShaft(1);
		assertEquals("Length after add1", sp1.getIncludedShafts().size(), 1);
		sp1.addShaft(3);
		sp1.addShaft(5);
		sp1.addShaft(7);
		assertEquals("Length after add all", sp1.getIncludedShafts().size(), 4);
		sp1.addShaft(5);
		assertEquals("Length after duplicate all", sp1.getIncludedShafts().size(), 4);
	}

	@Test
	public void testRemoveShaft() {
		assertEquals("Beginning length", sp2.getIncludedShafts().size(), 2);
		sp2.removeShaft(4);
		assertEquals("After removal length", sp2.getIncludedShafts().size(), 1);
		assertFalse("Shaft is gone", sp2.includes(4));
		assertTrue("Shaft is present", sp2.includes(2));
		sp2.removeShaft(4);
		sp2.removeShaft(2);
		assertEquals("After both gone length", sp2.getIncludedShafts().size(), 0);
	}

	@Test
	public void testIncludes() {
		for (int i = 0; i<8; i++) {
			sp3.addShaft(i);
			assertTrue("Shaft includes "+i, sp3.includes(i));
		}
	}

	@Test
	public void testToBinaryStringInt() {
		assertEquals("Binary string of evens", sp2.toBinaryString(8), "00001010");
		assertEquals("Binary string of evens", sp2.toBinaryString(4), "1010");
		assertEquals("Binary string of evens", sp2.toBinaryString(10), "0000001010");
	}

	@Test
	public void testToBinaryString() {
		assertEquals("Binary string of evens", sp2.toBinaryString(), "1010");
		sp2.addShaft(5);
		assertEquals("Binary string of evens", sp2.toBinaryString(), "11010");
	}

	@Test
	public void testToString() {
		assertEquals("Evens to string", sp2.toString(), "evens (2 4)");
	}

	@Test
	public void testGetIncludedShafts() {
		List<Integer> ints = sp2.getIncludedShafts();
		assertEquals("Shaft count", ints.size(), 2);
		assertEquals("Includes correct shaft", (Integer) ints.get(0), new Integer(2));
		assertEquals("Includes correct shaft", (Integer) ints.get(1), new Integer(4));

	}

}
