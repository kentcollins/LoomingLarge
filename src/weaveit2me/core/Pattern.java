package weaveit2me.core;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a weaving pattern as a named collection of (potentially) repeatable 
 * sub-patterns, each of which specifies an ordered sequence of shaft lifts.  
 * 
 * Accomodates up to 32 shafts.  
 * 
 * @author kentcollins
 *
 */

public class Pattern {

	private String name;
	private int numShafts;
	private List<Repeat> repeats;
	//TODO Shaft patterns are no good without a threading plan
	private String threadingPlan;

	public Pattern(String name, int numShafts) {
		super();
		this.name = name;
		this.numShafts = numShafts;
		repeats = new ArrayList<Repeat>();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumShafts() {
		return numShafts;
	}

	public void setNumShafts(int numShafts) {
		this.numShafts = numShafts;
	}

	public List<Repeat> getRepeats() {
		return repeats;
	}

	public void setRepeats(List<Repeat> repeats) {
		this.repeats = repeats;
	}
	
	public void addRepeat(Repeat r) {
		repeats.add(r);
	}
	
	public void addRepeat(int position, Repeat r) {
		repeats.add(position, r);
	}
	
	public void clear(){
		repeats = new ArrayList<Repeat>();
	}
	

}
