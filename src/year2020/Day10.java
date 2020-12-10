package year2020;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import global.AdventUtil;

public class Day10 {
	
	public static void main(String[] args) throws IOException {
		System.out.println(part2(AdventUtil.intList(AdventUtil.DEFAULT)));
	}
	
	/**
	 * Part 2
	 * Linear time solution haha yes
	 * 
	 * @param input
	 * @return
	 */
	public static long part2(ArrayList<Integer> input) {
		/*
		 * The number of paths to an adapter is equal to the sum of the number of paths to the adapters that can reach it
		 */
		
		input.add(0); // Initial value is the outlet
		Collections.sort(input);
		ArrayList<Long> lengths = new ArrayList<>();
		lengths.add(1l); // One path to 0
		
		// Process all adapters
		for(int i = 1; i < input.size(); i++) {
			long sum = 0;
			int v = input.get(i);
			
			// Up to 3 previous adapters can reach
			for(int j = i - 1; j > i - 4 && j >= 0; j--) {
				// If an adapter can reach, add its count to this one's, and break if it can't reach
				if((v - input.get(j)) < 4) {
					sum += lengths.get(j);
				} else break;
			}
			
			lengths.add(sum);
		}
		
		return lengths.get(lengths.size() - 1);
	}
	
	/**
	 * Part 1
	 * 
	 * @param input
	 * @return
	 */
	public static int part1(ArrayList<Integer> input) {
		int diff1 = 0, diff3 = 1; // our port
		
		// We have to use all of them, so just go in sorted order
		Collections.sort(input);
		
		// Deal with initial value
		if(input.get(0) == 1) diff1++;
		if(input.get(0) == 3) diff3++; 
		
		// Process the rest
		for(int i = 1; i < input.size(); i++) {
			// Get difference, increment relevant counter
			int d = input.get(i) - input.get(i - 1);
			if(d == 1) diff1++;
			else if(d == 3) diff3++;
			
			// Also make sure this works
			if(d > 4 || d == 0) throw new IllegalArgumentException("Invalid difference: " + input.get(i) + " - " + input.get(i - 1) + " = " + d);
		}
		
		return diff1 * diff3;
	}
}
