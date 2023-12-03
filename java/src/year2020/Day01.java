package year2020;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import global.util.AdventUtil;

public class Day01 {
	public static void main(String[] args) throws IOException{
		File inputfile = new File("input.txt");
		
		ArrayList<Integer> input = AdventUtil.intList(inputfile);
		input.sort((a, b) -> b - a);
		System.out.println(input);
		
		// Search recursively
		System.out.println(recursiveHelper(input, 0, 8, 0));
	}
	
	/**
	 * A completely overkill method which can search for arbitrarily sized sets, currently setup for 8 values (which works!)
	 * 
	 * @param in
	 * @param startIndex
	 * @param depth
	 * @param sum
	 * @return
	 */
	public static int recursiveHelper(ArrayList<Integer> in, int startIndex, int depth, int sum) {
		// Last number
		if(depth == 1) {
			for(int i = startIndex; i < in.size(); i++) {
				if(in.get(i) + sum == 2020) {
					System.out.println("\t\t\t\t\t\t\t" + in.get(i));
					return in.get(i);
				} else if(in.get(i) + sum < 2020) { // too far
					return -1;
				}
			}
			
			return -1;
		}
		
		// Find largest value whose sum is less than 2020
		int i;
		for(i = startIndex; i < in.size(); i++) {
			if(in.get(i) + sum < 2020) {
				break;
			}
		}
		
		if(i == in.size()) return -1;
		
		// Traverse smaller numbers
		for(; i < in.size(); i++) {
			// Print values we're using
			for(int j = 8 - depth; j > 0; j--) {
				System.out.print("\t");
			}
			
			System.out.println(in.get(i));
			
			// Recurse
			int r = recursiveHelper(in, i, depth - 1, sum + in.get(i));
			if(r != -1) {
				return in.get(i) * r;
			}
		}
		
		// Not found
		return -1;
	}
}
