package year2020;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import global.util.AdventUtil;

public class Day19 {
	
	public static void main(String[] args) throws IOException {
		System.out.println(part2(AdventUtil.inputList(AdventUtil.DEFAULT)));
	}
	
	public static int part2(ArrayList<String> input) {
		Map<Integer, String> rules = generateRules(input.subList(0, input.indexOf("")));
		List<String> messages = input.subList(input.indexOf("") + 1, input.size());
		Set<String> r31 = generatePossible(rules, 31), // 31 and 42 are the only non-recursive parts of 8 and 11
					r42 = generatePossible(rules, 42);
		int l31 = r31.toArray(new String[0])[0].length(), // Lengths for the contents of 31 and 42
			l42 = r42.toArray(new String[0])[0].length(),
			count = 0;
		
		/*
		 * Rule 0 matches 42 at least 2 times, then 31 at least once
		 * Rule 0 matches 42 a + b times, then 31 b times
		 * a > 1, b > 1
		 * Number of 42s must be greater than the number of 32s
		 */
		
		for(String m : messages) {
			int n42 = 2,
				n31 = 1;
			
			// Initial check and trim for 2x42
			if(!match(r42, m)) continue;
			m = m.substring(l42);
			if(!match(r42, m)) continue;
			m = m.substring(l42);
			
			while(match(r42, m)) { // Rest of the 42s
				m = m.substring(l42);
				n42++;
			}
			
			// Check 31
			if(!match(r31, m)) continue;
			m = m.substring(l31);
			
			// The rest of the string should match
			while(match(r31, m)) {
				m = m.substring(l31);
				n31++;
			}
			
			// Match
			if(m.equals("") && n42 > n31) count++; 
		}
		
		return count;
	}
	
	/**
	 * Checks if s starts with a string in possible
	 * 
	 * @param possible
	 * @param s
	 * @return Whether s matched
	 */
	public static boolean match(Set<String> possible, String s) {
		for(String p : possible) if(s.startsWith(p)) return true;
		return false;
	}
	
	/**
	 * Part 1
	 * 
	 * @param input
	 * @return
	 */
	public static int part1(ArrayList<String> input) {
		Set<String> possible = generatePossible(generateRules(input.subList(0, input.indexOf(""))), 0);
		List<String> messages = input.subList(input.indexOf("") + 1, input.size());
		int count = 0;
		
		// Check each message
		for(String m : messages) if(possible.contains(m)) count++;
		
		return count;
	}
	
	/**
	 * Generates the possible matches for a given rule
	 * 
	 * @param rules
	 * @param rule
	 * @return
	 */
	public static Set<String> generatePossible(Map<Integer, String> rules, int rule) {
		String fr = rules.get(rule); // Full rule
		Set<String> possible = new HashSet<>();
		
		// Single character
		if(fr.matches("\".\"")) {
			possible.add(fr.charAt(1) + "");
		} else {
			// Subrules
			for(String sr : fr.split(" \\| ")) {
				String[] split = sr.split(" ");
				
				// Only one
				if(split.length == 1) {
					possible.addAll(generatePossible(rules, Integer.parseInt(split[0])));
				} else {
					// Usually 2
					Set<String> a = generatePossible(rules, Integer.parseInt(split[0])),
								b = generatePossible(rules, Integer.parseInt(split[1]));
					
					// Combine
					for(String sa : a) {
						for(String sb : b) {
							possible.add(sa + sb);
						}
					}
				}
			}
		}
		
		return possible;
	}
	
	/**
	 * Builds a map of rules
	 * 
	 * @param input
	 * @return
	 */
	public static Map<Integer, String> generateRules(List<String> input) {
		HashMap<Integer, String> map = new HashMap<>();
		
		for(String s : input) {
			String[] split = s.split(": ");
			map.put(Integer.parseInt(split[0]), split[1]);
		}
		
		return map;
	}
}
