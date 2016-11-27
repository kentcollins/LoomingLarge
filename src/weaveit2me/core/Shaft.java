package weaveit2me.core;

public class Shaft implements Comparable<Shaft> {
	private int idNum;

	public Shaft(int n) {
		idNum = n;
	}

	public int getIdNum() {
		return idNum;
	}
	
	public String toString() {
		return ""+idNum;
	}

	@Override
	public int compareTo(Shaft other) {
		if (this.idNum<other.idNum) return -1;
		else if (this.idNum>other.idNum) return 1;
		return 0;
	}
}
