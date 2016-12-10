package weaveit2me.core;

import java.util.ArrayList;
import java.util.List;

/**
 * An ordered sequence of treadles.
 *
 */
public class Pattern {

	private List<Treadle> sequence;
	private int currIndex;

	public Pattern() {
		this(new ArrayList<Treadle>());
	}

	public Pattern(List<Treadle> seq) {
		this.sequence = seq;
		currIndex = 0;
	}

	public void addTreadle(Treadle t) {
		if (t != null)
			sequence.add(t);
	}

	public void deleteAtIndex(int i) {
		sequence.remove(i);
	}

}
