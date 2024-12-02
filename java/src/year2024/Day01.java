package year2024;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import global.util.AdventUtil;

public class Day01 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    private static void part2(List<String> lines) {
        // Get left and right lists
        List<Integer> lefts = new ArrayList<>(lines.stream().map(ln -> Integer.parseInt(ln.split("\s+")[0])).toList());
        List<Integer> rights = new ArrayList<>(lines.stream().map(ln -> Integer.parseInt(ln.split("\s+")[1])).toList());
        
        // Count # of times each value appears in rights
        Map<Integer, Integer> counts = new HashMap<>();
        rights.forEach(x -> counts.put(x, counts.getOrDefault(x, 0) + 1));
        
        // Sum of left * # in right
        int sum = 0;
        
        for(Integer l : lefts) {
            sum += l * counts.getOrDefault(l, 0);
        }
        
        System.out.println(sum);
    }
    
    private static void part1(List<String> lines) {
        // Get left and right lists
        List<Integer> lefts = new ArrayList<>(lines.stream().map(ln -> Integer.parseInt(ln.split("\s+")[0])).toList());
        List<Integer> rights = new ArrayList<>(lines.stream().map(ln -> Integer.parseInt(ln.split("\s+")[1])).toList());
        
        // Sort to align
        Collections.sort(lefts);
        Collections.sort(rights);
        
        // Sum of distances
        int sum = 0;
        
        for(int i = 0; i < lefts.size(); i++) {
            sum += Math.abs(lefts.get(i) - rights.get(i));
        }
        
        System.out.println(sum);
    }
}
