package year2020;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import global.util.AdventUtil;

public class Day04 {
	
	public static void main(String[] args) throws IOException {
		System.out.println(part2(AdventUtil.inputLines(AdventUtil.DEFAULT)));
	}
	
	/**
	 * Part 2
	 * 
	 * @param lines
	 * @return
	 */
	public static int part2(String[] lines) {
		HashMap<String, String> map = new HashMap<>();
		
		// Prepare individual validation things
		// Why write out validators for each thing when you can make them incomprehensible regex arguments instead lmao
		map.put("byr", "^(19[2-9][0-9]|200[0-2])$");
		map.put("iyr", "^(201[0-9]|2020)$");
		map.put("eyr", "^(202[0-9]|2030)$");
		map.put("hgt", "^((1[5-8][0-9]|19[0-3])cm|(59|6[0-9]|7[0-6])in)$");
		map.put("hcl", "^(#[0-9a-f]{6})$");
		map.put("ecl", "^(amb|blu|brn|gry|grn|hzl|oth)$");
		map.put("pid", "^([0-9]{9})$");
		
		return check(lines, map);
	}
	
	/**
	 * Part 1
	 * 
	 * @param lines
	 * @return
	 */
	public static int part1(String[] lines) {
		HashMap<String, String> map = new HashMap<>();
		
		// Prepare always true validation map
		for(String k : new String[] {"byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"}) {
			map.put(k, ".*");
		}
		
		return check(lines, map);
	}
	
	/**
	 * Counts valid passports given the set of required keys
	 * 
	 * @param lines
	 * @param keys
	 * @param validate
	 * @return
	 */
	public static int check(String[] lines, HashMap<String, String> validationMap) {
		int count = 0;
		
		// Init lists for .contains and .equals
		HashSet<String> keySet = new HashSet<>(validationMap.keySet()),
						foundSet = new HashSet<>();
		
		// Go line by line
		for(int i = 0; i <= lines.length; i++) {
			// End of set
			if(i == lines.length || lines[i].equals("")) {
				if(keySet.equals(foundSet)) count++;
				
				foundSet.clear();
			} else {
				// Loop over pairs
				String[] pairs = lines[i].split(" ");
				for(String p : pairs) {
					String[] psplit = p.split(":");
					String k = psplit[0],
						   v = psplit[1];
					
					if(keySet.contains(k) && v.matches(validationMap.get(k))) foundSet.add(k);
				}
			}
		}
		
		return count;
	}
}
