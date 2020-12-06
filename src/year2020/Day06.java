package year2020;

import java.io.IOException;
import java.util.HashSet;

import global.AdventUtil;

public class Day06 {
	
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
		int sum = 0;
		
		HashSet<Character> yeses = new HashSet<>();
		boolean first = true; // Track first line in group
		
		for(String s : lines) {
			if(s.equals("")) {
				// count and reset
				sum += yeses.size();
				yeses.clear();
				first = true;
			} else if(first) {
				// Add characters to the set
				for(char c : s.toCharArray()) yeses.add(c);
				first = false;
			} else {
				// Remove characters if not in both
				yeses.removeIf(c -> !s.contains(c + ""));
			}
		}
		
		// Can't forget the last group
		return sum + yeses.size();
	}
	
	/**
	 * Part 1
	 * 
	 * @param lines
	 * @return
	 */
	public static int part1(String[] lines) {
		int sum = 0;
		
		HashSet<Character> yeses = new HashSet<>();
		
		for(String s : lines) {
			if(s.equals("")) {
				// count unique and reset
				sum += yeses.size();
				yeses.clear();
			} else {
				// Add characters to the set
				for(char c : s.toCharArray()) yeses.add(c);
			}
		}
		
		return sum + yeses.size();
	}
}
