package year2020;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import global.AdventUtil;

public class Day07 {
	
	public static void main(String[] args) throws IOException {
		System.out.println(part2(AdventUtil.inputList(AdventUtil.DEFAULT)));
	}
	
	/**
	 * Part 2
	 * 
	 * @param lines
	 * @return
	 */
	public static int part2(ArrayList<String> lines) {
		ArrayList<BagType> typeList = buildList(lines);
		
		// Count inside the shiny gold
		return typeList.get(typeList.indexOf(new BagType("shiny gold"))).countBags() - 1;
	}
	
	/**
	 * Part 1
	 * 
	 * @param lines
	 * @return
	 */
	public static int part1(ArrayList<String> lines) {
		int count = 0;
		ArrayList<BagType> typeList = buildList(lines);
		
		// Count types containing shiny gold
		for(BagType t : typeList) {
			if(t.contains("shiny gold")) count++;
		}
		
		return count;
	}
	
	/**
	 * Build the types and the list of them
	 * 
	 * @param lines
	 * @return
	 */
	public static ArrayList<BagType> buildList(ArrayList<String> lines) {
		ArrayList<BagType> typeList = new ArrayList<>();
		
		// Until we have all definitions
		outer:
		for(int i = 0; lines.size() > 0; i = (++i == lines.size() ? 0 : i)) {
			
			// Separate into relevant parts
			String[] split = lines.get(i).split(" bags contain "),
					 contained = split[1].split(" bags?[,.] ?");
			String typeName = split[0];
			
			HashMap<BagType, Integer> containsMap = new HashMap<>();
			
			// Don't build for empty bags
			if(!contained[0].equals("no other")) {
				// Build contain map
				for(String s : contained) {
					// Isolate name
					BagType comparer = new BagType(s.substring(s.indexOf(' ') + 1));
					
					// Make sure we have the required type defined
					if(!typeList.contains(comparer)) {
						continue outer;
					}
					
					// Puts the right type with its number
					containsMap.put(typeList.get(typeList.indexOf(comparer)), Integer.parseInt(s.substring(0, s.indexOf(' '))));
				}
			}
			
			// Define the new type
			typeList.add(new BagType(typeName, containsMap));
			lines.remove(i--);
		}
		
		return typeList;
	}
}

class BagType {
	String name;
	
	HashMap<BagType, Integer> containsMap;
	
	/**
	 * Create a bag type representation
	 * 
	 * @param name Name of the bag, e.g. "shiny gold"
	 * @param contiansMap Map of containable bag types
	 */
	public BagType(String name, HashMap<BagType, Integer> containsMap) {
		this.name = name;
		this.containsMap = containsMap;
	}
	
	/**
	 * An empty type for comparison purposes
	 * 
	 * @param name
	 */
	public BagType(String name) {
		this.name = name;
		
		containsMap = null;
	}
	
	/**
	 * Counts the number of bags contained by this bag
	 * 
	 * @return
	 */
	public int countBags() {
		int sum = 1; // This bag
		
		// Count the bags inside
		for(BagType b : containsMap.keySet()) {
			sum += b.countBags() * containsMap.get(b);
		}
		
		return sum;
	}
	
	/**
	 * Determine if this contains a given type at some point
	 * 
	 * @param typeName
	 * @return
	 */
	public boolean contains(String typeName) {
		// If the key is the type we're looking for or contains the type
		for(BagType b : containsMap.keySet()) {
			if(b.equals(new BagType(typeName)) || b.contains(typeName)) return true;
		}
		
		return false;
	}
	
	// Getty boi
	public String getName() { return name; }
	
	@Override
	public boolean equals(Object o) {
		// Equal to another bag if they have the same name
		if(o instanceof BagType) {
			return ((BagType) o).getName().equals(name);
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return name + ": " + containsMap;
	}
}
