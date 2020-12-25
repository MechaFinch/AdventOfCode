package year2020;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import global.AdventUtil;

public class Day23 {
	
	public static void main(String[] args) throws IOException {
		long startTime = System.nanoTime();
		
		System.out.println(part1(AdventUtil.inputLines(AdventUtil.DEFAULT)[0]));
		
		System.out.println((System.nanoTime() - startTime) / 1_000_000 + " ms");
	}
	
	/**
	 * Part 2
	 * 
	 * @param input
	 * @return
	 */
	public static long part2(String input) {
		// Construct list
		List<Integer> cups = new ArrayList<>(input.chars().map(x -> x - '0').boxed().collect(Collectors.toList()));
		
		for(int i = 10; i < 1_000_001; i++) {
			cups.add(i);
		}
		
		// Run
		run(cups, 1_000_000, 10_000);
		
		int i = cups.indexOf(1);
		System.out.println(i);
		System.out.println(cups.subList(i, i + 4));
		
		return ((long) cups.get(i + 1)) * ((long) cups.get(i + 2));
	}
	
	/**
	 * Part 1
	 * 
	 * @param input
	 * @return
	 */
	public static int part1(String input) {
		// Construct list
		List<Integer> cups = new ArrayList<>(input.chars().map(x -> x - '0').boxed().collect(Collectors.toList()));
		
		run(cups, 9, 100);
		
		// Assemble answer
		int ans = 0,
			offset = cups.indexOf(1);
		
		for(int i = 1; i < cups.size(); i++) {
			ans = (ans * 10) + cups.get((i + offset) % cups.size());
		}
		
		return ans;
	}
	
	/**
	 * Runs the game
	 * 
	 * @param cups List of cups to modify
	 * @param max Maximum value
	 * @param steps Number of steps to run
	 */
	public static void run(List<Integer> cups, int max, int steps) {
		List<Integer> picked = new ArrayList<>();
		
		// Run steps
		for(int s = 0, i = 0; s < steps; s++, i = (i + 1) % max) {
			int current = cups.get(i);
			
			// Grab cups
			picked.clear();
			for(int j = 1; j < 4; j++) {
				picked.add(cups.get((i + j) % max));
			}
			
			// Find destination
			int dest = (current == 1 ? max : current - 1);
			while(picked.contains(dest)) dest = (dest == 1 ? max : dest - 1);
			
			// Rearrange list
			// Shift things over by 3 until we've shifted the destination
			// The efficiency problem is that shifting left with the origin in place is consistently shifting almost the whole thing
			// We need a separate system that shifts with something else, maybe the picked set, in place
			System.out.print(((cups.indexOf(dest) - i + max) % max) + "   ");
			int j = 4,
				m = 0;
			for(; (m = cups.get((i + j) % max)) != dest; j++) {
				cups.set((i + j - 3) % max, m);
			}
			System.out.println(j);
			
			cups.set((i + j - 3) % max, m);
			
			// Place picked
			for(int k = 0; k < 3; k++) {
				cups.set((i + j + k - 2) % max, picked.get(k));
			}
		}
	}
}
