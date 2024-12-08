package year2024;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import global.util.AdventUtil;
import global.util.Coord2D;

public class Day08 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    private static void part2(List<String> lines) {
        // Parse
        Map<Character, List<Coord2D>> antenae = new HashMap<>();
        Coord2D max = new Coord2D(lines.get(0).length(), lines.size());
        
        for(int y = 0; y < lines.size(); y++) {
            String ln = lines.get(y);
            
            for(int x = 0; x < ln.length(); x++) {
                char c = ln.charAt(x);
                
                if(c != '.') {
                    if(!antenae.containsKey(c)) {
                        antenae.put(c, new ArrayList<>());
                    }
                    
                    antenae.get(c).add(new Coord2D(x, y));
                }
            }
        }
        
        // Find antinodes
        Set<Coord2D> antinodes = new HashSet<>();
        
        for(List<Coord2D> channel : antenae.values()) {
            // For each combination in a channel
            for(int i = 0; i < channel.size(); i++) {
                Coord2D nodeA = channel.get(i);
                
                for(int j = 0; j < channel.size(); j++) {
                    if(i == j) {
                        continue;
                    }
                    
                    Coord2D nodeB = channel.get(j);
                    Coord2D difference = nodeA.subtract(nodeB, 1);
                    int gcd = (int) AdventUtil.gcd(Math.abs(difference.x()), Math.abs(difference.y()));
                    
                    Coord2D step = new Coord2D(difference.x() / gcd, difference.y() / gcd);
                    
                    // A and B are both considered antinodes but the program double counts so no check or add is needed
                    // Part 2 completes in 50ms so that double count is fine
                    
                    // go positive from A
                    Coord2D spot = nodeA.add(step, 1);
                    while(spot.checkBounds(max.x(), max.y())) {
                        antinodes.add(spot);
                        spot = spot.add(step, 1);
                    }
                    
                    // negative from A
                    spot = nodeA.subtract(step, 1);
                    while(spot.checkBounds(max.x(), max.y())) {
                        antinodes.add(spot);
                        spot = spot.subtract(step, 1);
                    }
                }
            }
        }
        
        System.out.println(antinodes.size());
    }
    
    private static void part1(List<String> lines) {
        // Parse
        Map<Character, List<Coord2D>> antenae = new HashMap<>();
        Coord2D max = new Coord2D(lines.get(0).length(), lines.size());
        
        for(int y = 0; y < lines.size(); y++) {
            String ln = lines.get(y);
            
            for(int x = 0; x < ln.length(); x++) {
                char c = ln.charAt(x);
                
                if(c != '.') {
                    if(!antenae.containsKey(c)) {
                        antenae.put(c, new ArrayList<>());
                    }
                    
                    antenae.get(c).add(new Coord2D(x, y));
                }
            }
        }
        
        // Find antinodes
        Set<Coord2D> antinodes = new HashSet<>();
        
        for(List<Coord2D> channel : antenae.values()) {
            // For each combination in a channel
            for(int i = 0; i < channel.size(); i++) {
                Coord2D nodeA = channel.get(i);
                
                for(int j = 0; j < channel.size(); j++) {
                    if(i == j) {
                        continue;
                    }
                    
                    Coord2D nodeB = channel.get(j);
                    Coord2D difference = nodeA.subtract(nodeB, 1);
                    Coord2D antiA = nodeA.add(difference, 1);
                    Coord2D antiB = nodeB.subtract(difference, 1);
                    
                    if(antiA.checkBounds(max.x(), max.y())) {
                        antinodes.add(antiA);
                    }
                    
                    if(antiB.checkBounds(max.x(), max.y())) {
                        antinodes.add(antiB);
                    }
                }
            }
        }
        
        System.out.println(antinodes.size());
    }
}
