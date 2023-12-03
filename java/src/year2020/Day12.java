package year2020;

import java.io.IOException;

import global.util.AdventUtil;

public class Day12 {
	
	public static void main(String[] args) throws IOException {
		System.out.println(part2(AdventUtil.inputLines(AdventUtil.DEFAULT)));
	}
	
	/**
	 * Part 2
	 * 
	 * @param lines
	 * @return
	 */
	public static int part2(String[] lines) {
		// Ship x/y, waypoint x/y
		int sx = 0, sy = 0, wx = 10, wy = 1;
		
		// Run through each line
		for(String s : lines) {
			// Get value
			int v = Integer.parseInt(s.substring(1));
			
			switch(s.charAt(0)) {
				case 'N':
					wy += v;
					break;
				
				case 'S':
					wy -= v;
					break;
				
				case 'E':
					wx += v;
					break;
				
				case 'W':
					wx -= v;
					break;
				
				case 'F':
					sx += wx * v;
					sy += wy * v;
					break;
				
				// Set the things to the proper things idk what to put
				// ox and oy avoid RAW issues
				case 'L':
					int ox = wx, oy = wy;
					v /= 90;
					wx = (v % 2 == 0) ? ox * (1 - v) : oy * (v - 2);
					wy = (v % 2 == 0) ? oy * (1 - v) : ox * (2 - v);
					break;
				
				case 'R':
					ox = wx; oy = wy;
					v /= 90;
					wx = (v % 2 == 0) ? ox * (1 - v) : oy * (2 - v);
					wy = (v % 2 == 0) ? oy * (1 - v) : ox * (v - 2);
					break;
			}
			
			//System.out.println(String.format("%s\t(%d, %d) (%d, %d)", s, sx, sy, wx, wy));
		}
		
		return Math.abs(sx) + Math.abs(sy);
	}
	
	/**
	 * Part 1
	 * 
	 * @param lines
	 * @return
	 */
	public static int part1(String[] lines) {
		int x = 0, y = 0, d = 1; // n = 0, clockwise
		
		// Run through each line
		for(String s : lines) {
			// Get value
			int v = Integer.parseInt(s.substring(1));
			
			switch(s.charAt(0)) {
				case 'N':
					y += v;
					break;
				
				case 'S':
					y -= v;
					break;
				
				case 'E':
					x += v;
					break;
				
				case 'W':
					x -= v;
					break;
				
				// Convert the direction to the relevant +/- 1
				case 'F':
					x += (d % 2 == 1 ? 2 - d : 0) * v;
					y += (d % 2 == 0 ? 1 - d : 0) * v;
					break;
				
				// Add/subtract angle, keep in proper range
				case 'L':
					d = (d - (v / 90) + 4) % 4;
					break;
				
				case 'R':
					d = (d + (v / 90) + 4) % 4;
					break;
			}
			
			// System.out.println(String.format("%s\t(%d, %d) %d", s, x, y, d));
		}
		
		return Math.abs(x) + Math.abs(y);
	}
}
