package year2023;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import global.util.AdventUtil;

/**
 * Day 13
 * 
 * Mirrors
 */
public class Day13 {
    public static void run(File f) throws IOException {
        part1(AdventUtil.inputList(f));
    }
    
    /**
     * Separate inputs into patterns
     * For each vertical line, check for mirroring top-to-bottom
     * For each horizontal line, check for mirroring left-to-right
     * When mirroring is found, add 1x column to sum if vertical or 100x row if horizontal
     * 
     * @param lines
     */
    private static void part1(List<String> lines) {
        long sum = 0;
        int requiredErrors = 1; // 0 for part 1, 1 for part 2
        
        List<String> pattern = new ArrayList<>();
        
        for(String ln : lines) {
            if(ln.isBlank()) {
                // process pattern
                sum += checkPattern(pattern, requiredErrors);
                
                pattern.clear();
            } else {
                // add to pattern
                pattern.add(ln);
            }
        }
        
        // get last
        sum += checkPattern(pattern, requiredErrors);
        
        System.out.println(sum);
    }
    
    /**
     * Checks a single pattern
     * 
     * @param pattern
     * @return
     */
    private static int checkPattern(List<String> pattern, int requiredErrors) {
        int xlen = pattern.get(0).length(),
            ylen = pattern.size();
        
        // check vertical lines
        // each separating line
        nextSeparator:
        for(int x = 0; x < xlen - 1; x++) {
            int zMax = Math.min(x, xlen - x - 2),
                errors = 0;
            
            // each row
            for(int y = 0; y < ylen; y++) {
                String rs = pattern.get(y);
                
                // each mirrored column
                for(int z = 0; z <= zMax; z++) {
                    if(rs.charAt(x - z) != rs.charAt(x + z + 1)) {
                        errors++;
                        
                        if(errors > requiredErrors) {
                            continue nextSeparator;
                        }
                    }
                }
            }
            
            // if we're here, we found the mirroring
            if(errors == requiredErrors) {
                return x + 1;
            }
        }
        
        // check horizontal lines
        // each separating line
        nextSeparator:
        for(int y = 0; y < ylen - 1; y++) {
            int zMax = Math.min(y, ylen - y - 2),
                errors = 0;
            
            // each column
            for(int x = 0; x < xlen; x++) {
                // each mirrored row
                for(int z = 0; z <= zMax; z++) {
                    if(pattern.get(y - z).charAt(x) != pattern.get(y + z + 1).charAt(x)) {
                        errors++;
                        
                        if(errors > requiredErrors) {
                            continue nextSeparator;
                        }
                    }
                }
            }
            
            // if we're here, found the mirroring
            if(errors == requiredErrors) {
                return 100 * (y + 1);
            }
        }
        
        throw new IllegalArgumentException("No valid mirroring in pattern");
    }
}
