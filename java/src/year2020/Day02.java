package year2020;

import java.io.IOException;

import global.util.AdventUtil;

public class Day02 {
	
	public static void main(String[] args) throws IOException {
		String[] lines = AdventUtil.inputLines(AdventUtil.DEFAULT);
		
		System.out.println(part2(lines));
	}
	
	/**
	 * Part 2
	 * 
	 * @param lines
	 * @return
	 */
	public static int part2(String[] lines) { 
		int count = 0;
		
		for(String line : lines) {
			// Split into policy and password
			String[] splitLine = line.split(":"),
					 policy = splitLine[0].split("[\\- ]");
			String password = splitLine[1].substring(1);
			
			// Process policy
			int first = Integer.parseInt(policy[0]) - 1,
				second = Integer.parseInt(policy[1]) - 1;
			char c = policy[2].charAt(0);
			
			// Check
			if((password.charAt(first) == c) ^ (password.charAt(second) == c)) count++;
		}
		
		return count;
	}
	
	/**
	 * Part 1
	 * 
	 * @param lines
	 * @return
	 */
	public static int part1(String[] lines) {
		int count = 0;
		
		for(String line : lines) {
			// Split into policy and password
			String[] splitLine = line.split(":"),
					 policy = splitLine[0].split("[\\- ]");
			String password = splitLine[1].substring(1);
			
			// Process policy
			int min = Integer.parseInt(policy[0]),
				max = Integer.parseInt(policy[1]);
			
			// Check via uwu regex
			// ([^c]*c[^c]*){min,max}
			if(password.matches(String.format("([^%1$s]*%1$s[^%1$s]*){%2$d,%3$d}", policy[2], min, max))) count++;
		}
		
		return count;
	}
}
