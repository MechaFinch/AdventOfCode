package year2023;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import global.util.AdventUtil;
import global.util.Pair;

/**
 * Day 11
 * 
 * Cosmic Expansion
 */
public class Day11 {
    public static void run(File f) throws IOException {
        part1(AdventUtil.inputList(f));
    }
    
    /**
     * Parse into a grid. For any row or column with no #, add another empty row/column
     * Find and sum the manhattan distance between each pair of #
     * 
     * @param lines
     */
    private static void part1(List<String> lines) {
        // 2 for part 1, 1000000 for part 2
        final int expansionFactor = 1_000_000;
        
        // we could do this in-place
        // but nah
        char[][] originalMap = new char[lines.get(0).length()][lines.size()];
        
        Set<Integer> emptyRows = new HashSet<>(),
                     emptyColumns = new HashSet<>();
        
        // get map & empty rows
        for(int y = 0; y < lines.size(); y++) {
            String ln = lines.get(y);
            
            // we can count empty rows here
            if(!ln.contains("#")) {
                //System.out.println("row: " + y);
                emptyRows.add(y);
            }
            
            for(int x = 0; x < ln.length(); x++) {
                originalMap[x][y] = ln.charAt(x);
            }
        }
        
        // get empty columns
        for(int x = 0; x < originalMap.length; x++) {
            boolean empty = true;
            
            for(int y = 0; y < originalMap[0].length; y++) {
                if(originalMap[x][y] != '.') {
                    empty = false;
                    break;
                }
            }
            
            if(empty) {
                //System.out.println("column: " + x);
                emptyColumns.add(x);
            }
        }
        
        // construct adjusted map
        List<Pair<Integer, Integer>> adjustedMap = new ArrayList<>(); // list of just galaxy coordinates
        
        for(int ox = 0, ax = 0; ox < originalMap.length; ox++, ax++) {
            if(emptyColumns.contains(ox)) {
                ax += expansionFactor - 1;
            }
            
            for(int oy = 0, ay = 0; oy < originalMap[0].length; oy++, ay++) {
                if(emptyRows.contains(oy)) {
                    ay += expansionFactor - 1;
                }
                
                //adjustedMap[ax][ay] = originalMap[ox][oy];
                if(originalMap[ox][oy] == '#') {
                    adjustedMap.add(new Pair<>(ax, ay));
                }
            }
        }
        
        // find distances
        long sum = 0;
        
        for(int i = 0; i < adjustedMap.size(); i++) {
            Pair<Integer, Integer> g1 = adjustedMap.get(i);
            
            for(int j = i + 1; j < adjustedMap.size(); j++) {
                Pair<Integer, Integer> g2 = adjustedMap.get(j);
                
                int dist = Math.abs(g1.a() - g2.a()) + Math.abs(g1.b() - g2.b());
                
                //System.out.printf("(%s, %s): %s%n", i, j, dist);
                
                sum += dist;
            }
        }
        
        System.out.println(sum);
    }
}
