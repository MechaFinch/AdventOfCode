package year2024;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import global.util.AdventUtil;
import global.util.Pair;

public class Day07 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    private static void part2(List<String> lines) {
        // Parse
        List<Pair<Long, List<Long>>> tests = lines.stream().map(ln -> new Pair<Long, List<Long>>(
            Long.parseLong(ln.split(": ")[0]),
            Arrays.stream(ln.split(": ")[1].split(" ")).map(Long::parseLong).toList()
        )).toList();
        
        long sum = 0;
        
        // Test
        for(Pair<Long, List<Long>> test : tests) {
            long target = test.a();
            List<Long> vals = test.b();
            
            if(eval(vals, target, vals.get(0), 1)) {
                sum += target;
            }
        }
        
        System.out.println(sum);
    }
    
    /**
     * @param vals
     * @param target
     * @param val
     * @param idx
     * @return true if the expression can be equal to target for some combination of operators
     */
    private static boolean eval(List<Long> vals, long target, long val, int idx) {
        if(idx == vals.size()) {
            return val == target;
        } else {
            long v = vals.get(idx);
            
            if(eval(vals, target, val + v, idx + 1)) {
                return true;
            } else if(eval(vals, target, val * v, idx + 1)) {
                return true;
            } else if(eval(vals, target, concat(val, v), idx + 1)) {
                return true;
            }
            
            return false;
        }
    }
    
    /**
     * @param a
     * @param b
     * @return a || b
     */
    private static long concat(long a, long b) {
        return (a * (long)Math.pow(10, 1 + Math.floor(Math.log10(b)))) + b;
    }
    
    private static void part1(List<String> lines) {
        // Parse
        List<Pair<Long, List<Long>>> tests = lines.stream().map(ln -> new Pair<Long, List<Long>>(
            Long.parseLong(ln.split(": ")[0]),
            Arrays.stream(ln.split(": ")[1].split(" ")).map(Long::parseLong).toList()
        )).toList();
        
        long sum = 0;
        
        // Test
        for(Pair<Long, List<Long>> test : tests) {
            long target = test.a();
            List<Long> vals = test.b();
            
            // For each combination of + and *
            for(long i = 0; i < 1l << ((long) vals.size() - 1); i++) {
                long x = vals.get(0);
                
                // If bit = 1, +. If bit = 0, *
                for(int j = 1; j < vals.size(); j++) {
                    if(((i >> (j - 1)) & 1) != 0) {
                        x += vals.get(j);
                    } else {
                        x *= vals.get(j);
                    }
                }
                
                if(x == target) {
                    sum += target;
                    break;
                }
            }
        }
        
        System.out.println(sum);
    }
}
