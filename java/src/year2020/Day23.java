package year2020;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import global.util.AdventUtil;

public class Day23 {
	
	public static void main(String[] args) throws IOException {
		long startTime = System.nanoTime();
		
		System.out.println(part2(AdventUtil.inputLines(AdventUtil.DEFAULT)[0]));
		
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
		run_fast(cups, 1_000_000, 10_000_000);
		
		System.out.println(new HashSet<Integer>(cups).size()); // Will be 1_000_000 if there's no pointer duplication
		
		int i = cups.indexOf(1);
		// System.out.println(cups);
		System.out.println(i);
		System.out.println(cups.subList(i, i + 4));
		
		return ((long) cups.get(i + 1)) * ((long) cups.get(i + 2));
	}
	
	/**
	 * Runs the game but fast
	 * 
	 * @param cups
	 * @param max
	 * @param steps
	 */
	public static void run_fast(List<Integer> cups, int max, int steps) {
		// Hashmap based linked list because idk
		// Hashmap key is the value, hasmap list has {previous, next}
		HashMap<Integer, IntPair> linkedList = new HashMap<>(max);
		
		System.out.println("Building linked list...");
		
		// Convert to linked list
		for(int i = 0; i < cups.size(); i++) {
			// Original group
			if(i < 9) {
				int prev = (i == 0) ? max - 1 : cups.get(i - 1) - 1,
					next = (i == 8) ? 9 : cups.get(i + 1) - 1;
				linkedList.put(cups.get(i) - 1, new IntPair(prev, next));
			} else if(i == max - 1) { // Wrap properly
				linkedList.put(i, new IntPair(i - 1, cups.get(0) - 1));
			} else { // Linear
				linkedList.put(i, new IntPair(i - 1, i + 1));
			}
		}
		
		System.out.println("Linked List built. Running steps...");
		
		// Run steps
		List<Integer> picked = new ArrayList<>();
		
		// Step, current pointer
		for(int s = 0, cp = cups.get(0) - 1; s < steps; s++) {
			// System.out.println("\n" + s);
			// printLinkedList(linkedList);
			
			// Assemble picked list
			picked.clear();
			int p = cp;
			for(int i = 0; i < 3; i++) {
				// Next value
				p = linkedList.get(p).getB();
				picked.add(p);
			}
			
			// Find destination
			int dest = (cp == 0 ? max - 1 : cp - 1);
			while(picked.contains(dest)) dest = (dest == 0 ? max - 1 : dest - 1);
			
			// Find new linkings
			int nextCP = linkedList.get(p).getB(),			// CP will link to the value after the last picked
				nextDest = picked.get(0),					// The destination will link to picked[0]
				nextPicked = linkedList.get(dest).getB();	// picked[2] will link to the value after dest
			
			// System.out.println((cp + 1) + "   " + (dest + 1) + "   " + picked);
			// System.out.println((nextCP + 1) + "   " + (nextDest + 1) + "   " + (nextPicked + 1));
			
			if(cp == nextCP) throw new IllegalStateException("Bad CP  " + s);
			if(dest == nextDest) throw new IllegalStateException("Bad dest  " + s);
			if(picked.get(2) == nextPicked) throw new IllegalStateException("Bad picked  " + s);
			
			// Update previous values
			linkedList.get(nextCP).setA(cp);
			linkedList.get(nextDest).setA(dest);
			linkedList.get(nextPicked).setA(picked.get(2));
			
			// Update next values
			linkedList.get(cp).setB(nextCP);
			linkedList.get(dest).setB(nextDest);
			linkedList.get(picked.get(2)).setB(nextPicked);
			
			// Update cp
			cp = linkedList.get(cp).getB();
		}
		
		System.out.println("Steps complete. Converting linked list...");
		
		// Convert back to list starting from 1
		for(int i = 0, p = 0; i < linkedList.size(); i++) {
			cups.set(i, p + 1);
			p = linkedList.get(p).getB();
		}
		
		System.out.println("Complete.\n");
	}
	
	/**
	 * Prints in linked order
	 * 
	 * @param linkedList
	 */
	public static void printLinkedList(HashMap<Integer, IntPair> linkedList) {
		List<Integer> toPrint = new ArrayList<>();
		
		// Convert to list starting from 1
		for(int i = 0, p = 0; i < linkedList.size(); i++) {
			toPrint.add(p + 1);
			if(!linkedList.containsKey(p)) break;
			p = linkedList.get(p).getB();
		}
		
		System.out.println(toPrint);
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
		
		run_slow(cups, 9, 100);
		
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
	public static void run_slow(List<Integer> cups, int max, int steps) {
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

// A pair of ints
class IntPair {
	int a, b;
	
	public IntPair(int a, int b) {
		this.a = a;
		this.b = b;
	}
	
	public void setA(int na) { a = na; }
	public void setB(int nb) { b = nb; }
	
	public int getA() { return a; }
	public int getB() { return b; }
	
	@Override
	public String toString() {
		return "(" + a + ", " + b + ")";
	}
}

