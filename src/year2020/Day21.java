package year2020;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import global.AdventUtil;

public class Day21 {
	
	public static void main(String[] args) throws IOException {
		System.out.println(part2(AdventUtil.inputLines(AdventUtil.DEFAULT)));
	}
	
	/**
	 * Part 2
	 * 
	 * @param input
	 * @return
	 */
	public static String part2(String[] input) {
		Map<String, String> allergenMap = findAllergens(input);
		List<String> dangerous = new ArrayList<>(Arrays.asList(allergenMap.keySet().toArray(new String[0]))), // Make sure we can rearrange
					 allergens = new ArrayList<>();
		
		// Build allergen list so we can sort in parallel
		for(String d : dangerous) allergens.add(allergenMap.get(d));
		
		// Sort both at once (insertion)
		for(int i = 1; i < allergens.size(); i++) {
			String a = allergens.get(i),
				   b = dangerous.get(i);
			
			int j = i - 1;
			for(; j >= 0 && allergens.get(j).compareTo(a) > 0; j--) {
				allergens.set(j + 1, allergens.get(j));
				dangerous.set(j + 1, dangerous.get(j));
			}
			
			allergens.set(j + 1, a);
			dangerous.set(j + 1, b);
		}
		
		// Build CSV
		String csv = "";
		for(String s : dangerous) csv += "," + s;
		
		return csv.substring(1);
	}
	
	/**
	 * Part 1
	 * 
	 * @param input
	 * @return
	 */
	public static int part1(String[] input) {
		Map<String, String> allergenMap = findAllergens(input);
		System.out.println(allergenMap);
		
		// Go over all ingredients and count those not in the map
		int count = 0;
		for(String s : input) {
			for(String i : s.substring(0, s.indexOf(" (")).split(" ")) {
				if(!allergenMap.keySet().contains(i)) count++;
			}
		}
		
		return count;
	}
	
	/**
	 * Generates a map of allergens, from ingredient to allergen
	 * 
	 * @param input
	 * @return
	 */
	public static Map<String, String> findAllergens(String[] input) {
		Map<String, String> allergenMap = new HashMap<>();
		List<List<String>> ingredients = new ArrayList<>(),
						   allergens = new ArrayList<>();
		
		// Fill out ingredient & allergen lists
		for(String s : input) {
			String[] split = s.split(" \\(contains ");
			
			// Arraylists so that we can manipulate them
			ingredients.add(new ArrayList<>(Arrays.asList(split[0].split(" "))));
			allergens.add(new ArrayList<>(Arrays.asList(split[1].substring(0, split[1].length() - 1).split(", "))));
		}
		
		// Track number of total allergens
		int totalAllergens = 0;
		for(List<String> al : allergens) totalAllergens += al.size();
		
		// Until we've emptied the lists
		int i = 0;
		while(totalAllergens > 0) {
			String allergen = "";
			
			// Find a food with 1 unknown ingredient
			for(; i < ingredients.size(); i++) {
				if(allergens.get(i).size() == 1) {
					allergen = allergens.get(i).get(0);
					break;
				}
			}
			
			/*
			System.out.println(ingredients);
			System.out.println(allergens);
			System.out.println(allergen);
			System.out.println();
			*/
			
			// Failed
			if(allergen.equals("")) {
				System.out.println(ingredients);
				System.out.println(allergens);
				throw new IllegalStateException("No single-allergen foods found");
			}
			
			// Narrow ingredient to those common with others with this allergen
			List<String> possible = new ArrayList<>(ingredients.get(i));
			
			for(int j = 0; j < ingredients.size(); j++) {
				if(!allergens.get(j).contains(allergen)) continue;
				
				List<String> compareList = ingredients.get(j);
				
				for(int k = 0; k < possible.size(); k++) {
					if(!compareList.contains(possible.get(k))) possible.remove(k--);
				}
			}
			
			// Failed by invalid state
			if(possible.size() == 0) {
				System.out.println(ingredients);
				System.out.println(allergens);
				throw new IllegalStateException("No possible ingredients: " + allergen);
			}
			
			// Failed by not enough information, continue search from next ingredient
			if(possible.size() != 1) {
				i++;
				continue;
			}
			
			// Update map
			allergenMap.put(possible.get(0), allergen);
			
			// Remove the allergen from all lists and the ingredient containing it
			for(int j = 0; j < ingredients.size(); j++) {
				ingredients.get(j).remove(possible.get(0));
				if(allergens.get(j).remove(allergen)) totalAllergens--;
				
				// Fully remove if empty
				if(ingredients.get(j).size() == 0) {
					ingredients.remove(j);
					allergens.remove(j);
					j--;
				}
			}
			
			// Set before we continue
			i = 0;
		}
		
		return allergenMap;
	}
}