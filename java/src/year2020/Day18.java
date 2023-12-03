package year2020;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import global.util.AdventUtil;

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
		HashMap<Integer, String> pmap = new HashMap<>();
		pmap.put(0, "\\+");
		pmap.put(1, "\\*");
		
		return solve(input, pmap);
	}
	
	/**
	 * Part 1
	 * 
	 * @param input
	 * @return
	 */
	public static long part1(String[] input) {
		HashMap<Integer, String> pmap = new HashMap<>();
		pmap.put(0, "\\+|\\*");
		
		return solve(input, pmap);
	}
	
	/**
	 * General solution
	 * 
	 * @param input
	 * @param precedence
	 * @return
	 */
	public static long solve(String[] input, HashMap<Integer, String> precedence) {
		long sum = 0;
		
		// Each line
		for(String s : input) sum += eval(s, precedence);
		
		return sum;
	}
	
	/**
	 * Recursive evaluator (general)
	 * 
	 * @param line
	 * @param precedence Map of precedence to regex matching the operators, evaluated 0, 1, 2, etc
	 * @return
	 */
	public static long eval(String line, HashMap<Integer, String> precedence) {
		ArrayList<String> tokens = tokenize(line);
		
		// Single tokens
		if(tokens.size() == 1) {
			String t = tokens.get(0);
			
			// Evaluate parenthesis interiors or just return the value
			if(t.startsWith("(")) return eval(t.substring(1, t.length() - 1), precedence);
			return Long.parseLong(t);
		}
		
		// Apply each set of operators by precedence, left to right
		for(int p = 0, levels = precedence.keySet().size(); p < levels; p++) {
			String opRegex = precedence.get(p);
			
			// Pass over tokens, replacing sets of 3 (arg, op, arg) with the result
			for(int i = 1; i < tokens.size() - 1; i += 2) {
				String op = tokens.get(i);
				
				if(op.matches(opRegex)) {
					long result = 0,
						 a = eval(tokens.get(i - 1), precedence),
						 b = eval(tokens.get(i + 1), precedence);
					
					// Evaluate whatever operator this is, to support more put them here
					if(op.equals("+")) result = a + b;
					else if(op.equals("*")) result = a * b;
					
					// Replace set of 3 with the result
					tokens.set(i - 1, Long.toString(result));
					tokens.remove(i);
					tokens.remove(i);
					i -= 2;
				}
			}
		}
		
		return Long.parseLong(tokens.get(0));
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
