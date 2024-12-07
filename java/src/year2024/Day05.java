package year2024;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import global.util.AdventUtil;

public class Day05 {
    public static void run(File f) throws IOException {
        part1(AdventUtil.inputList(f));
    }
    
    private static void part1(List<String> lines) {
        // Parse rules
        Map<Integer, Set<Integer>> notBeforeMap = new HashMap<>();
        
        int idx = 0;
        String ln;
        
        while(!(ln = lines.get(idx++)).isBlank()) {
            String[] split = ln.split("\\|");
            int before = Integer.parseInt(split[0]),
                after = Integer.parseInt(split[1]);
            
            if(!notBeforeMap.containsKey(before)) {
                notBeforeMap.put(before, new HashSet<>());
            }
            
            notBeforeMap.get(before).add(after);
        }
        
        // Parse updates
        List<List<Integer>> updates = new ArrayList<>();
        for(; idx < lines.size(); idx++) {
            updates.add(Arrays.stream(lines.get(idx).split(",")).map(Integer::parseInt).toList());
        }
        
        // Check updates
        int sum1 = 0, sum2 = 0;
        
        for(List<Integer> update : updates) {
            Set<Integer> printed = new HashSet<>();
            
            boolean valid = true;
            
            // Check validity
            out:
            for(int i : update) {
                Set<Integer> notBefore = notBeforeMap.get(i);
                
                if(notBefore != null) {
                    for(int cantBeBefore : notBeforeMap.get(i)) {
                        if(printed.contains(cantBeBefore)) {
                            valid = false;
                            break out;
                        }
                    }
                }
                
                printed.add(i);
            }
            
            if(valid) {
                // Sum for part 1
                sum1 += update.get(update.size() / 2);
            } else {
                // make modifiable
                update = new ArrayList<>(update);
                
                // Fix order for part 2
                Collections.sort(update, (a, b) -> {
                    if(notBeforeMap.containsKey(a)) {
                        // a must be before anything here
                        if(notBeforeMap.get(a).contains(b)) {
                            return -1;
                        }
                    }
                    
                    if(notBeforeMap.containsKey(b)) {
                        // b must be before anything here
                        if(notBeforeMap.get(b).contains(a)) {
                            return 1;
                        }
                    }
                    
                    return 0;
                });
                
                sum2 += update.get(update.size() / 2);
            }
        }
        
        System.out.println("Part 1: " + sum1);
        System.out.println("Part 2: " + sum2);
    }
}
