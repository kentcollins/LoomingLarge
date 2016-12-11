package weaveit2me.core;

public class Loom {

	private int numShafts;
	private TieUp[] treadles;

	public Loom(int numShafts, int numTreadles) {
		this.numShafts = numShafts;
		treadles = new TieUp[numTreadles];
	}

	public int getNumShafts() {
		return numShafts;
	}
	
	public TieUp[] getTreadles() {
		return treadles;
	}

	public static void main(String[] args) {
		Loom loom = new Loom(10, 4);
		System.out.println("Found " + loom.getNumShafts() + " shafts.");
		TieUp t1 = new TieUp(loom);
		t1.tieUp(7);
		t1.tieUp(2);
		System.out.println(t1);
		t1.tieUp(10);
		t1.untie(2);
		System.out.println(t1.toBinaryString());
		for (int i = 7; i >= 0; i--) {
			t1.tieUp(i);
			System.out.println(t1);
		}
		System.out.println(t1.toBinaryString());
	}
}
