package weaveit2me.core;

import java.util.List;

/**
 * Loads pattern information and provides sequential access to the repeats and
 * shaft lift plans.
 * 
 * @author kentcollins
 *
 */

public class PatternPlayback {

	private Pattern currentPattern = null;
	private int currentRepeatIndex;
	private int currentLiftIndex;

	public void loadPattern(Pattern p) {
		currentPattern = p;
		currentRepeatIndex = 0;
		currentLiftIndex = 0;
	}

	public Pattern getCurrentPattern() {
		return currentPattern;
	}

	public Repeat getCurrentRepeat() {
		return currentPattern.getRepeats().get(currentRepeatIndex);
	}

	/**
	 * Get the shaft code for the current location in the repeat.
	 * 
	 * @return the shaft plan for this location or 0 if plan is empty.
	 */
	public Integer getCurrentLift() {
		List<Integer> plan = getCurrentRepeat().getPlan();
		if (plan.size() > 0)
			return plan.get(currentLiftIndex);
		return 0;
	}

	/**
	 * Step forward to the next lift in the current repeat.
	 * 
	 * @return the shaft lift pattern for the next row or 0 if the repeat is
	 *         empty
	 */
	public Integer advance() {
		List<Integer> plan = getCurrentRepeat().getPlan();
		if (plan.size() > 0) {
			currentLiftIndex++;
			currentLiftIndex %= getCurrentRepeat().getPlan().size();
			return getCurrentLift();
		} else {
			return 0;
		}
	}

	/**
	 * Step backwards to the previous lift pattern.
	 * 
	 * @return the shaft lift pattern for the preceding row or 0 if the repeat
	 *         is empty
	 */
	public Integer reverse() {
		List<Integer> plan = getCurrentRepeat().getPlan();
		if (plan.size() > 0) {
			currentLiftIndex += getCurrentRepeat().getPlan().size() - 1;
			currentLiftIndex %= getCurrentRepeat().getPlan().size();
			return getCurrentLift();
		} else {
			return 0;
		}
	}

	/**
	 * Jump to the next repeat in this pattern and reset the lift index to the
	 * start of that repeat.
	 * 
	 * @return the lift pattern for the first line in the next repeat
	 */
	public Integer nextRepeat() {
		currentRepeatIndex++;
		currentRepeatIndex %= currentPattern.getRepeats().size();
		currentLiftIndex = 0;
		return getCurrentLift();
	}

	/**
	 * Jump to the previous repeat in this pattern and reset the lift index to
	 * the start of that repeat.
	 * 
	 * @return the lift pattern for the first line in the previous repeat
	 */
	public Integer previousRepeat() {
		currentRepeatIndex += currentPattern.getRepeats().size() - 1;
		currentRepeatIndex %= currentPattern.getRepeats().size();
		currentLiftIndex = 0;
		return getCurrentLift();
	}

	/**
	 * Sets current location to the beginning of the pattern
	 * 
	 * @return the lift pattern for the first line of the first repeat
	 */
	public Integer restartPattern() {
		currentRepeatIndex = 0;
		currentLiftIndex = 0;
		return getCurrentLift();
	}

}
