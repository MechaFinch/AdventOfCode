package year2020;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import global.util.AdventUtil;

public class Day14 {
	
	public static void main(String[] args) throws IOException {
		System.out.println(part2(AdventUtil.inputLines(AdventUtil.DEFAULT)));
	}
	
	/**
	 * Part 2
	 * 
	 * @param lines
	 * @return
	 */
	public static long part2(String[] lines) {
		long bitMask = 0;
		int perms = 0; // Number of permutations from floating bits
		ArrayList<Integer> floating = new ArrayList<>();
		
		HashMap<Long, Long> mem = new HashMap<>(); // Arbitrary addresses
		
		// Process
		for(String line : lines) {
			String[] split = line.split(" = ");
			
			if(split[0].equals("mask")) { // Create mask
				bitMask = Long.parseLong(split[1].replaceAll("X", "0"), 2);
				
				// count floating
				floating.clear();
				perms = 1;
				
				// Go backwards for shifting convenience
				for(int i = split[1].length() - 1; i >= 0; i--) {
					if(split[1].charAt(i) == 'X') {
						floating.add(35 - i);
						perms *= 2;
					}
				}
			} else { // Apply mask
				long address = Long.parseLong(split[0].replaceAll("[^0-9]", "")),
					 value = Long.parseLong(split[1]);
				
				// Apply bitmask
				address |= bitMask;
				
				// Apply floating bits
				if(floating.size() == 0) continue;
				
				for(int i = 0; i < perms; i++) {
					// Set each floating bit
					for(int j = 0; j < floating.size(); j++) {
						// Test relevant bit from permutation
						if(((i >> j) & 1) == 0) { // Set to 0
							address &= Long.MAX_VALUE ^ (1l << floating.get(j)); // 1's except relevant bit
						} else { // Set to 1
							address |= 1l << floating.get(j);
						}
					}
					
					System.out.println(Long.toBinaryString(address));
					
					// Put
					mem.put(address, value);
				}
			}
		}
		
		// 8 go brrrrrrrr
		return mem.values().stream().mapToLong(Long::longValue).sum();
	}
	
	/**
	 * Part 1
	 * 
	 * @param lines
	 * @return
	 */
	public static long part1(String[] lines) {
		long onesMask = 0, zerosMask = 0;
		
		HashMap<Long, Long> mem = new HashMap<>(); // Arbitrary addresses
		
		// Process all lines
		for(String line : lines) {
			String[] split = line.split(" = ");
			
			if(split[0].equals("mask")) { // Setup mask
				onesMask = Long.parseLong(split[1].replaceAll("X", "0"), 2);						// all 0s except 1s
				zerosMask = Long.parseLong(split[1].replaceAll("1", "X").replaceAll("X", "1"), 2);	// all 1's except 0s
			} else { // Apply mask
				long address = Long.parseLong(split[0].replaceAll("[^0-9]", "")), // Isolate address
					 value = Long.parseLong(split[1]);
				
				// Apply
				value |= onesMask;
				value &= zerosMask;
				
				mem.put(address, value);
			}
		}
		
		// 8 go brrrr
		return mem.values().stream().mapToLong(Long::longValue).sum();
	}
}
