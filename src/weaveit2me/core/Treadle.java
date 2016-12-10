package weaveit2me.core;

public class Treadle {
	Loom loom;
	boolean[] shafts;

	public Treadle(Loom loom) {
		this.loom = loom;
		shafts = new boolean[loom.getNumShafts()];
	}

	/**
	 * Add the indicated shaft to this treadle
	 * 
	 * @param shaft
	 *            the number of the shaft
	 */
	public void tieUp(int shaft) {
		if (shaft < shafts.length && shaft >= 0)
			shafts[shaft] = true;
	}

	/**
	 * Remove the indicated shaft from this treadle
	 * 
	 * @param shaft
	 *            the number of the shaft
	 */
	public void untie(int shaft) {
		if (shaft < shafts.length && shaft >= 0)
			shafts[shaft] = false;
	}

	/**
	 * Determine if the specified shaft is set to lift
	 * 
	 * @param shaft
	 * @return whether this shaft should lift
	 */
	public boolean isTied(int shaft) {
		return shafts[shaft];
	}

	/**
	 * Expresses this pattern as a string of binary values.
	 * 
	 * @param numShafts
	 *            the number of shaft places to include in presentation.
	 * @return a binary string where 1's indicate a lifted shaft
	 */
	public String toBinaryString() {
		String s = "";
		for (int i = 0; i < shafts.length; i++) {
			if (shafts[i])
				s = 1 + s;
			else
				s = 0 + s;
		}
		return s;
	}
	
	public String toString() {
		return this.toBinaryString();
	}

	/**
	 * Get a boolean array indicated for each shaft whether it should be lifted
	 * when this treadle is activated.
	 * 
	 * @return a boolean array
	 */
	public boolean[] getShafts() {
		return shafts;
	}

}
