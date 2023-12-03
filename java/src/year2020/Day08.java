package year2020;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import global.util.AdventUtil;

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

// Moved from other file cause it doesn't show up elsewhere
class Interpreter {
	
	long acc; // Gotta be sure we can fit the numbers
	int ip;
	
	// An array might be cleaner but if we want to add/remove instructions
	ArrayList<String> instructions;
	
	/**
	 * Initializes the interpreter with a set of instructions
	 * 
	 * @param instructions
	 */
	public Interpreter(ArrayList<String> instructions) {
		this.instructions = instructions;
		
		acc = 0;
		ip = 0;
	}
	
	/**
	 * Executes one instruction
	 * 
	 * @return True if the interpreter can continue
	 */
	public boolean step() {
		// Opcode and argument
		String[] inst = instructions.get(ip).split(" ");
		
		// Execute the instuction
		boolean jump = false;
		switch(inst[0]) {
			case "acc":
				acc += Integer.parseInt(inst[1]);
				break;
			
			case "jmp":
				ip += Integer.parseInt(inst[1]);
				jump = true;
				break;
			
			case "nop":
				break;
			
			default:
				throw new IllegalArgumentException("Invalid instruction: " + instructions.get(ip));
		}
		
		// Increment IP unless we jump
		if(!jump) ip++;
		
		// Halt if we're out of instructions
		return ip < instructions.size();
	}
	
	/*
	 * Getty bois
	 */
	public String getCurrentInstruction() { return instructions.get(ip); }
	
	public ArrayList<String> getInstructions() { return instructions; }
	
	public long getACC() { return acc; }
	public int getIP() { return ip; }
}
