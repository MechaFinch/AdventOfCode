package year2020;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import global.AdventUtil;
import global.Interpreter;

public class Day08 {
	
	public static void main(String[] args) throws IOException {
		System.out.println(part2(AdventUtil.inputList(AdventUtil.DEFAULT)));
	}
	
	/**
	 * Part 2
	 * 
	 * @param program
	 * @return
	 */
	public static long part2(ArrayList<String> program) {
		// Replace instructions until we halt
		for(int i = 0; i < program.size(); i++) {
			String s = program.get(i);
			
			// Correct instruction?
			if((s.startsWith("nop") && !s.endsWith("+0")) || s.startsWith("jmp")) {
				// Make a copy
				ArrayList<String> pc = new ArrayList<>(program);
				
				// Replace
				String[] inst = s.split(" ");
				pc.set(i, (inst[0].equals("nop") ? "jmp" : "nop") + " " + inst[1]);
				
				// Execute until repeat or halt
				HashSet<Integer> addresses = new HashSet<>();
				Interpreter intp = new Interpreter(pc);
				
				while(!addresses.contains(intp.getIP())) {
					addresses.add(intp.getIP());
					
					if(!intp.step()) return intp.getACC();
				}
			}
		}
		
		return -1l;
	}
	
	/**
	 * Part 1
	 * 
	 * @param program
	 * @return
	 */
	public static long part1(ArrayList<String> program) {
		Interpreter intp = new Interpreter(program);
		HashSet<Integer> addresses = new HashSet<>();
		
		// Step until repeat
		while(!addresses.contains(intp.getIP())) {
			addresses.add(intp.getIP());
			intp.step();
		}
		
		return intp.getACC();
	}
}
