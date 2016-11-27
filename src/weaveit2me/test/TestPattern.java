package weaveit2me.test;

import weaveit2me.core.Pattern;
import weaveit2me.core.Repeat;

public class TestPattern {
	
	public static Pattern generateTestPattern() {
		Repeat r1 = new Repeat("Example Repeat A");
		r1.addShaftLiftPattern(1);
		r1.addShaftLiftPattern(2);
		r1.addShaftLiftPattern(4);
		r1.insertShaftLiftPattern(2, 3);
		Integer[] arr1 = {126, 127, 128};
		Repeat r2 = new Repeat("Example Repeat B",arr1);
		r2.addShaftLiftPattern(1);
		r2.addShaftLiftPattern(2);
		r2.addShaftLiftPattern(4);
		r2.addShaftLiftPattern(8);
		r2.addShaftLiftPattern(16);
		r2.addShaftLiftPattern(32);
		r2.addShaftLiftPattern(64);
		r2.addShaftLiftPattern(128);
		r2.addShaftLiftPattern(256);
		Pattern p = new Pattern("Test Some Repeats", 8);
		p.addRepeat(r1);
		p.addRepeat(r2);
		//TODO handle an empty repeat
		p.addRepeat(new Repeat("Example Repeat C", 8));
		return p;		
	}
}
