package year2023;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import global.util.AdventUtil;

/**
 * Day 1
 * Trebuchet
 */
public class Day01 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    /**
     * Part 1
     * Find the first and last digit in each string, concatonate to a number, sum for all strings
     * Digits can be spelled out
     * 
     * @param strings
     */
    private static void part2(List<String> strings) {
        int sum = 0;
        
        for(String s : strings) {
            s = s.toLowerCase();
            
            int d1 = '0', d2 = '0'; // digit 1 (first), digit 2 (last)
            boolean h1 = false, h2 = false; // has 1, has 2
            
            // check for spelled out numbers
            Map<Integer, Character> firstIndexMap = new HashMap<>(),
                                    lastIndexMap = new HashMap<>();
            for(Entry<String, Character> entry : AdventUtil.DIGIT_WORD_MAP.entrySet()) {
                String digit = entry.getKey();
                char c = entry.getValue();
                
                int firstIndex = s.indexOf(digit),
                    lastIndex = s.lastIndexOf(digit);
                
                if(firstIndex != -1) {
                    firstIndexMap.put(firstIndex, c);
                    lastIndexMap.put(lastIndex, c);
                }
            }
            
            // find first and last digits
            for(int i = 0; i < s.length(); i++) {
                if(!h1) {
                    char c = s.charAt(i);
                    
                    if(Character.isDigit(c)) {
                        d1 = c;
                        h1 = true;
                    } else if(firstIndexMap.containsKey(i)) {
                        d1 = firstIndexMap.get(i);
                        h1 = true;
                    }
                }
                
                if(!h2) {
                    int backIndex = (s.length() - 1) - i;
                    char c = s.charAt(backIndex);
                    
                    if(Character.isDigit(c)) {
                        d2 = c;
                        h2 = true;
                    } else if(lastIndexMap.containsKey(backIndex)) {
                        d2 = lastIndexMap.get(backIndex);
                        h2 = true;
                    }
                }
                
                if(h1 && h2) break;
            }
            
            // concatonate & sum
            int val = ((d1 - '0') * 10) + (d2 - '0');
            
            System.out.println(val);
            
            sum += val;
        }
        
        System.out.println("Sum: " + sum);
    }
    
    /**
     * Part 1
     * Find the first and last digit in each string, concatonate to a number, sum for all strings
     * @param strings
     */
    private static void part1(List<String> strings) {
        int sum = 0;
        
        for(String s : strings) {
            int d1 = '0', d2 = '0'; // digit 1 (first), digit 2 (last)
            boolean h1 = false, h2 = false; // has 1, has 2
            
            // find first and last digits
            for(int i = 0; i < s.length(); i++) {
                if(!h1) {
                    char c = s.charAt(i);
                    if(Character.isDigit(c)) {
                        d1 = c;
                        h1 = true;
                    }
                }
                
                if(!h2) {
                    char c = s.charAt((s.length() - 1) - i);
                    if(Character.isDigit(c)) {
                        d2 = c;
                        h2 = true;
                    }
                }
                
                if(h1 && h2) break;
            }
            
            // concatonate & sum
            int val = ((d1 - '0') * 10) + (d2 - '0');
            
            System.out.println(val);
            
            sum += val;
        }
        
        System.out.println("Sum: " + sum);
    }
}
