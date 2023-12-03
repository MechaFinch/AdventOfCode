package year2020;

import java.io.IOException;

import global.util.AdventUtil;

public class Day17 {
	
	public static void main(String[] args) throws IOException {
		System.out.println(part2(AdventUtil.inputLines(AdventUtil.DEFAULT), 50));
	}
	
	/**
	 * Part 2
	 * copy and paste go brrrrrr
	 * 
	 * @param input
	 * @param size Size of all dimensions, range -(size / 2) to (size / 2) - 1
	 * @return
	 */
	public static int part2(String[] input, int size) {
		// Let's not worry about resizing and hope these are large enough
		int[][][][] automata = initialize2(input, size);
		
		// Do the steps
		for(int i = 0; i < 6; i++) {
			System.out.println(i);
			automata = step2(automata);
		}
		
		// Sum the things
		int sum = 0;
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				for(int z = 0; z < size; z++) {
					for(int w = 0; w < size; w++) {
						sum += automata[x][y][z][w];
					}
				}
			}
		}
		
		return sum;
	}
	
	/**
	 * Steps the automata
	 * 
	 * @param automata
	 */
	public static int[][][][] step2(int[][][][] automata) {
		int size = automata.length;
		int[][][][] next = new int[size][size][size][size];
		
		// Go over each cell
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				for(int z = 0; z < size; z++) {
					for(int w = 0; w < size; w++) {
						int count = 0;
						
						// Count surrounding cells
						for(int x2 = (x > 0 ? x - 1 : x); x2 < x + 2 && x2 < size; x2++) {
							for(int y2 = (y > 0 ? y - 1 : y); y2 < y + 2 && y2 < size; y2++) {
								for(int z2 = (z > 0 ? z - 1 : z); z2 < z + 2 && z2 < size; z2++) {
									for(int w2 = (w > 0 ? w - 1 : w); w2 < w + 2 && w2 < size; w2++) {
										// Skip current cell
										if(x == x2 && y == y2 && z == z2 && w == w2) continue;
										count += automata[x2][y2][z2][w2];
									}
								}
							}
						}
						
						// Apply rules
						if(count == 3) next[x][y][z][w] = 1;
						else if(count == 2) next[x][y][z][w] = automata[x][y][z][w];
						else next[x][y][z][w] = 0;
					}
				}
			}
		}
		
		return next;
	}
	
	/**
	 * Initializes the automata
	 * 
	 * @param input
	 * @return
	 */
	public static int[][][][] initialize2(String[] input, int size) {
		int[][][][] automata = new int[size][size][size][size];
		int center = size / 2,
			halfWidth = input[0].length() / 2;
		
		for(int y = 0; y < input.length; y++) {
			String s = input[y];
			System.out.println(s);
			
			for(int x = 0; x < s.length(); x++) {
				automata[x + center - halfWidth][y + center - halfWidth][center][center] = (s.charAt(x) == '#' ? 1 : 0);
			}
		}
		
		return automata;
	}
	
	/**
	 * Part 1
	 * 
	 * @param input
	 * @param size Size of all dimensions, range -(size / 2) to (size / 2) - 1
	 * @return
	 */
	public static int part1(String[] input, int size) {
		// Let's not worry about resizing and hope these are large enough
		int[][][] automata = initialize1(input, size);
		
		// Do the steps
		for(int i = 0; i < 6; i++) {
			automata = step1(automata);
		}
		
		// Sum the things
		int sum = 0;
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				for(int z = 0; z < size; z++) {
					sum += automata[x][y][z];
				}
			}
		}
		
		return sum;
	}
	
	/**
	 * Steps the automata
	 * 
	 * @param automata
	 */
	public static int[][][] step1(int[][][] automata) {
		int size = automata.length;
		int[][][] next = new int[size][size][size];
		
		// Go over each cell
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				for(int z = 0; z < size; z++) {
					int count = 0;
					
					// Count surrounding cells
					for(int x2 = (x > 0 ? x - 1 : x); x2 < x + 2 && x2 < size; x2++) {
						for(int y2 = (y > 0 ? y - 1 : y); y2 < y + 2 && y2 < size; y2++) {
							for(int z2 = (z > 0 ? z - 1 : z); z2 < z + 2 && z2 < size; z2++) {
								// Skip current cell
								if(x == x2 && y == y2 && z == z2) continue;
								count += automata[x2][y2][z2];
							}
						}
					}
					
					// Apply rules
					if(count == 3) next[x][y][z] = 1;
					else if(count == 2) next[x][y][z] = automata[x][y][z];
					else next[x][y][z] = 0;
				}
			}
		}
		
		return next;
	}
	
	/**
	 * Initializes the automata
	 * 
	 * @param input
	 * @return
	 */
	public static int[][][] initialize1(String[] input, int size) {
		int[][][] automata = new int[size][size][size];
		int center = size / 2,
			halfWidth = input[0].length() / 2;
		
		for(int y = 0; y < input.length; y++) {
			String s = input[y];
			System.out.println(s);
			
			for(int x = 0; x < s.length(); x++) {
				automata[x + center - halfWidth][y + center - halfWidth][center] = (s.charAt(x) == '#' ? 1 : 0);
			}
		}
		
		return automata;
	}
}
