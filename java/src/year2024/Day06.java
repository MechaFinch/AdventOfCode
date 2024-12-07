package year2024;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import global.util.AdventUtil;
import global.util.Coord2D;
import global.util.Direction2D;
import global.util.Pair;

public class Day06 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    private static void part2(List<String> lines) {
        // parse
        Coord2D max = new Coord2D(lines.get(0).length(), lines.size());
        
        // maps x/y to obstacles found in that row/column in a searchable set
        List<TreeSet<Coord2D>> verticalObstacles = new ArrayList<>(),
                               horizontalObstacles = new ArrayList<>();
        
        Comparator<Coord2D> xComp = (a, b) -> a.x() - b.x();
        Comparator<Coord2D> yComp = (a, b) -> a.y() - b.y();
        
        for(int x = 0; x < max.x(); x++) {
            verticalObstacles.add(new TreeSet<>(yComp));
        }
        
        Coord2D guardStartPos = null;
        Direction2D guardStartDir = Direction2D.NORTH;
        
        for(int y = 0; y < max.y(); y++) {
            horizontalObstacles.add(new TreeSet<>(xComp));
            
            String ln = lines.get(y);
            for(int x = 0; x < max.x(); x++) {
                Coord2D point = new Coord2D(x, y);
                
                if(ln.charAt(x) == '^') {
                    guardStartPos = point;
                } else if(ln.charAt(x) == '#') {
                    horizontalObstacles.get(y).add(point);
                    verticalObstacles.get(x).add(point);
                }
            }
        }
        
        int loopCount = 0;
        
        // For each new obstacle
        // Completes in ~0.4 seconds
        for(int y = 0; y < max.y(); y++) {
            for(int x = 0; x < max.x(); x++) {
                Coord2D newObst = new Coord2D(x, y);
                
                if(guardStartPos.equals(newObst) || horizontalObstacles.get(y).contains(newObst)) {
                    continue;
                }
                
                horizontalObstacles.get(y).add(newObst);
                verticalObstacles.get(x).add(newObst);
                
                Set<Pair<Coord2D, Direction2D>> seen = new HashSet<>();
                Coord2D guardPos = guardStartPos;
                Direction2D guardDir = guardStartDir;
                
                // play
                while(guardPos.checkBounds(max.x(), max.y())) {
                    Pair<Coord2D, Direction2D> state = new Pair<>(guardPos, guardDir);
                    
                    if(seen.contains(state)) {
                        loopCount++;
                        break;
                    }
                    
                    seen.add(state);
                    
                    Coord2D obstacle = switch(guardDir) {
                        case NORTH  -> verticalObstacles.get(guardPos.x()).floor(guardPos);
                        case SOUTH  -> verticalObstacles.get(guardPos.x()).ceiling(guardPos);
                        case EAST   -> horizontalObstacles.get(guardPos.y()).ceiling(guardPos);
                        case WEST   -> horizontalObstacles.get(guardPos.y()).floor(guardPos);
                    };
                    
                    if(obstacle == null) {
                        break;
                    }
                    
                    guardPos = switch(guardDir) {
                        case NORTH  -> Direction2D.SOUTH.convert2D(obstacle, 1);
                        case SOUTH  -> Direction2D.NORTH.convert2D(obstacle, 1);
                        case EAST   -> Direction2D.WEST.convert2D(obstacle, 1);
                        case WEST   -> Direction2D.EAST.convert2D(obstacle, 1);
                    };
                    
                    guardDir = guardDir.right();
                }
                
                horizontalObstacles.get(y).remove(newObst);
                verticalObstacles.get(x).remove(newObst);
            }
        }
        
        System.out.println(loopCount);
    }
    
    private static void part2Slow(List<String> lines) {
        // parse
        Coord2D max = new Coord2D(lines.get(0).length(), lines.size());
        Set<Coord2D> obstacles = new HashSet<>();
        Coord2D guardStartPos = null;
        Direction2D guardStartDir = Direction2D.NORTH;
        
        for(int y = 0; y < max.y(); y++) {
            String ln = lines.get(y);
            for(int x = 0; x < max.x(); x++) {
                Coord2D point = new Coord2D(x, y);
                if(ln.charAt(x) == '^') {
                    guardStartPos = point;
                } else if(ln.charAt(x) == '#') {
                    obstacles.add(point);
                }
            }
        }
        
        int loopCount = 0;
        
        // For each new obstacle
        // Completes in ~5 seconds
        for(int y = 0; y < max.y(); y++) {
            for(int x = 0; x < max.x(); x++) {
                Coord2D newObst = new Coord2D(x, y);
                
                if(guardStartPos.equals(newObst) || obstacles.contains(newObst)) {
                    continue;
                }
                
                obstacles.add(newObst);
                
                Set<Pair<Coord2D, Direction2D>> seen = new HashSet<>();
                Coord2D guardPos = guardStartPos;
                Direction2D guardDir = guardStartDir;
                
                // play
                while(guardPos.checkBounds(max.x(), max.y())) {
                    Pair<Coord2D, Direction2D> state = new Pair<>(guardPos, guardDir);
                    
                    if(seen.contains(state)) {
                        loopCount++;
                        break;
                    }
                    
                    seen.add(state);
                    
                    Coord2D nextPos = guardDir.convert2D(guardPos, 1);
                    
                    if(obstacles.contains(nextPos)) {
                        guardDir = guardDir.right();
                    } else {
                        guardPos = nextPos;
                    }
                }
                
                obstacles.remove(newObst);
            }
        }
        
        System.out.println(loopCount);
    }
    
    private static void part1(List<String> lines) {
        // parse
        Coord2D max = new Coord2D(lines.get(0).length(), lines.size());
        Set<Coord2D> obstacles = new HashSet<>();
        Coord2D guardPos = null;
        Direction2D guardDir = Direction2D.NORTH;
        
        for(int y = 0; y < max.y(); y++) {
            String ln = lines.get(y);
            for(int x = 0; x < max.x(); x++) {
                Coord2D point = new Coord2D(x, y);
                if(ln.charAt(x) == '^') {
                    guardPos = point;
                } else if(ln.charAt(x) == '#') {
                    obstacles.add(point);
                }
            }
        }
        
        Set<Coord2D> seen = new HashSet<>();
        
        // play
        while(guardPos.checkBounds(max.x(), max.y())) {
            seen.add(guardPos);
            Coord2D nextPos = guardDir.convert2D(guardPos, 1);
            
            if(obstacles.contains(nextPos)) {
                guardDir = guardDir.right();
            } else {
                guardPos = nextPos;
            }
        }
        
        System.out.println(seen.size());
    }
}
