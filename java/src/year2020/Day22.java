package year2020;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import global.AdventUtil;

public class Day22 {
	
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
		// Separate
		List<Integer> player1 = new ArrayList<>(input.subList(1, input.indexOf("")).stream().mapToInt(Integer::parseInt).boxed().collect(Collectors.toList())),
					  player2 = new ArrayList<>(input.subList(input.indexOf("") + 2, input.size()).stream().mapToInt(Integer::parseInt).boxed().collect(Collectors.toList())),
					  winner = recursiveCombat(player1, player2) ? player1 : player2;
		
		// Scoring
		long score = 0;
		
		for(int i = 1; i <= winner.size(); i++) score += winner.get(winner.size() - i) * i;
		
		return score;
	}
	
	/**
	 * Plays a game of recursive combat, returning true if player1 wins
	 * 
	 * @param player1
	 * @param player2
	 * @return
	 */
	public static boolean recursiveCombat(List<Integer> player1, List<Integer> player2) {
		// Round histories
		List<List<Integer>> history1 = new ArrayList<>(),
							history2 = new ArrayList<>();
		
		while(true) {
			// Check histories
			int i1 = history1.indexOf(player1),
				i2 = history2.indexOf(player2);
			
			// Add to histories
			history1.add(new ArrayList<>(player1));
			history2.add(new ArrayList<>(player2));
			
			// Infinite game, p1 wins
			if(i1 != -1 && i1 == i2) {
				return true;
			}
			
			int p1 = player1.remove(0),
				p2 = player2.remove(0);
			
			// Can recurse
			if(player1.size() >= p1 && player2.size() >= p2) {
				// Run sub game
				List<Integer> np1 = new ArrayList<>(player1.subList(0, p1)),
							  np2 = new ArrayList<>(player2.subList(0, p2));
				
				if(recursiveCombat(np1, np2)) { // Add them cards
					player1.add(p1);
					player1.add(p2);
				} else {
					player2.add(p2);
					player2.add(p1);
				}
			} else if(p1 > p2) {
				player1.add(p1);
				player1.add(p2);
			} else {
				player2.add(p2);
				player2.add(p1);
			}
			
			// Check sizes
			if(player1.size() == 0 || player2.size() == 0) {
				return player2.size() == 0;
			}
		}
	}
	
	/**
	 * Part 1
	 * 
	 * @param input
	 * @return
	 */
	public static long part1(ArrayList<String> input) {
		// Separate
		List<Integer> player1 = new ArrayList<>(input.subList(1, input.indexOf("")).stream().mapToInt(Integer::parseInt).boxed().collect(Collectors.toList())),
					  player2 = new ArrayList<>(input.subList(input.indexOf("") + 2, input.size()).stream().mapToInt(Integer::parseInt).boxed().collect(Collectors.toList()));
		
		// Run game
		while(player1.size() > 0 && player2.size() > 0) {
			int p1 = player1.remove(0),
				p2 = player2.remove(0);
			
			// Check & add
			if(p1 > p2) {
				player1.add(p1);
				player1.add(p2);
			} else {
				player2.add(p2);
				player2.add(p1);
			}
		}
		
		// Calculate score
		long score = 0;
		List<Integer> winner = (player1.size() == 0 ? player2 : player1);
		
		for(int i = 1; i <= winner.size(); i++) score += winner.get(winner.size() - i) * i;
		
		return score;
	}
}
