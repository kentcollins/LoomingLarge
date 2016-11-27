package weaveit2me.core;
/**
 * A collection of integers representing the shafts that should be lifted
 * when this pattern is requested.  Any integer may be added to the list.
 *
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShaftPattern {
	String name;
	List<Integer> included;

	public ShaftPattern(String name) {
		this.name = name;
		included = new ArrayList<Integer>();
	}

	public String getName() {
		return name;
	}

	/**
	 * If not already in the pattern, add the indicated shaft
	 * 
	 * @param shaftId number of the shaft
	 */
	public void addShaft(Integer shaftId) {
		if (shaftId != null && !included.contains(shaftId))
			included.add(shaftId);
	}

	/**
	 * Remove the indicated shaft if it is in the pattern.
	 * 
	 * @param shaftId
	 */
	public void removeShaft(Integer shaftId) {
		if (shaftId !=null && included.contains(shaftId))
			included.remove(shaftId);
	}

	/**
	 * Determine whether the indicated shaft is in this pattern.
	 * 
	 * @param shaftId
	 * @return
	 */
	public boolean includes(int shaftId) {
		for (Integer i : included) {
			if (i == shaftId)
				return true;
		}
		return false;
	}

	/**
	 * Expresses this pattern as a string of binary values.  The rightmost
	 * digit is for shaft 1 and additional shafts increase from right to left.
	 * 1 indicates a shaft is lifted; 0, lowered.  
	 * 
	 * If the shaft pattern holds any values higher than the requested number 
	 * of shafts, they will simply and silently be disregarded.
	 * 
	 * @param numShafts the number of shaft places to include in presentation.
	 * @return a binary string where 1's indicate a lifted shaft
	 */
	public String toBinaryString(int numShafts) {
		String s = "";
		Collections.sort(included);
		for (int i = 1; i <= numShafts; i++) {
			if (includes(i))
				s = 1 + s;
			else
				s = 0 + s;
		}
		return s;
	}
	
	/**
	 * Get a binary string at the minimum length necessary
	 * 
	 * @return binary string
	 */
	public String toBinaryString() {
		Collections.sort(included);
		int maxShaftsNeeded = included.get(included.size()-1);
		return toBinaryString(maxShaftsNeeded);
	}

	/**
	 * A string representation showing which shafts are included in this 
	 * pattern, in increasing order.
	 * 
	 * @return a string indicating which shafts are included
	 */
	public String toString() {
		Collections.sort(included);
		String s = name + " (";
		for (Integer i:included) {
			s+=i;
			s+=" ";
		}
		s=s.substring(0, s.length()-1);
		s+=")";
		return s;
	}

	/**
	 * Get the list of shafts.
	 * 
	 * @return
	 */
	public List<Integer> getIncludedShafts() {
		return included;
	}

}
