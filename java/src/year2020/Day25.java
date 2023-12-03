package year2020;

import java.io.IOException;

import global.util.AdventUtil;

public class Day25 {
	
	public static void main(String[] args) throws IOException {
		System.out.println(part1(AdventUtil.inputLines(AdventUtil.DEFAULT)));
	}
	
	/**
	 * Part 1
	 * 
	 * @param input
	 * @return
	 */
	public static long part1(String[] input) {
		long pk1 = Long.parseLong(input[0]),
			 pk2 = Long.parseLong(input[1]),
			 size = findLoopSize(pk2, 7, 20201227);
		
		System.out.println(findLoopSize(pk1, 7, 20201227));
		System.out.println(findLoopSize(pk2, 7, 20201227));
		
		return getKey(1, pk1, 20201227, size);
	}
	
	/**
	 * Finds the loop size of a key
	 * 
	 * @param key
	 * @param subject
	 * @param divisor
	 * @return
	 */
	public static long findLoopSize(long key, long subject, long divisor) {
		long size = 0,
			 value = 1;
		
		while(value != key) {
			value = (value * subject) % divisor;
			size++;
		}
		
		return size;
	}
	
	/**
	 * Runs the encryption
	 * 
	 * @param value
	 * @param subject
	 * @param divisor
	 * @return
	 */
	public static long getKey(long value, long subject, long divisor, long size) {
		for(int i = 0; i < size; i++) value = (value * subject) % divisor;
		return value;
	}
}
