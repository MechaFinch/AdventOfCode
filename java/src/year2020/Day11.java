package year2020;

import java.io.IOException;
import java.util.Arrays;

import global.util.AdventUtil;

public class Day11 {
	
	public static void main(String[] args) throws IOException {
		String[] lines = AdventUtil.inputLines(AdventUtil.DEFAULT);
		int[][] automata = new int[lines.length][lines[0].length()];
		
		// Setup 2d array
		for(int i = 0; i < lines.length; i++) {
			for(int j = 0; j < lines[0].length(); j++)
				automata[i][j] = lines[i].charAt(j);
		}
		
		System.out.println(part2(automata));
	}
	
	/**
	 * Part 2
	 * 
	 * @param automata
	 * @return
	 */
	public static int part2(int[][] automata) {
		// Same as part 1
		while(step2(automata) != 0);
		
		int c = 0;
		for(int i = 0; i < automata.length; i++) {
			for(int j = 0; j < automata[0].length; j++) {
				if(automata[i][j] == '#') c++;
			}
		}
		
		return c;
	}
	
	/**
	 * Steps the automata through part 2, returning the number of changes
	 * 
	 * @param automata
	 * @return
	 */
	public static int step2(int[][] automata) {
		int count = 0;
		// fancy 2d array copy
		int[][] original = Arrays.stream(automata).map(int[]::clone).toArray(int[][]::new);
		
		// Update each place
		for(int i = 0; i < automata.length; i++) {
			for(int j = 0; j < automata[0].length; j++) {
				// Skip the floor
				if(original[i][j] == '.') continue;
				
				int c = 0;
				
				// Loop to generate directions
				for(int dx = -1; dx < 2; dx++) {
					for(int dy = -1; dy < 2; dy++) {
						// Skip not moving
						if(dx == 0 && dy == 0) continue;
						
						// Walk in specified direction until a seat is found
						for(int x = i + dx, y = j + dy; x >= 0 && x < automata.length && y >= 0 && y < automata[0].length; x += dx, y += dy) {
							// Is seat?
							if(original[x][y] != '.') {
								if(original[x][y] == '#') c++;
								break;
							}
						}
					}
				}
				
				// Occupy or unoccupy depending on count
				if(c == 0) {
					automata[i][j] = '#';
					if(original[i][j] != '#') count++;
				} else if(c > 4) {
					automata[i][j] = 'L';
					if(original[i][j] != 'L') count++;
				}
			}
		}
		return count;
	}
	
	/**
	 * Part 1
	 * 
	 * @param automata
	 * @return
	 */
	public static int part1(int[][] automata) {
		// Step until stable
		while(step1(automata) != 0);
		
		// Count occupied
		int c = 0;
		for(int i = 0; i < automata.length; i++) {
			for(int j = 0; j < automata[0].length; j++) {
				if(automata[i][j] == '#') c++;
			}
		}
		
		return c;
	}
	
	/**
	 * Steps the automata through part 1, returning the number of changes
	 * 
	 * @param automata State
	 * @return Number of changed seats
	 */
	public static int step1(int[][] automata) {
		int count = 0;
		// fancy 2d array copy
		int[][] original = Arrays.stream(automata).map(int[]::clone).toArray(int[][]::new);
		
		// Update each place
		for(int i = 0; i < automata.length; i++) {
			for(int j = 0; j < automata[0].length; j++) {
				// Skip the floor
				if(original[i][j] == '.') continue;
				
				int c = 0;
				
				// Count surroundings
				for(int a = (i == 0 ? 0 : i - 1); a < i + 2 && a < automata.length; a++) {
					for(int b = (j == 0 ? 0 : j - 1); b < j + 2 && b < automata[0].length; b++) {
						if((i != a || j != b) && original[a][b] == '#') c++;
					}
				}
				
				// Occupy or unoccupy depending on count
				if(c == 0) {
					automata[i][j] = '#';
					if(original[i][j] != '#') count++;
				} else if(c > 3) {
					automata[i][j] = 'L';
					if(original[i][j] != 'L') count++;
				}
			}
		}
		return count;
	}
	
	/**
	 * Pretty print the state
	 * 
	 * @param automata
	 */
	public static void prettyPrint(int[][] automata) {
		for(int i = 0; i < automata.length; i++) {
			for(int j = 0; j < automata[0].length; j++) {
				System.out.print((char) automata[i][j]);
			}
			
			System.out.println();
		}
		
		System.out.println();
	}
}
