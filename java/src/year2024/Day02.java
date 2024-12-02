package year2024;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import global.util.AdventUtil;

public class Day02 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    private static void part2(List<String> lines) {
        List<ArrayList<Integer>> reports = lines.stream()
                                           .map(l -> new ArrayList<>(Arrays.stream(l.split("\\s+")).map(Integer::parseInt).toList()))
                                           .toList();
        
        int safeCount = 0;
        outer:
        for(List<Integer> report : reports) {
            if(safe(report)) {
                safeCount++;
                continue;
            }
            
            for(int i = 0; i < report.size(); i++) {
                int v = report.remove(i);
                
                if(safe(report)) {
                    safeCount++;
                    continue outer;
                }
                
                report.add(i, v);
            }
        }
        
        System.out.println(safeCount);
    }
    
    private static void part1(List<String> lines) {
        List<List<Integer>> reports = lines.stream()
                                           .map(l -> Arrays.stream(l.split("\\s+")).map(Integer::parseInt).toList())
                                           .toList();
        
        // Count safe
        int safeCount = 0;
        for(List<Integer> report : reports) {
            if(safe(report)) {
                safeCount++;
            }
        }
        
        System.out.println(safeCount);
    }
    
    /**
     * @param report
     * @return true if report is safe
     */
    private static boolean safe(List<Integer> report) {
        int last = report.get(0);
        boolean increasing = (report.get(1) - last) > 0;
        
        for(int i = 1; i < report.size(); i++) {
            int next = report.get(i);
            int difference = next - last;
            int change = Math.abs(difference);
            
            // safety criteria
            if(change < 1 || change > 3 || (increasing != (difference > 0))) {
                return false;
            }
            
            last = next;
        }
        
        return true;
    }
}
