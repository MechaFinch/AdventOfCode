package year2020;

import java.io.IOException;
import java.util.ArrayList;

import global.AdventUtil;

public class Day18 {
	
	public static void main(String[] args) throws IOException {
		System.out.println(part2(AdventUtil.inputLines(AdventUtil.DEFAULT)));
	}
	
	/**
	 * Part 2
	 * 
	 * @param input
	 * @return
	 */
	public static long part2(String[] input) {
		long sum = 0;
		
		// Each line
		for(String s : input) sum += eval2(s);
		
		return sum;
	}
	
	/**
	 * Recursive evaluator (part 2)
	 * 
	 * @param line
	 * @return
	 */
	public static long eval2(String line) {
		ArrayList<String> tokens = tokenize(line);
		
		// Deal with single tokens
		if(tokens.size() == 1) {
			String t = tokens.get(0);
			
			// Evaluate the inside of parens
			if(t.startsWith("(")) return eval2(t.substring(1, t.length() - 1));
			return Integer.parseInt(t); // Otherwise its a number
		}
		
		// Addition Pass
		for(int i = 1; i < tokens.size() - 1; i += 2) {
			// Replace the set of 3 tokens with the result
			if(tokens.get(i).equals("+")) {
				tokens.set(i - 1, Long.toString(eval2(tokens.get(i - 1)) + eval2(tokens.get(i + 1))));
				tokens.remove(i); // Remove consumed values
				tokens.remove(i);
				i -= 2;
			}
		}
		
		// Multiplication pass
		long accumulator = eval2(tokens.get(0));
		for(int i = 2; i < tokens.size(); i += 2) {
			accumulator *= eval2(tokens.get(i)); 
		}
		
		return accumulator;
	}
	
	/**
	 * Part 1
	 * 
	 * @param input
	 * @return
	 */
	public static long part1(String[] input) {
		long sum = 0;
		
		// Each line
		for(String s : input) sum += eval1(s);
		
		return sum;
	}
	
	/**
	 * Recursive evaluator (part 1)
	 * 
	 * @param line
	 * @return
	 */
	public static long eval1(String line) {
		ArrayList<String> tokens = tokenize(line);
		
		// Deal with single tokens
		if(tokens.size() == 1) {
			String t = tokens.get(0);
			
			// Evaluate the inside of parens
			if(t.startsWith("(")) return eval1(t.substring(1, t.length() - 1));
			return Integer.parseInt(t); // Otherwise its a number
		}
		
		// Get first
		long accumulator = eval1(tokens.get(0));
		
		// Process by operator
		for(int i = 1; i < tokens.size() - 1; i += 2) {
			String op = tokens.get(i);
			
			if(op.equals("+")) accumulator += eval1(tokens.get(i + 1));
			else if(op.equals("*")) accumulator *= eval1(tokens.get(i + 1));
		}
		
		return accumulator;
	}
	
	/**
	 * Splits a line into its component tokens
	 * 
	 * @param line
	 * @return
	 */
	public static ArrayList<String> tokenize(String line) {
		ArrayList<String> tokens = new ArrayList<>();
		
		// By character
		for(int i = 0, parens = 0; i <= line.length(); i++) {
			// End of a token?
			if(i == line.length() || (line.charAt(i) == ' ' && parens == 0)) {
				tokens.add(line.substring(0, i));
				if(i == line.length()) break;
				line = line.substring(i + 1);
				i = -1;
			} else if(line.charAt(i) == '(') { // Increase parenthesis depth
				parens++;
			} else if(line.charAt(i) == ')') { // Decrease parenthesis depth
				parens--;
			}
		}
		
		return tokens;
	}
}
