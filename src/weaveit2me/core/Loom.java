package weaveit2me.core;

public class Loom {

	private int numShafts;
	private Treadle[] treadles;

	public Loom(int numShafts, int numTreadles) {
		this.numShafts = numShafts;
		treadles = new Treadle[numTreadles];
	}

	public int getNumShafts() {
		return numShafts;
	}
	
	public Treadle[] getTreadles() {
		return treadles;
	}

	public static void main(String[] args) {
		Loom loom = new Loom(10, 4);
		System.out.println("Found " + loom.getNumShafts() + " shafts.");
		Treadle t1 = new Treadle(loom);
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
