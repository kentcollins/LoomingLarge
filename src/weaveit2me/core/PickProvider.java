package weaveit2me.core;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weaveit2me.client.LiftPlan;

/***
 * Returns a collection of integers representing shafts to lift. Responses may
 * be pulled from either an A/B alternating selection of shaft picks, as used in
 * creating a fill, or from a more complicated pattern loaded into memory; the
 * selection is determined by the current mode of the list.
 * 
 * The looping setting determines whether a pattern will be repeated once it has
 * finished; if not looping but at the end of the pattern, an empty lift (no
 * shafts) will be returned.
 * 
 * The direction setting determines whether picks are moving forwards or
 * backwards through the list; this setting will have no effect when in A/B fill
 * mode as the returned values always alternate between A and B.
 *
 */

public class PickProvider {

	public static final int DIR_FWD = 0;
	public static final int DIR_REV = 1;
	public static final int LOOP = 0;
	public static final int NO_LOOP = 1;
	public static final int AB_FILL_MODE = 0;
	public static final int LIFTPLAN_MODE = 1;
	public static final int CLEAR_MODE = 2;

	private int direction = DIR_FWD;
	private int loop = NO_LOOP;
	private int patternMode = CLEAR_MODE;

	private int patternIndex = -1;
	private String fillIndex = "A";

	private final List<Integer> NO_SHAFTS = Arrays.asList(new Integer[]{0});
	private Map<Integer, List<Integer>> patternMap;
	private Map<String, List<Integer>> fillMap;

	public PickProvider() {
		patternMap = new HashMap<>();
		patternMap.put(0, NO_SHAFTS);
		fillMap = new HashMap<>();
		fillMap.put("A", NO_SHAFTS);
		fillMap.put("B", NO_SHAFTS);
	}

	public List<Integer> getNextPick() {
		switch (patternMode) {
			case AB_FILL_MODE :
				if (fillIndex.equals("A")) {
					fillIndex = "B";
					return fillMap.get("A");
				} else {
					fillIndex = "A";
					return fillMap.get("B");
				}
			case LIFTPLAN_MODE :
				List<Integer> picks;
				if (patternMap.containsKey(patternIndex)) {
					picks = patternMap.get(patternIndex);
				} else
					picks = NO_SHAFTS;
				if (direction == DIR_FWD) {
					patternIndex++;
					// check if out of range
					if (!patternMap.containsKey(patternIndex)) {
						// if looping, go back to 1
						if (loop == LOOP)
							patternIndex = 1;
						// if not looping, just keep counting
					}
				} else if (direction == DIR_REV) {
					patternIndex--;
					if (!patternMap.containsKey(patternIndex)
							|| patternIndex <= 0) {
						if (loop == LOOP) {
							patternIndex = patternMap.size();
						} else
							patternIndex = 0;
					}
				}
				return picks;
			case CLEAR_MODE :
				return NO_SHAFTS;
			default :
				return NO_SHAFTS;
		}
	}

	/**
	 * @return the direction
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * @param direction
	 *            the direction to set
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}

	/**
	 * @return the loop
	 */
	public int getLoop() {
		return loop;
	}

	/**
	 * @param loop
	 *            the loop to set
	 */
	public void setLoop(int loop) {
		this.loop = loop;
	}

	/**
	 * @return the patternMode
	 */
	public int getPatternMode() {
		return patternMode;
	}

	/**
	 * @param patternMode
	 *            the patternMode to set
	 */
	public void setPatternMode(int patternMode) {
		this.patternMode = patternMode;
	}

	/**
	 * @return the patternIndex
	 */
	public int getPatternIndex() {
		return patternIndex;
	}

	/**
	 * @param patternIndex
	 *            the patternIndex to set
	 */
	public void setPatternIndex(int patternIndex) {
		this.patternIndex = patternIndex;
	}

	/**
	 * @return the abIndex
	 */
	public String getFillIndex() {
		return fillIndex;
	}

	/**
	 * @param abIndex
	 *            the abIndex to set
	 */
	public void setFillIndex(String fillIndex) {
		this.fillIndex = fillIndex;
	}

	/**
	 * @return the patternMap
	 */
	public Map<Integer, List<Integer>> getPatternMap() {
		return patternMap;
	}

	/**
	 * @param patternMap
	 *            the patternMap to set
	 */
	public void setPatternMap(Map<Integer, List<Integer>> patternMap) {
		this.patternMap = patternMap;
	}

	/**
	 * @return the abMap
	 */
	public Map<String, List<Integer>> getFillMap() {
		return fillMap;
	}

	/**
	 * @param abMap
	 *            the abMap to set
	 */
	public void setFillMap(Map<String, List<Integer>> abMap) {
		this.fillMap = abMap;
	}
	
	public static void main(String[] args) {
		PickProvider pp = new PickProvider();
		LiftPlan lp = new LiftPlan();
		pp.setPatternMap(lp.getPicks());
		System.out.println(pp.getPatternMap());
		pp.setPatternIndex(0);
		pp.setPatternMode(LIFTPLAN_MODE);
		pp.setDirection(DIR_FWD);
		pp.setLoop(LOOP);
		for (int i = 0; i<12; i++) {
			System.out.println(pp.getNextPick());
		}
	}

}
