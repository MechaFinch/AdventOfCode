package year2023;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import global.util.AdventUtil;

/**
 * Day 8
 * Wasteland Graph Theory
 */
public class Day08 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    /**
     * Search for cycles ending on a Z, and use the cycle lengths to calculate when they intersect
     * 
     * @param lines
     */
    private static void part2(List<String> lines) {
        String instructions = lines.get(0);
        Map<String, Node> nodeMap = buildGraph(lines);
        
        // find A-nodes
        List<Node> nodes = new ArrayList<>();
        
        for(Node n : nodeMap.values()) {
            if(n.label.endsWith("A")) {
                nodes.add(n);
            }
        }
        
        // for full input, start is always equal to len
        List<Integer> cycleLengths = new ArrayList<>();
        
        for(Node n : nodes) {
            // find when this node finds a Z-node, then when it cycles
            Node nc = n;
            int firstZ = -1;
            Set<NodeState> zStates = new HashSet<>();
            
            int i = 0,
                count = 0;
            while(true) {
                char inst = instructions.charAt(i++);
                if(i >= instructions.length()) i = 0;
                count++;
                
                if(inst == 'L') {
                    nc = nc.left;
                } else {
                    nc = nc.right;
                }
                
                if(nc.label.endsWith("Z")) {
                    if(firstZ == -1) {
                        firstZ = count;
                    }
                    
                    NodeState state = new NodeState(nc.label, i);
                    boolean cycleFound = zStates.contains(state);
                    zStates.add(state);
                    
                    if(cycleFound) {
                        System.out.println("Found cycle for " + n.label + ": start " + firstZ + " len " + (count - firstZ) + " states " + zStates.size());
                        cycleLengths.add(firstZ);
                        break;
                    }
                }
            }
        }
        
        // find least common multiple of all cycles
        long lcm = cycleLengths.get(0);
        
        for(int i = 1; i < cycleLengths.size(); i++) {
            lcm = (cycleLengths.get(i) * lcm) / gcd(cycleLengths.get(i), lcm);
        }
        
        System.out.println(lcm);
    }
    
    /**
     * @param a
     * @param b
     * @return greatest common denominator of a and b
     */
    private static long gcd(long a, long b) {
        if(b == 0) return a;
        return gcd(b, a % b);
    }
    
    /**
     * Apply instructions to A-nodes until Z-nodes are reached simultaneously
     * This does not work as the actual number is ~16.5 trillion steps
     * @param lines
     */
    private static void part2Naive(List<String> lines) {
        String instructions = lines.get(0);
        Map<String, Node> nodeMap = buildGraph(lines);
        
        // find A-nodes
        List<Node> nodes = new ArrayList<>();
        
        for(Node n : nodeMap.values()) {
            if(n.label.endsWith("A")) {
                nodes.add(n);
            }
        }
        
        // run
        int i = 0,
            count = 0;
        while(true) {
            char inst = instructions.charAt(i++);
            if(i >= instructions.length()) i = 0;
            count++;
            
            boolean allGood = true;
            for(int j = 0; j < nodes.size(); j++) {
                Node n = nodes.get(j);
                
                if(inst == 'L') {
                    n = n.left;
                } else {
                    n = n.right;
                }
                
                if(!n.label.endsWith("Z")) allGood = false;
                
                nodes.set(j, n);
            }
            
            if(allGood) break;
        }
        
        System.out.println(count);
    }
    
    /**
     * Apply instructions until ZZZ is reached
     * 
     * @param lines
     */
    private static void part1(List<String> lines) {
        String instructions = lines.get(0);
        Node node = buildGraph(lines).get("AAA");
        
        int i = 0,
            count = 0;
        while(true) {
            char inst = instructions.charAt(i++);
            if(i >= instructions.length()) i = 0;
            count++;
            
            if(inst == 'L') {
                node = node.left;
            } else {
                node = node.right;
            }
            
            if(node.label.equals("ZZZ")) break;
        }
        
        System.out.println(count);
    }
    
    /**
     * Build a graph of Nodes rooted at AAA
     * 
     * @param lines
     * @return
     */
    private static Map<String, Node> buildGraph(List<String> lines) {
        Map<String, PartialNode> partialNodeMap = new HashMap<>();
        
        // parse
        for(int i = 2; i < lines.size(); i++) {
            String ln = lines.get(i),
                   label = ln.substring(0, 3),
                   left = ln.substring(7, 10),
                   right = ln.substring(12, 15);
            
            partialNodeMap.put(label, new PartialNode(label, left, right));
        }
        
        // make graph
        Map<String, Node> fullNodeMap = new HashMap<>();
        
        // create all nodes
        for(PartialNode pn : partialNodeMap.values()) {
            fullNodeMap.put(pn.label(), new Node(pn.label()));
        }
        
        // link nodes
        for(PartialNode pn : partialNodeMap.values()) {
            Node n = fullNodeMap.get(pn.label());
            
            n.left = fullNodeMap.get(pn.left());
            n.right = fullNodeMap.get(pn.right());
        }
        
        return fullNodeMap;
    }
}

/**
 * @param label Node label
 * @param index instruction index
 */
record NodeState(String label, int index) {
    
}

/**
 * A Node
 */
class Node {
    public final String label;
    
    public Node left, right;
    
    public Node(String label) {
        this.label = label;
        this.left = null;
        this.right = null;
    }
}

/**
 * A Node that can be created without its target Nodes existing, requiring a secondary data structure
 */
record PartialNode(String label, String left, String right) {
    
}
