package weaveit2me.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * Processes a WIF formatted file to generate a LiftPlan. The file
 * provided must either specify a [LIFTPLAN] or provide both [TIEUP]
 * and [TREADLING] sections.
 *
 */

public class LiftPlan {

	Map<Integer, List<Integer>> picks;

	public LiftPlan() {
		this("/Default.wif");
	}

	public LiftPlan(String fileName) {
		Map<String, HashMap<String, String>> info = WIFReader.parseResource(fileName);
		if (info.containsKey("LIFTPLAN")) {
			initializePicks(info.get("LIFTPLAN"));
		} else if (info.containsKey("TIEUP") && info.containsKey("TREADLING")) {
			initializePicks(info.get("TIEUP"), info.get("TREADLING"));
		} else {
			// throw an error? throw a fit?
			System.out.println("Could not generate a lift plan from file: " + fileName);
		}
	}

	private void initializePicks(HashMap<String, String> hashMap) {
		picks = toIntegers(hashMap);
	}

	private void initializePicks(HashMap<String, String> tieMap, HashMap<String, String> treadleMap) {
		Map<Integer, List<Integer>> tieups = toIntegers(tieMap);
		Map<Integer, List<Integer>> treadles = toIntegers(treadleMap);
		picks = new HashMap<Integer, List<Integer>>();
		for (Integer treadle : treadles.keySet()) {
			if (!picks.containsKey(treadle)) {
				picks.put(treadle, new ArrayList<Integer>());
			}
			for (Integer tieup : treadles.get(treadle)) {
				picks.get(treadle).addAll(tieups.get(tieup));
			}
		}

	}

	private static Map<Integer, List<Integer>> toIntegers(HashMap<String, String> stringMap) {
		Map<Integer, List<Integer>> intMap = new HashMap<Integer, List<Integer>>();
		for (String stringKey : stringMap.keySet()) {
			Integer intKey = Integer.parseInt(stringKey.trim());
			if (!intMap.containsKey(intKey))
				intMap.put(intKey, new ArrayList<Integer>());
			String[] stringVals = stringMap.get(stringKey).split(",");
			for (String i : stringVals) {
				intMap.get(intKey).add(Integer.parseInt(i.trim()));
			}
		}
		return intMap;
	}

	/**
	 * @return the picks
	 */
	public Map<Integer, List<Integer>> getPicks() {
		return picks;
	}

	/**
	 * @param picks
	 *            the picks to set
	 */
	public void setPicks(Map<Integer, List<Integer>> picks) {
		this.picks = picks;
	}

	public static void main(String[] args) {
		// loads the default wif file
		LiftPlan p = new LiftPlan();
		System.out.println(p.getPicks());
	}

}
