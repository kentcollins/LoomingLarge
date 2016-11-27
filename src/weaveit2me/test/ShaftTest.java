package weaveit2me.test;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import weaveit2me.core.Shaft;

public class ShaftTest {

	Shaft s1;
	Shaft s2;
	
	@Before
	public void setUp() throws Exception {
		s1 = new Shaft(1);
		s2 = new Shaft(2);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetIdNum() {
		assertEquals("Did not match shaft number", s1.getIdNum(), 1);
		assertEquals("Did not match shaft number", s2.getIdNum(), 2);
	}

	@Test
	public void testToString() {
		assertEquals("String match error", s1.toString(), "1");
	}

	@Test
	public void testCompareTo() {
		assertEquals("Compare lower", s1.compareTo(s2), -1);
		assertEquals("Compare higher", s2.compareTo(s1), 1);
		assertEquals("Compare equal", s1.compareTo(s1), 0);
	}

}
