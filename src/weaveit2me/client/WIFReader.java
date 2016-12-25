package weaveit2me.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WIFReader {
	public static Map<String, HashMap<String, String>> parseFile(String fileName) {
		Map<String, HashMap<String, String>> info = new HashMap<String, HashMap<String, String>>();
		String section = null;
		try (Scanner scan = new Scanner(new File(fileName))) {
			while (scan.hasNextLine()) {
				String s = scan.nextLine().trim();
				if (s.indexOf(";") >= 0) {
					s = s.substring(0, s.indexOf(";"));
				}
				if (s.equals("")) {
					section = null;
					continue;
				} else if (isSection(s)) {
					int leftBracket = s.indexOf("[");
					int rightBracket = s.indexOf("]");
					section = s.substring(leftBracket + 1, rightBracket);
					section = section.toUpperCase();
					info.put(section, new HashMap<String, String>());
				} else { // within a section
					int equals = s.indexOf("=");
					if (equals > -0) { // found a new mapping
						String subkey = s.substring(0, equals);
						String subval = s.substring(equals + 1);
						info.get(section).put(subkey, subval);
					}
				}

			}
		} catch (

		FileNotFoundException e) {
			e.printStackTrace();
		}

		return info;
	}

	public static Map<String, HashMap<String, String>> parseResource(String fileName) {
		InputStream is = WIFReader.class.getResourceAsStream(fileName);
		Map<String, HashMap<String, String>> info = new HashMap<String, HashMap<String, String>>();
		String section = null;
		try (Scanner scan = new Scanner(is)) {
			while (scan.hasNextLine()) {
				String s = scan.nextLine().trim();
				if (s.indexOf(";") >= 0) {
					s = s.substring(0, s.indexOf(";"));
				}
				if (s.equals("")) {
					section = null;
					continue;
				} else if (isSection(s)) {
					int leftBracket = s.indexOf("[");
					int rightBracket = s.indexOf("]");
					section = s.substring(leftBracket + 1, rightBracket);
					section = section.toUpperCase();
					info.put(section, new HashMap<String, String>());
				} else { // within a section
					int equals = s.indexOf("=");
					if (equals > -0) { // found a new mapping
						String subkey = s.substring(0, equals);
						String subval = s.substring(equals + 1);
						info.get(section).put(subkey, subval);
					}
				}

			}
		}
		return info;
	}

	private static boolean isSection(String s) {
		if (s.indexOf("[") >= 0 && s.indexOf("]") >= 0) {
			return true;
		}
		return false;
	}
}
