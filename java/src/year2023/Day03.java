package year2023;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import global.util.AdventUtil;
import global.util.Pair;

/**
 * Day 3
 * Gears
 */
public class Day03 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    /**
     * A part number in the diagram
     * @param startPosition position of MSD
     * @param width nubmer of digits
     * @param value value
     */
    private record PartNumber(Pair<Integer, Integer> startPosition, int width, int value) {
        
    }
    
    /**
     * Find and sum the gear ratios
     * 
     * @param strings
     */
    private static void part2(List<String> strings) {
        Map<Pair<Integer, Integer>, Character> symbolMap = new HashMap<>();
        Map<Integer, List<PartNumber>> partNumberRows = new HashMap<>();
        
        // parse input to find numbers & symbols
        for(int sx = 0, sy = 0, number = -1, y = 0; y < strings.size(); y++) {
            String s = strings.get(y);
            
            for(int x = 0; x < s.length(); x++) {
                char c = s.charAt(x);
                
                if(Character.isDigit(c)) {
                    // if we have a digit, build a part number
                    if(number == -1) {
                        sx = x;
                        sy = y;
                        number = 0;
                    }
                    
                    number = (number * 10) + (c - '0');
                } else {
                    if(number != -1) {
                        // if we finished a number, record it
                        addPartNumber(partNumberRows, new PartNumber(new Pair<>(sx, sy), x - sx, number));
                        number = -1;
                    }
                    
                    if(c != '.') {
                        // if we have a symbol, record it
                        symbolMap.put(new Pair<>(x, y), c);
                    }
                }
            }
            
            // if a number ends at the last charactr, record it
            if(number != -1) {
                addPartNumber(partNumberRows, new PartNumber(new Pair<>(sx, sy), s.length() - sx, number));
                number = -1;
            }
        }
        
        int sum = 0;
        
        // for each symbol, check each part number on the same row (+/- 1) for adjacency to count
        // sum those with exactly 2 adjacent
        for(Pair<Integer, Integer> symbolCoords : symbolMap.keySet()) {
            List<PartNumber> partNumbersAbove = partNumberRows.get(symbolCoords.b() - 1),
                             partNumbersInline = partNumberRows.get(symbolCoords.b()),
                             partNumbersBelow = partNumberRows.get(symbolCoords.b() + 1),
                             partNumbersAdjacent = new ArrayList<>();
            
            // check inline
            if(partNumbersInline != null) {
                for(PartNumber pn : partNumbersInline) {
                    if(partNumbersAdjacent.size() > 2) break;
                    
                    if(pn.startPosition().a() <= symbolCoords.a() + 1 &&
                       pn.startPosition().a() + pn.width() >= symbolCoords.a()) {
                        partNumbersAdjacent.add(pn);
                    }
                }
            }
            
            // check above
            if(partNumbersAbove != null) {
                for(PartNumber pn : partNumbersAbove) {
                    if(partNumbersAdjacent.size() > 2) break;
                    
                    if(pn.startPosition().a() <= symbolCoords.a() + 1 &&
                       pn.startPosition().a() + pn.width() >= symbolCoords.a()) {
                        partNumbersAdjacent.add(pn);
                    }
                }
            }
            
            // check below
            if(partNumbersBelow != null) {
                for(PartNumber pn : partNumbersBelow) {
                    if(partNumbersAdjacent.size() > 2) break;
                    
                    if(pn.startPosition().a() <= symbolCoords.a() + 1 &&
                       pn.startPosition().a() + pn.width() >= symbolCoords.a()) {
                        partNumbersAdjacent.add(pn);
                    }
                }
            }
            
            // is this a gear?
            if(partNumbersAdjacent.size() == 2) {
                // yes
                sum += partNumbersAdjacent.get(0).value() * partNumbersAdjacent.get(1).value();
            }
        }
        
        System.out.println(sum);
    }
    
    /**
     * Adds a part number to the map, keyed by row
     * @param partNumberRows
     * @param pn
     */
    private static void addPartNumber(Map<Integer, List<PartNumber>> partNumberRows, PartNumber pn) {
        int y = pn.startPosition().b();
        
        if(!partNumberRows.containsKey(y)) {
            partNumberRows.put(y, new ArrayList<>());
        }
        
        partNumberRows.get(y).add(pn);
    }
    
    /**
     * Find and sum the part numbers adjacent to a symbol
     * 
     * @param strings
     */
    private static void part1(List<String> strings) {
        Map<Pair<Integer, Integer>, Character> symbolMap = new HashMap<>();
        List<PartNumber> partNumbers = new ArrayList<>();
        
        // parse input to find numbers & symbols
        for(int sx = 0, sy = 0, number = -1, y = 0; y < strings.size(); y++) {
            String s = strings.get(y);
            
            for(int x = 0; x < s.length(); x++) {
                char c = s.charAt(x);
                
                if(Character.isDigit(c)) {
                    // if we have a digit, build a part number
                    if(number == -1) {
                        sx = x;
                        sy = y;
                        number = 0;
                    }
                    
                    number = (number * 10) + (c - '0');
                } else {
                    if(number != -1) {
                        // if we finished a number, record it
                        partNumbers.add(new PartNumber(new Pair<>(sx, sy), x - sx, number));
                        number = -1;
                    }
                    
                    if(c != '.') {
                        // if we have a symbol, record it
                        symbolMap.put(new Pair<>(x, y), c);
                    }
                }
            }
            
            // if a number ends at the last charactr, record it
            if(number != -1) {
                partNumbers.add(new PartNumber(new Pair<>(sx, sy), s.length() - sx, number));
                number = -1;
            }
        }
        
        // debug
        for(PartNumber pn : partNumbers) {
            System.out.println(pn);
        }
        
        System.out.println();
        
        for(Entry<Pair<Integer, Integer>, Character> e : symbolMap.entrySet()) {
            System.out.printf("(%s, %s): '%s'%n", e.getKey().a(), e.getKey().b(), e.getValue());
        }
        
        System.out.println();
        
        int sum = 0;
        
        // find & sum part numbers
        for(PartNumber pn : partNumbers) {
            int sx = pn.startPosition().a(),
                sy = pn.startPosition().b();
            
            boolean hasSymbol = false;
            
            // check start & end
            if(symbolMap.containsKey(new Pair<>(sx - 1, sy)) ||
               symbolMap.containsKey(new Pair<>(sx + pn.width(), sy))) {
                hasSymbol = true;
            }
            
            // check above & below
            for(int xOffs = -1; xOffs < pn.width() + 1; xOffs++) {
                if(hasSymbol) break;
                
                if(symbolMap.containsKey(new Pair<>(sx + xOffs, sy - 1)) || // above
                   symbolMap.containsKey(new Pair<>(sx + xOffs, sy + 1))) { // below
                    hasSymbol = true;
                }
            }
            
            if(hasSymbol) {
                // is a part number, add to sum
                System.out.println("Adding " + pn.value());
                sum += pn.value();
            }
        }
        
        System.out.println();
        System.out.println(sum);
    }
}
