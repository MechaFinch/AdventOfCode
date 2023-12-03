package year2020;

import java.io.IOException;

import global.util.AdventUtil;

public class Day03 {

	public static void main(String[] args) throws IOException {
		System.out.println(part2(AdventUtil.inputLines(AdventUtil.DEFAULT)));
	}
	
	/**
	 * Part 2
	 * 
	 * @param map
	 * @return
	 */
	public static long part2(String[] map) {
		return traverse(map, 1, 1) * traverse(map, 3, 1) * traverse(map, 5, 1) * traverse(map, 7, 1) * traverse(map, 1, 2);
	}
	
	/**
	 * Part 1
	 * 
	 * @param map
	 * @return
	 */
	public static long part1(String[] map) {
		return traverse(map, 3, 1);
	}
	
	/**
	 * Traverse the slope
	 * 
	 * @param map
	 * @param sx x component of slope
	 * @param sy y component of slope
	 * @return
	 */
	public static long traverse(String[] map, int sx, int sy) {
		long count = 0;
		
		// nyoom
		for(int x = sx, y = sy; y < map.length; y += sy, x = (x + sx) % map[0].length()) {
			if(map[y].charAt(x) == '#') count++;
		}
		
		return count;
	}
}
