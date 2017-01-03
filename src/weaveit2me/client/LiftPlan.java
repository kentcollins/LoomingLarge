package weaveit2me.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	public void loadFromURL(String url) throws IOException {
		URL remoteFile = new URL(url);
        BufferedReader in = new BufferedReader(
        new InputStreamReader(remoteFile.openStream()));

        String inputLine;
        List<String> strings = new ArrayList<>();
        while ((inputLine = in.readLine()) != null)
            strings.add(inputLine);
        in.close();
        Map<String, HashMap<String, String>> info =WIFReader.parseStringArray(strings.toArray(new String[] {}));
		if (info.containsKey("LIFTPLAN")) {
			initializePicks(info.get("LIFTPLAN"));
		} else if (info.containsKey("TIEUP") && info.containsKey("TREADLING")) {
			initializePicks(info.get("TIEUP"), info.get("TREADLING"));
		} else {
			// throw an error? throw a fit?
			System.out.println("Could not generate a lift plan from file: " + url);
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
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Integer[] keys = picks.keySet().toArray(new Integer[picks.keySet().size()]);
		Arrays.sort(keys);
		for (Integer i: keys) {
			sb.append(picks.get(i));
		}
		return sb.toString();
	}

	public static void main(String[] args) throws IOException {
		// loads the default wif file
		LiftPlan p = new LiftPlan();
		p.loadFromURL("1fhmz2QkeJ1DQh8KiQKhEzGpxJjOO9oLcZGAK_vpjjfY");
		System.out.println(p.getPicks());
	}

}
