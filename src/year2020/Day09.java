package year2020;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import global.AdventUtil;

public class Day09 {
	
	public static void main(String[] args) throws IOException {
		System.out.println(part2(AdventUtil.inputLongs(AdventUtil.DEFAULT), 25));
	}
	
	/**
	 * Part 2
	 * 
	 * @param input
	 * @return
	 */
	public static long part2(long[] input, int size) {
		long target = part1(input, size),
			 sum = 0;
		
		// Search for contiguous thing
		ArrayList<Long> longs = new ArrayList<>();
		for(int i = 0; i < input.length; i++) {
			// Add next value until we go past or equal the target
			longs.add(input[i]);
			sum += input[i];
			
			if(sum < target) continue;
			if(sum == target) return Collections.min(longs) + Collections.max(longs);
			
			// Remove previous values until we're under or equal
			while(sum > target) {
				sum -= longs.get(0);
				longs.remove(0);
			}
			
			if(sum == target) return Collections.min(longs) + Collections.max(longs);
		}
		
		return -1;
	}
	
	/**
	 * Part 1
	 * 
	 * @param input
	 * @param size
	 * @return
	 */
	public static long part1(long[] input, int size) {
		ArrayList<Long> possibleValues = new ArrayList<>();
		
		// Process preamble
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				possibleValues.add(input[i] + input[j]);
			}
		}
		
		// Find exception
		for(int i = size; i < input.length; i++) {
			// Check for exception
			if(!possibleValues.contains(input[i])) return input[i];
			
			// Update list
			for(int j = 0; j < size; j++) {
				possibleValues.remove(0);
				possibleValues.add(input[i] + input[i - j - 1]);
			}
		}
		
		return -1;
	}
}
