package year2023;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import global.util.AdventUtil;
import global.util.Pair;

/**
 * Day 19
 * 
 * Finite State Automata
 */
public class Day19 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    private enum ComparisonType {
        GREATER,
        LESS,
        NONE
    }
    
    private enum Category {
        X,
        M,
        A,
        S,
        NONE
    }
    
    private record Part(long x, long m, long a, long s) { }
    
    private record Comparison(Category category, ComparisonType type, int value, String target) { }
    
    /**
     * For every possible part, count how many are accepted
     * @param lines
     */
    private static void part2(List<String> lines) {
        Pair<Map<String, List<Comparison>>, List<Part>> parsed = parse(lines);
        Map<String, List<Comparison>> stateMap = parsed.a();
        
        // Following state instructions, split the range of each part number for each comparison.
        // Do this recursively, returning the length of the range accepted
        Pair<Integer, Integer> x = new Pair<>(1, 4000),
                               m = new Pair<>(1, 4000),
                               a = new Pair<>(1, 4000),
                               s = new Pair<>(1, 4000);
        
        
        System.out.println(part2Recursive(stateMap, "in", x, m, a, s));
    }
    
    /**
     * Follow instructions by splitting the range of the compared value and recursing with its target
     * @param mins
     * @param maxes
     * @return The number of combinations accepted (product of each category's range)
     */
    private static long part2Recursive(Map<String, List<Comparison>> stateMap, String state, Pair<Integer, Integer> x, Pair<Integer, Integer> m, Pair<Integer, Integer> a, Pair<Integer, Integer> s) {
        long sum = 0;
        
        // check that the ranges are valid
        if(x.b() < x.a()) return 0;
        if(m.b() < m.a()) return 0;
        if(a.b() < a.a()) return 0;
        if(s.b() < s.a()) return 0;
        
        if(state.equals("A")) {
            // accepted. return number of combinations
            return (x.b() - x.a() + 1L) *
                   (m.b() - m.a() + 1L) *
                   (a.b() - a.a() + 1L) *
                   (s.b() - s.a() + 1L);
        } else if(state.equals("R")) {
            // rejected
            return 0;
        }
        
        // apply state transitions
        for(Comparison c : stateMap.get(state)) {
            switch(c.category) {
                case X:
                    if(c.type == ComparisonType.GREATER) {
                        // greater. recurse with upper part of range
                        if(x.b() > c.value) {
                            sum += part2Recursive(stateMap, c.target,
                                                  new Pair<>(c.value + 1, x.b()),
                                                  m, a, s);
                            
                            x = new Pair<>(x.a(), c.value);
                        }
                    } else {
                        // less. recurse with lower part of range
                        if(x.a() < c.value) {
                            sum += part2Recursive(stateMap, c.target,
                                                  new Pair<>(x.a(), c.value - 1),
                                                  m, a, s);
                            
                            x = new Pair<>(c.value, x.b());
                        }
                    }
                    break;
                    
                case M:
                    if(c.type == ComparisonType.GREATER) {
                        // greater. recurse with upper part of range
                        if(m.b() > c.value) {
                            sum += part2Recursive(stateMap, c.target, x,
                                                  new Pair<>(c.value + 1, m.b()),
                                                  a, s);
                            
                            m = new Pair<>(m.a(), c.value);
                        }
                    } else {
                        // less. recurse with lower part of range
                        if(m.a() < c.value) {
                            sum += part2Recursive(stateMap, c.target, x,
                                                  new Pair<>(m.a(), c.value - 1),
                                                  a, s);
                            
                            m = new Pair<>(c.value, m.b());
                        }
                    }
                    break;
                    
                case A:
                    if(c.type == ComparisonType.GREATER) {
                        // greater. recurse with upper part of range
                        if(a.b() > c.value) {
                            sum += part2Recursive(stateMap, c.target, x, m,
                                                  new Pair<>(c.value + 1, a.b()),
                                                  s);
                            
                            a = new Pair<>(a.a(), c.value);
                        }
                    } else {
                        // less. recurse with lower part of range
                        if(a.a() < c.value) {
                            sum += part2Recursive(stateMap, c.target, x, m,
                                                  new Pair<>(a.a(), c.value - 1),
                                                  s);
                            
                            a = new Pair<>(c.value, a.b());
                        }
                    }
                    break;
                    
                case S:
                    if(c.type == ComparisonType.GREATER) {
                        // greater. recurse with upper part of range
                        if(s.b() > c.value) {
                            sum += part2Recursive(stateMap, c.target, x, m, a,
                                                  new Pair<>(c.value + 1, s.b()));
                            
                            s = new Pair<>(s.a(), c.value);
                        }
                    } else {
                        // less. recurse with lower part of range
                        if(s.a() < c.value) {
                            sum += part2Recursive(stateMap, c.target, x, m, a,
                                                  new Pair<>(s.a(), c.value - 1));
                            
                            s = new Pair<>(c.value, s.b());
                        }
                    }
                    break;
                    
                case NONE:
                    sum += part2Recursive(stateMap, c.target, x, m, a, s);
                    break;
            }
        }
        
        return sum;
    }
    
    /**
     * Parse states, parse parts, find the sum of the sum of the ratings of each accepted part 
     * 
     * @param lines
     */
    private static void part1(List<String> lines) {
        Pair<Map<String, List<Comparison>>, List<Part>> parsed = parse(lines);
        Map<String, List<Comparison>> stateMap = parsed.a();
        List<Part> parts = parsed.b();
        
        // for each part, find if its accepted. If it is, add its category values to the sum
        long sum = 0;
        
        for(Part p : parts) {
            String state = "in";
            
            while(!(state.equals("A") || state.equals("R"))) {
                String stateBefore = state;
                List<Comparison> stateFlow = stateMap.get(state);
                
                out:
                for(Comparison c : stateFlow) {
                    long compVal = switch(c.category) {
                        case X      -> p.x;
                        case M      -> p.m;
                        case A      -> p.a;
                        case S      -> p.s;
                        case NONE   -> 0;
                    };
                    
                    switch(c.type) {
                        case GREATER:
                            if(compVal > c.value) {
                                state = c.target;
                                break out;
                            }
                            break;
                            
                        case LESS:
                            if(compVal < c.value) {
                                state = c.target;
                                break out;
                            }
                            break;
                            
                        case NONE:
                            state = c.target;
                            break out;
                    }
                }
                
                if(state.equals(stateBefore)) throw new IllegalStateException("Did not escape state: " + state);
            }
            
            if(state.equals("A")) {
                // accepted!
                sum += p.x;
                sum += p.m;
                sum += p.a;
                sum += p.s;
            }
        }
        
        System.out.println(sum);
    }
    
    /**
     * Parses the input file
     * 
     * @param lines
     * @return
     */
    private static Pair<Map<String, List<Comparison>>, List<Part>> parse(List<String> lines) {
        // parse input
        
        Map<String, List<Comparison>> stateMap = new HashMap<>();
        List<Part> parts = new ArrayList<>();
        boolean mapping = true;
        
        for(String ln : lines) {
            if(mapping) {
                // mapping phase. parse states and put them in the map
                if(ln.isBlank()) {
                    mapping = false;
                    continue;
                }
                
                // for each comparison
                int start = ln.indexOf("{"),
                    i = 0;
                String stateName = ln.substring(0, start),
                       compString = ln.substring(start + 1, ln.length() - 1) + ",";
                
                List<Comparison> comps = new ArrayList<>();
                
                while(i < ln.length()) {
                    int idx = compString.indexOf(',', i);
                    
                    String cString = compString.substring(i, idx);
                    boolean isLess = cString.contains("<"),
                            isGreater = cString.contains(">"),
                            isCompare = isLess || isGreater;
                    
                    if(isCompare) {
                        // comparison. Parse in full and move to next
                        String[] split = cString.split("[<>:]");
                        
                        comps.add(new Comparison(
                            switch(split[0]) {
                                case "m"    -> Category.M;
                                case "a"    -> Category.A;
                                case "s"    -> Category.S;
                                default     -> Category.X;
                            },
                            isLess ? ComparisonType.LESS : ComparisonType.GREATER,
                            Integer.parseInt(split[1]),
                            split[2]
                        ));
                        
                        i = idx + 1;
                    } else {
                        // non-comparison. done.
                        comps.add(new Comparison(Category.NONE, ComparisonType.NONE, 0, cString));
                        stateMap.put(stateName, comps);
                        break;
                    }
                }
                
                //System.out.println(stateName + ": " + stateMap.get(stateName));
            } else {
                // parts phase. parse parts
                String[] cats = ln.split(",");
                
                parts.add(new Part(
                        Integer.parseInt(cats[0].substring(3)), // has prefix {
                        Integer.parseInt(cats[1].substring(2)),
                        Integer.parseInt(cats[2].substring(2)),
                        Integer.parseInt(cats[3].substring(2, cats[3].length() - 1)) // has postfix }
                ));
                
                //System.out.println(parts.get(parts.size() - 1));
            }
        }
        
        return new Pair<>(stateMap, parts);
    }
}
