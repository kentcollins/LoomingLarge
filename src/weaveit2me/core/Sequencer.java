package weaveit2me.core;

public class Sequencer {
	private Pattern pattern;
	private int loc;
	
	public Sequencer(){
	}
	
	public Sequencer(Pattern p) {
		pattern = p;
		loc = 0;
	}
	
	public void setIndex(int i) {
		loc = i;
	}
	
	public int getIndex() {
		return loc;
	}
	


}
