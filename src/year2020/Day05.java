package year2020;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import global.AdventUtil;

public class Day05 {
	
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
		ArrayList<Integer> seats = convert(lines);
		Collections.sort(seats);
		
		// Linear search
		for(int i = 0; i < seats.size() + 1; i++) {
			// +1 and -1 exist, but not center
			if(seats.get(i + 1) - seats.get(i) == 2) return seats.get(i) + 1;
		}
		
		return -1;
	}
	
	/**
	 * Converts from encoded strings to integers
	 * 
	 * @param lines
	 * @return
	 */
	public static ArrayList<Integer> convert(String[] lines) {
		ArrayList<Integer> list = new ArrayList<>();
		
		for(String s : lines) {
			// Convert to binary string, parse int
			list.add(Integer.parseInt(s.replaceAll("F|L", "0").replaceAll("B|R", "1"), 2));
		}
		
		return list;
	}
	
	/**
	 * Part 1
	 * 
	 * @param lines
	 * @return
	 */
	public static int part1(String[] lines) {
		int max = 0;
		
		for(String s : lines) {
			// Convert to binary, parse int
			int v = Integer.parseInt(s.replaceAll("F|L", "0").replaceAll("B|R", "1"), 2);
			if(v > max) max = v;
		}
		
		return max;
	}
}
