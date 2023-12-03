package year2020;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import global.util.AdventUtil;

public class Day13 {
	
	public static void main(String[] args) throws IOException {
		String[] input = AdventUtil.inputLines(AdventUtil.DEFAULT);
		
		// Console input
		long start = Long.parseLong(args[0]),
			 size = Long.parseLong(args[1]);
		int count = Integer.parseInt(args[2]);
		
		long[] values = new long[count];
		
		for(int i = 0; i < count; i++) {
			values[i] = start + (size * i);
		}
		
		long startTime = System.nanoTime() / 1_000_000_000l;
		
		// Parallelize part 2
		Arrays.stream(values).parallel().forEach(x -> {
			System.out.println("RESULT: " + part2(input, x, size));
		});
		
		System.out.println("Elapsed time: " + ((System.nanoTime() / 1_000_000_000l) - startTime) + "s");
	}
	
	/**
	 * Part 2
	 * 
	 * ~11.9 seconds per trillion
	 * 
	 * @param input
	 * @param start Starting value
	 * @param area Size of the area to search
	 * @return
	 */
	public static long part2(String[] input, long start, long area) {
		// Fill x's with -1 this time
		List<Integer> idsList = Arrays.stream(input[1].split(",")).mapToInt(s -> s.equals("x") ? -1 : Integer.parseInt(s)).boxed().collect(Collectors.toList());
		int max = Collections.max(idsList),
			min = Collections.min(idsList, (a, b) -> {
				if(a == -1) a = Integer.MAX_VALUE;
				if(b == -1) b = Integer.MAX_VALUE;
				return a - b;
			}),
			step = max * min;
		
		// Convert to pair of arrays, one with ids and the other with indicies
		int[] ids = idsList.stream().filter(x -> x != -1).mapToInt(x -> x).toArray();
		int[] indicies = new int[ids.length];
		
		for(int i = 0; i < ids.length; i++) {
			indicies[i] = idsList.indexOf(ids[i]);
		}
		
		// Start from the lowest multiple of the step
		start += step - (start % step) - idsList.indexOf(max);
		
		// Search
		long prev = 0,
			 oldTime = System.nanoTime() / 1_000_000;
		
		outer:
		for(long i = start; i < start + area + (2 * step); i += step) {
			// Monitor how long this is taking for comparison purposes
			if(i - prev >= 10_000_000_000_000l) {
				long newTime = System.nanoTime() / 1_000_000;
				System.out.println(i + "   " + (newTime - oldTime) + "ms");
				
				prev = i;
				oldTime = newTime;
			}
			
			// Run through by the requirements
			for(int j = 0; j < ids.length; j++) {
				// Correct minute 
				if((i + indicies[j]) % ids[j] != 0) {
					//System.out.println(j);
					continue outer;
				}
			}
			
			return i;
		}
		
		return -1;
	}
	
	/**
	 * Part 1
	 * 
	 * @param input
	 * @return
	 */
	public static int part1(String[] input) {
		// Filter out x's when converting to ints
		List<Integer> ids = Arrays.stream(input[1].split(",")).filter(s -> !s.equals("x")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
		int start = Integer.parseInt(input[0]),
			min = Collections.min(ids);
		
		// Minimum we need to check
		for(int i = start; i < start + min + 1; i++) {
			for(int v : ids) {
				if(i % v == 0) return (i - start) * v;
			}
		}
		
		return -1;
	}
}
