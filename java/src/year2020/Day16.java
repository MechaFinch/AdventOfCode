package year2020;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import global.AdventUtil;

public class Day16 {
	
	public static void main(String[] args) throws IOException {
		System.out.println(part2(AdventUtil.inputList(AdventUtil.DEFAULT)));
	}
	
	/**
	 * Part 2
	 * 
	 * @param input
	 * @return
	 */
	public static long part2(ArrayList<String> input) {
		// Get list of valid tickets 
		ArrayList<String> validTickets = new ArrayList<>(input);
		part1(validTickets);
		
		// Get map of fields to ranges
		HashMap<String, ArrayList<Integer>> rangeMap = generateRanges(input);
		
		/*
		 * 1. For each column, assemble a list of valid fields
		 * 2. Find columns with only 1 valid field, and assign them that field, removing it from other columns with that field as an option
		 * 3. Repeat 2 until all columns are assigned or the necessary fields have been found 
		 */
		
		// Get our ticket
		String[] ourTicket = input.get(input.indexOf("your ticket:") + 1).split(",");
		
		// Arrange data by column
		int[][] data = new int[ourTicket.length][validTickets.size()];
		for(int i = 0; i < validTickets.size(); i++) {
			String[] ticket = validTickets.get(i).split(",");
			
			for(int j = 0; j < ticket.length; j++) {
				data[j][i] = Integer.parseInt(ticket[j]);
			}
		}
		
		// We can't have a normal array because generics
		ArrayList<ArrayList<String>> validFields = new ArrayList<>();
		
		// For each column, assemble a list of valid fields 
		for(int c = 0; c < data.length; c++) {
			ArrayList<String> valid = new ArrayList<>();
			
			// Loop over each key to the range map, if one works add it to the list
			fieldLoop:
			for(String k : rangeMap.keySet()) {
				ArrayList<Integer> ranges = rangeMap.get(k);
				
				// Make sure each value in the column fits the range
				for(int i = 0; i < data[c].length; i++) {
					int v = data[c][i];
					
					// Check that any range is valid
					boolean isValid = false;
					for(int j = 0; j < ranges.size(); j += 2) {
						if(v >= ranges.get(j) && v <= ranges.get(j + 1)) {
							isValid = true;
							break;
						}
					}
					
					// This data point is invalid
					if(!isValid) continue fieldLoop;
				}
				
				// This field works for the column
				valid.add(k);
			}
			
			validFields.add(valid);
		}
		
		// Assign fields until done
		long product = 1;
		for(int assigned = 0; assigned < validFields.size();) {
			// Go over columns with only 1 valid field
			for(int i = 0; i < validFields.size(); i++) {
				ArrayList<String> valid = validFields.get(i);
				
				if(valid.size() == 1) {
					String field = valid.get(0);
					
					// Remove the field from all others
					for(ArrayList<String> other : validFields) {
						other.remove(field);
					}
					
					assigned++;
					
					// If it's one of the values we want, give the value
					if(field.startsWith("departure")) product *= Integer.parseInt(ourTicket[i]);
				}
			}
		}
		
		return product;
	}
	
	/**
	 * Part 1
	 * 
	 * @param input
	 * @return
	 */
	public static int part1(ArrayList<String> input) {
		// Create list of keys before they get consumed
		ArrayList<String> keyList = new ArrayList<>();
		for(String s : input) {
			if(s.equals("")) break;
			keyList.add(s.split(":")[0]);
		}
		
		// Get our ranges, also removes header
		HashMap<String, ArrayList<Integer>> map = generateRanges(input);
		
		// Remove until we reach nearby
		while(!input.remove(0).startsWith("nearby"));
		
		// Run through nearby tickets
		int sum = 0;
		for(int l = 0; l < input.size(); l++) {
			String[] fields = input.get(l).split(",");
			
			// Check each field
			field:
			for(int i = 0; i < fields.length; i++) {
				int v = Integer.parseInt(fields[i]);
				
				// Check all ranges
				for(String k : map.keySet()) {
					ArrayList<Integer> range = map.get(k);
					
					// Ranges
					for(int j = 0; j < range.size(); j += 2) {
						if(v >= range.get(j) && v <= range.get(j + 1)) continue field;
					}
				}
				
				// Not in any range
				sum += v;
				
				// Remove to support part 2
				input.remove(l--);
			}
		}
		
		return sum;
	}
	
	/**
	 * Generates the range map, consuming the lines used
	 * 
	 * @param input
	 * @return
	 */
	public static HashMap<String, ArrayList<Integer>> generateRanges(ArrayList<String> input) {
		HashMap<String, ArrayList<Integer>> map = new HashMap<>();
		
		// Until we reach the end of the notes
		String line = "";
		while(!(line = input.remove(0)).equals("")) {
			String[] split = line.split(": ");
			String k = split[0];
			ArrayList<Integer> v = new ArrayList<>();
			
			// Parse ranges into ordered min max pairs
			for(String s : split[1].split("-| or ")) v.add(Integer.parseInt(s));
			
			map.put(k, v);
		}
		
		return map;
	}
}
