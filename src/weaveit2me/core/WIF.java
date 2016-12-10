package weaveit2me.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/***
 * Holds information complying with the WIF file format.
 *
 */

public class WIF {

	Map<String, HashMap<String, String>> info = new HashMap<String, HashMap<String, String>>();

	public WIF() {
		info = new HashMap<String, HashMap<String, String>>();
	}

	public static WIF loadFile(String fileName) {
		WIF wif = new WIF();
		String section = null;
		try (Scanner scan = new Scanner(new File(fileName))) {
			while (scan.hasNextLine()) {
				String s = scan.nextLine().trim();
				if (s.equals("")) {
					section = null;
					continue;
				} else if (isSection(s)) {
					int leftBracket = s.indexOf("[");
					int rightBracket = s.indexOf("]");
					section = s.substring(leftBracket + 1, rightBracket);
					wif.info.put(section, new HashMap<String, String>());
				} else { // within a section
					int equals = s.indexOf("=");
					if (equals > -0) { // found a new mapping
						String subkey = s.substring(0, equals);
						String subval = s.substring(equals + 1);
						wif.info.get(section).put(subkey, subval);
					}
				}

			}
		} catch (

		FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(wif.info);
		return wif;
	}

	private static boolean isSection(String s) {
		if (s.indexOf("[") >= 0 && s.indexOf("]") >= 0) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		WIF wif = WIF.loadFile("DDW-Multi with Dobby.wif");
	}
}
