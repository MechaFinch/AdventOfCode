package year2023;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import global.util.AdventUtil;

/**
 * Day 4
 * Scratchcards
 */
public class Day04 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    /**
     * Each line has a list of winning numbers and a list of your numbers
     * For each of your numbers matching a winning numbers, acquire a subsequent line.
     * @param lines
     */
    private static void part2(List<String> lines) {
        // parse all cards, as they get reused
        List<Integer> cardValues = new ArrayList<>();
        
        for(String ln : lines) {
            String winningString = ln.substring(ln.indexOf(':') + 1, ln.indexOf('|')).trim(),
                   cardString = ln.substring(ln.indexOf('|') + 1).trim();
            
            Set<Integer> winningNumbers = new HashSet<>();
            
            // parse winning numbers
            for(String s : winningString.split("\s+")) {
                winningNumbers.add(Integer.parseInt(s));
            }
            
            // parse card numbers & determine value
            int value = 0;
            
            for(String s : cardString.split("\s+")) {
                if(winningNumbers.contains(Integer.parseInt(s))) {
                    value++;
                }
            }
            
            cardValues.add(value);
        }
        
        // play the game
        int sum = 0;
        
        for(int i = 0; i < cardValues.size(); i++) {
            sum += playRecursively(cardValues, i);
        }
        
        System.out.println(sum);
    }
    
    /**
     * Simple recursive play
     * 
     * @param cardValues
     * @param index
     * @return
     */
    private static int playRecursively(List<Integer> cardValues, int index) {
        int sum = 1,
            value = cardValues.get(index);
        
        for(int i = 0; i < value; i++) {
            sum += playRecursively(cardValues, index + i + 1);
        }
        
        return sum;
    }
    
    /**
     * For each line:
     * - Parse two lists of numbers, the first being winning numbers and the second being card numbers
     * - For each winning number found in the card numbers, multiply the point value by two. 
     * - Value starts at zero, and is set to 1 by the first winning number found
     * Find the sum of card values
     * 
     * @param lines
     */
    private static void part1(List<String> lines) {
        int sum = 0;
        
        for(String ln : lines) {
            String winningString = ln.substring(ln.indexOf(':') + 1, ln.indexOf('|')).trim(),
                   cardString = ln.substring(ln.indexOf('|') + 1).trim();
            
            Set<Integer> winningNumbers = new HashSet<>();
            
            // parse winning numbers
            for(String s : winningString.split("\s+")) {
                winningNumbers.add(Integer.parseInt(s));
            }
            
            // parse card numbers & determine value
            int value = 1;
            
            for(String s : cardString.split("\s+")) {
                if(winningNumbers.contains(Integer.parseInt(s))) {
                    value <<= 1;
                }
            }
            
            value >>= 1;
            sum += value;
        }
        
        System.out.println(sum);
    }
}
