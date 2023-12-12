package year2023;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import global.util.AdventUtil;

/**
 * Day 12
 * 
 * 温泉じゃないよホットスプリングだよ！
 */
public class Day12 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    /**
     * Part 1 but the lists are way longer
     * 
     * @param lines
     */
    private static void part2(List<String> lines) {
        long sum = 0;
        
        for(String ln : lines) {
            // parse
            int sep = ln.indexOf(' ');
            String rowString = ln.substring(0, sep),
                   bigRow = rowString,
                   groupsString = ln.substring(sep + 1);
            
            List<Integer> groups = AdventUtil.toIntList(groupsString, ","),
                          bigGroups = new ArrayList<>(groups);
            
            // unfold
            for(int i = 0; i < 4; i++) {
                bigRow += "?" + rowString;
                bigGroups.addAll(groups);
            }
            
            // minSpace = sum(groups) + num(groups) - 1
            int minSpace = -1;
            for(int g : bigGroups) {
                minSpace += g + 1;
            }
            
            long possible = permuteRecursive(bigRow, bigGroups, 0, 0, minSpace, new HashMap<>()); 
            
            //System.out.println(possible);
            
            sum += possible;
        }
        
        System.out.println(sum);
    }
    
    /**
     * Parse rows, determine what is set by knowns/pattern, compute permutations of unknowns
     * Sum number of permutations
     * 
     * @param lines
     */
    private static void part1(List<String> lines) {
        int sum = 0;
        
        for(String ln : lines) {
            // parse
            int sep = ln.indexOf(' ');
            String rowString = ln.substring(0, sep),
                   groupsString = ln.substring(sep + 1);
            
            List<Integer> groups = AdventUtil.toIntList(groupsString, ",");
            
            // minSpace = sum(groups) - num(groups) - 1
            int minSpace = -1;
            for(int g : groups) {
                minSpace += g - 1;
            }
            
            sum += permuteRecursive(rowString, groups, 0, 0, minSpace, new HashMap<>());
        }
        
        System.out.println(sum);
    }
    
    private static long permuteRecursive(String row, List<Integer> groups, int rowIndex, int groupIndex, int minSpace, Map<String, Long> remRowCache) {
        if(rowIndex >= row.length()) {
            // we have reached the end of the string
            if(groupIndex >= groups.size()) {
                // and have no remaining groups
                return 1;
            } else {
                // and haven't accounted for all groups
                return 0;
            }
        } else {
            // we have more to take care of
            if(groupIndex >= groups.size()) {
                // we're out of groups, so we can do a quick check
                for(int i = rowIndex; i < row.length(); i++) {
                    if(row.charAt(i) == '#') {
                        return 0;
                    }
                }
                
                return 1;
            }
            
            // do we have enough space to fit any remaining groups
            if(minSpace > row.length() - rowIndex) { 
                return 0;
            }
            
            int c = row.charAt(rowIndex);
            
            if(c == '.') {
                // working, move along
                return permuteRecursive(row, groups, rowIndex + 1, groupIndex, minSpace, remRowCache);
            } else if(c == '#') {
                // damaged, can we fit the next group here?
                int groupSize = groups.get(groupIndex);
                
                if(rowIndex + groupSize > row.length()) {
                    // it cannot fit
                    return 0;
                } else {
                    // it can physically fit
                    // have we seen this before?
                    String remRow = row.substring(rowIndex) + Integer.toString(groupIndex);// + Integer.toString(minSpace) + Integer.toString(rowIndex);
                    long val = remRowCache.getOrDefault(remRow, -1l);
                    
                    if(val != -1) {
                        // we have.
                        return val;
                    }
                    
                    // we haven't. is anything in the way?
                    for(int i = 0; i < groupSize; i++) {
                        if(row.charAt(rowIndex + i) == '.') {
                            // yes
                            remRowCache.put(remRow, 0l);
                            return 0;
                        }
                    }
                    
                    // no. can we end it?
                    if(rowIndex + groupSize == row.length() || row.charAt(rowIndex + groupSize) != '#') {
                        // yes                        
                        String rowA = replaceChar(row, rowIndex + groupSize, '.');
                        long possible = permuteRecursive(rowA, groups, rowIndex + groupSize + 1, groupIndex + 1, minSpace - groupSize - 1, remRowCache);
                        remRowCache.put(remRow, possible);
                        return possible;
                    } else {
                        // no
                        remRowCache.put(remRow, 0l);
                        return 0;
                    }
                }
            } else {
                // unknown. branch.
                String rowA = replaceChar(row, rowIndex, '#'),
                       rowB = replaceChar(row, rowIndex, '.');
                
                return permuteRecursive(rowA, groups, rowIndex, groupIndex, minSpace, remRowCache) +
                       permuteRecursive(rowB, groups, rowIndex, groupIndex, minSpace, remRowCache);
            }
        }
    }
    
    private static String replaceChar(String s, int i, char c) {
        if(i >= s.length()) return s;
        return s.substring(0, i) + c + s.substring(i + 1);
    }
}
