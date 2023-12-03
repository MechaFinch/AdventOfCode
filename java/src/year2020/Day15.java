package year2020;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import global.util.AdventUtil;

public class Day15 {
	
	public static void main(String[] args) throws IOException {
		System.out.println(solution(AdventUtil.inputLines(AdventUtil.DEFAULT), 30_000_000));
	}
	
	/**
	 * General solution
	 * 
	 * @param input
	 * @param index 2020 for part 1, 30,000,000 for part 2
	 * @return
	 */
	public static int solution(String[] input, int index) {
		// Easy version of input
		List<Integer> numbers = Arrays.asList(input[0].split(",")).stream().mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
		
		// Map of a number to its last seen index
		HashMap<Integer, Integer> map = new HashMap<>();
		
		// Initialize
		for(int i = 0; i < numbers.size() - 1; i++) map.put(numbers.get(i), i);
		
		// Iterate
		int last = numbers.get(numbers.size() - 1); // Previous value
		for(int i = numbers.size(); i < index; i++) {
			// Feels faster when you can see it progress
			if(i % 1_000_000 == 0) System.out.println(i);
			
			// Index of the previous value or current index
			int diff = i - map.getOrDefault(last, i - 1) - 1;
			
			// Update with previous value
			map.put(last, i - 1);
			last = diff;
		}
		
		return last;
	}
}
