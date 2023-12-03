package year2020;

import java.io.IOException;

import global.util.AdventUtil;

public class Day24 {
	
	public static void main(String[] args) throws IOException {
		System.out.println(part2(AdventUtil.inputLines(AdventUtil.DEFAULT)));
	}
	
	/**
	 * Part 2
	 * 
	 * @param input
	 * @return
	 */
	public static int part2(String[] input) {
		int size = 175;
		boolean[][][] tiles = generateInitial(input, size);
		
		// Run automata
		for(int i = 0; i < 100; i++) {
			step(tiles);
		}
		
		// Count black tiles
		int count = 0;
		
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				for(int z = 0; z < size; z++) {
					count += tiles[x][y][z] ? 1 : 0;
				}
			}
		}
		
		return count;
	}
	
	/**
	 * Steps the automata
	 * 
	 * @param tiles
	 */
	public static void step(boolean[][][] tiles) {
		int size = tiles.length;
		boolean[][][] next = new boolean[size][size][size];
		
		// Find next state for each tile
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				for(int z = 0; z < size; z++) {
					// Count surroundings
					int count = 0;
					
					// eh just do it manually
					count += god(tiles, x, y + 1, z - 1) ? 1 : 0; // NW
					count += god(tiles, x + 1, y, z - 1) ? 1 : 0; // NE
					count += god(tiles, x + 1, y - 1, z) ? 1 : 0; // E
					count += god(tiles, x, y - 1, z + 1) ? 1 : 0; // SE
					count += god(tiles, x - 1, y, z + 1) ? 1 : 0; // SW
					count += god(tiles, x - 1, y + 1, z) ? 1 : 0; // W
					
					// Set next
					if(tiles[x][y][z]) {
						next[x][y][z] = (count == 1 || count == 2);
					} else {
						next[x][y][z] = (count == 2);
					}
				}
			}
		}
		
		// Deep copy
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				for(int z = 0; z < size; z++) {
					tiles[x][y][z] = next[x][y][z];
				}
			}
		}
	}
	
	/**
	 * Get or Default
	 * 
	 * @param tiles
	 * @param x
	 * @param y
	 * @param z
	 * @return tiles[x][y][z] or false if x, y, or z are out of range
	 */
	public static boolean god(boolean[][][] tiles, int x, int y, int z) {
		int size = tiles.length;
		
		if(x >= size || x < 0 ||
		   y >= size || y < 0 ||
		   z >= size || z < 0) {
			return false;
		}
		
		return tiles[x][y][z];
	}
	
	/**
	 * Part 1
	 * 
	 * @param input
	 * @return
	 */
	public static int part1(String[] input) {
		// Guarantees a fit
		int size = 50;
		boolean[][][] tiles = generateInitial(input, size);
		
		// Count black tiles
		int count = 0;
		
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				for(int z = 0; z < size; z++) {
					count += tiles[x][y][z] ? 1 : 0;
				}
			}
		}
		
		return count;
	}
	
	/**
	 * Generates the floor
	 * 
	 * @param input
	 * @return
	 */
	public static boolean[][][] generateInitial(String[] input, int size) {
		// White = false, black = true
		boolean[][][] tiles = new boolean[size][size][size];
		
		for(String s : input) {
			int x = size / 2,
				y = size / 2,
				z = size / 2;
			
			// Consume characters to find position
			while(s.length() > 0) {
				// Use thing from https://www.redblobgames.com/grids/hexagons/#conversions
				switch(s.charAt(0)) {
					case 'n':
						x += s.charAt(1) == 'e' ? 1 : 0;
						y += s.charAt(1) == 'e' ? 0 : 1;
						z--;
						s = s.substring(2);
						break;
						
					case 's':
						x -= s.charAt(1) == 'e' ? 0 : 1;
						y -= s.charAt(1) == 'e' ? 1 : 0;
						z++;
						s = s.substring(2);
						break;
						
					case 'e':
						x++;
						y--;
						s = s.substring(1);
						break;
						
					case 'w':
						x--;
						y++;
						s = s.substring(1);
						break;
				}
			}
			
			// Update tile
			tiles[x][y][z] = !tiles[x][y][z];
		}
		
		return tiles;
	}
}
