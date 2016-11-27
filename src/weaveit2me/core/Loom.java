package weaveit2me.core;

public class Loom {

	Shaft[] shafts;

	public Loom(int numShafts) {
		shafts = new Shaft[numShafts];
		for (int i = 0; i < shafts.length; i++) {
			shafts[i] = new Shaft(i + 1);
		}
	}

	public int getNumShafts() {
		return shafts.length;
	}

	public Shaft[] getShafts() {
		return shafts;
	}

	public Shaft getShaftById(int id) {
		for (Shaft s : shafts) {
			if (s.getIdNum() == id)
				return s;
		}
		return null;
	}

	public static void main(String[] args) {
		Loom loom = new Loom(8);
		System.out.println("Found " + loom.getNumShafts() + " shafts.");
		ShaftPattern sp = new ShaftPattern("TestPattern");
		sp.addShaft(7);
		sp.addShaft(2);
		sp.addShaft(10);
		System.out.println(sp.toBinaryString(16));
		for (int i = 7; i > 0; i--) {
			sp.addShaft(i);
			System.out.println(sp);
		}
		System.out.println(sp.toBinaryString(8));
		System.out.println(sp.toBinaryString());
	}
}
