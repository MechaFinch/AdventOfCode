package global;

import java.util.ArrayList;

/**
 * An interpreter for assembly based days
 * Last year the interpreter was a key part of many problems, so it's a good idea to separate this
 * 
 * @author Mechafinch
 */
public class Interpreter {
	
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
