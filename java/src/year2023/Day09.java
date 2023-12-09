package year2023;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import global.util.AdventUtil;

/**
 * Day 9
 * 
 * OASIS Calculus
 */
public class Day09 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    /**
     * For each sequence, differentiate it until the derivative is zero, use that information to predict the next value.
     * Sum predicted values
     * 
     * @param lines
     */
    private static void part2(List<String> lines) {
        int sum = 0;
        
        for(String ln : lines) {
            List<Integer> seq = Arrays.stream(ln.split(" "))
                                      .mapToInt(Integer::parseInt)
                                      .boxed()
                                      .toList();
            
            sum += predictRecursive(seq, false);
        }
        
        System.out.println("\n" + sum);
    }
    
    /**
     * For each sequence, differentiate it until the derivative is zero, use that information to predict the next value.
     * Sum predicted values
     * 
     * @param lines
     */
    private static void part1(List<String> lines) {
        int sum = 0;
        
        for(String ln : lines) {
            List<Integer> seq = Arrays.stream(ln.split(" "))
                                      .mapToInt(Integer::parseInt)
                                      .boxed()
                                      .toList();
            
            sum += predictRecursive(seq, true);
        }
        
        System.out.println("\n" + sum);
    }
    
    /**
     * @param sequence
     * @return The predicted next value
     */
    private static int predictRecursive(List<Integer> sequence, boolean forwards) {
        // derive
        List<Integer> derivativeSequence = new ArrayList<>(sequence.size() - 1);
        boolean equal = true;
        
        for(int i = 1; i < sequence.size(); i++) {
            int d = sequence.get(i) - sequence.get(i - 1);
            derivativeSequence.add(d);
            
            if(i != 1 && derivativeSequence.get(i - 2) != d) equal = false;
        }
        
        // predict
        int p;
        
        if(equal) {
            if(forwards) {
                p = sequence.get(sequence.size() - 1) + derivativeSequence.get(0);
            } else {
                p = sequence.get(0) - derivativeSequence.get(0);
            }
        } else {
            if(forwards) {
                p = sequence.get(sequence.size() - 1) + predictRecursive(derivativeSequence, forwards);
            } else {
                p = sequence.get(0) - predictRecursive(derivativeSequence, forwards);
            }
        }
        
        //System.out.println(sequence + " predicted " + p);
        
        return p;
    }
}
