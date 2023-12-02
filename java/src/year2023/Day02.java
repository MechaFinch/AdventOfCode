package year2023;

import java.io.File;
import java.io.IOException;
import java.util.List;

import global.AdventUtil;

/**
 * Day 2
 * Cube Bag
 */
public class Day02 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    /**
     * Determine the minimum required red, green, and blue cubes for each game
     * Multiply these numbers for the power
     * Sum the powers
     * 
     * @param games
     */
    private static void part2(List<String> games) {
        long sum = 0;
        
        // for each
        for(String gameString : games) {
            gameString = gameString.toLowerCase();
            
            int colonIndex = gameString.indexOf(':');
            
            String idString = gameString.substring(5, colonIndex),
                   roundsString = gameString.substring(colonIndex + 1);
            
            long minRed = 0,
                 minGreen = 0,
                 minBlue = 0;
            
            // get ID
            int gameID = Integer.parseInt(idString);
            
            // split by round
            for(String round : roundsString.split(";")) {
                long r = 0,
                     g = 0,
                     b = 0;
                
                // split by color
                for(String color : round.split(",")) {
                    color = color.trim();
                    
                    // first is number, second is color
                    String[] colorParts = color.split(" ");
                    
                    int amount = Integer.parseInt(colorParts[0]);
                    
                    switch(colorParts[1]) {
                        case "red":
                            r += amount;
                            break;
                            
                        case "green":
                            g += amount;
                            break;
                            
                        case "blue":
                            b += amount;
                            break;
                    }
                }
                
                // check if we need more of a color
                if(r > minRed) {
                    minRed = r;
                }
                
                if(g > minGreen) {
                    minGreen = g;
                }
                
                if(b > minBlue) {
                    minBlue = b;
                }
            }
            
            // compute & sum power
            sum += minRed * minGreen * minBlue;
        }
        
        // done
        System.out.println(sum);
    }
    
    /**
     * Find which games are possible if they have 12 red, 13 green, 14 blue, and sum their IDs
     * 
     * @param games
     */
    private static void part1(List<String> games) {
        final int MAX_RED = 12,
                  MAX_GREEN = 13,
                  MAX_BLUE = 14;
        
        int sum = 0;
        
        // for each
        for(String gameString : games) {
            gameString = gameString.toLowerCase();
            
            int colonIndex = gameString.indexOf(':');
            
            String idString = gameString.substring(5, colonIndex),
                   roundsString = gameString.substring(colonIndex + 1);
            
            // get ID
            int gameID = Integer.parseInt(idString);
            boolean possible = true;
            
            // split by round
            for(String round : roundsString.split(";")) {
                int r = 0,
                    g = 0,
                    b = 0;
                
                // split by color
                for(String color : round.split(",")) {
                    color = color.trim();
                    
                    // first is number, second is color
                    String[] colorParts = color.split(" ");
                    
                    int amount = Integer.parseInt(colorParts[0]);
                    
                    switch(colorParts[1]) {
                        case "red":
                            r += amount;
                            break;
                            
                        case "green":
                            g += amount;
                            break;
                            
                        case "blue":
                            b += amount;
                            break;
                    }
                }
                
                // check if it's possible
                if(r > MAX_RED || g > MAX_GREEN || b > MAX_BLUE) {
                    possible = false;
                    break;
                }
            }
            
            // round is possible, add to sum
            if(possible) {
                sum += gameID;
            }
        }
        
        // done
        System.out.println(sum);
    }
}
