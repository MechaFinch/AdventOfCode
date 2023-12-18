package year2023;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import global.util.AdventUtil;
import global.util.Direction;

/**
 * Day 14
 */
public class Day14 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    private enum Rock {
        ROUND,
        SQUARE,
        NONE
    }

    /**
     * A wrapper class for the rock grid, with hashCode and equals to enable HashMap-ing
     */
    private static class RockState {
        public Rock[][] rocks;
        
        private Integer cachedCode = null;
        
        public RockState(Rock[][] rs) {
            this.rocks = new Rock[rs.length][rs[0].length];
            
            for(int x = 0; x < rs.length; x++) {
                for(int y = 0; y < rs[0].length; y++) {
                    this.rocks[x][y] = rs[x][y];
                }
            }
        }
        
        @Override
        public boolean equals(Object o) {
            if(o instanceof RockState rs) {
                for(int x = 0; x < this.rocks.length; x++) {
                    for(int y = 0; y < this.rocks[0].length; y++) {
                        if(this.rocks[x][y] != rs.rocks[x][y]) {
                            return false;
                        }
                    }
                }
                
                return true;
            } else {
                return false;
            }
        }
        
        @Override
        public int hashCode() {
            if(this.cachedCode != null) return this.cachedCode;
            
            int code = 0;
            
            for(int x = 0; x < this.rocks.length; x++) {
                int rCount = 0;
                
                for(int y = 0; y < this.rocks[0].length; y++) {
                    rCount += (this.rocks[x][y] == Rock.ROUND) ? 1 : 0;
                }
                
                code = (code << 1) ^ rCount;
            }
            
            this.cachedCode = code;
            return code;
        }
    }
    
    /**
     * Parse into a grid of rocks
     * run the spin cycle a billion times
     * compute the load
     * 
     * @param lines
     */
    private static void part2(List<String> lines) {
        Rock[][] rocks = new Rock[lines.get(0).length()][lines.size()];
        
        // parse to grid
        for(int y = 0; y < lines.size(); y++) {
            String ln = lines.get(y);
            for(int x = 0; x < ln.length(); x++) {
                rocks[x][y] = switch(ln.charAt(x)) {
                    case 'O'    -> Rock.ROUND;
                    case '#'    -> Rock.SQUARE;
                    default     -> Rock.NONE;
                };
            }
        }
        
        final int width = rocks.length,
                  height = rocks[0].length;
      
        //printRocks(rocks, width, height);
        
        Map<RockState, Integer> dynamicProgramming = new HashMap<>();
        
        for(int i = 0; i < 1_000_000_000; i++) {
            
            tilt(rocks, Direction.NORTH);
            tilt(rocks, Direction.WEST);
            tilt(rocks, Direction.SOUTH);
            tilt(rocks, Direction.EAST);
            
            //printRocks(rocks, width, height);
            
            RockState rs = new RockState(rocks);
            
            if(dynamicProgramming.containsKey(rs)) {
                // cycle found!
                int cycleLength = i - dynamicProgramming.get(rs);
                
                int remSpins = ((1_000_000_000 - i) % cycleLength) - 1;
                    
                System.out.println("Cycle length: " + cycleLength + ", spins left: " + remSpins);
                
                for(int j = 0; j < remSpins; j++) {
                    tilt(rocks, Direction.NORTH);
                    tilt(rocks, Direction.WEST);
                    tilt(rocks, Direction.SOUTH);
                    tilt(rocks, Direction.EAST);
                }
                
                //printRocks(rocks, width, height);
                
                break;
            }
            
            dynamicProgramming.put(rs, i);
        }
        
        System.out.println(computeLoad(rocks, Direction.NORTH));
    }
    
    /**
     * Parse into a grid of rocks
     * Slide round rocks north
     * Compute the load
     * 
     * @param lines
     */
    private static void part1(List<String> lines) {
        Rock[][] rocks = new Rock[lines.get(0).length()][lines.size()];
        
        // parse to grid
        for(int y = 0; y < lines.size(); y++) {
            String ln = lines.get(y);
            for(int x = 0; x < ln.length(); x++) {
                rocks[x][y] = switch(ln.charAt(x)) {
                    case 'O'    -> Rock.ROUND;
                    case '#'    -> Rock.SQUARE;
                    default     -> Rock.NONE;
                };
            }
        }
        
        tilt(rocks, Direction.NORTH);
        
        System.out.println(computeLoad(rocks, Direction.NORTH));
    }
    
    private static void printRocks(Rock[][] rocks, int width, int height) {
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                switch(rocks[x][y]) {
                    case ROUND:
                        System.out.print("O");
                        break;
                    
                    case SQUARE:
                        System.out.print("#");
                        break;
                        
                    default:
                        System.out.print(".");
                        break;
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    
    /**
     * Computes the load on a given direction
     * 
     * @param rocks
     * @param d
     */
    private static long computeLoad(Rock[][] rocks, Direction d) {
        final int width = rocks.length,
                  height = rocks[0].length;
        
        long load = 0;
        
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                if(rocks[x][y] == Rock.ROUND) {
                    load += switch(d) {
                        case NORTH  -> height - y;
                        case SOUTH  -> y + 1;
                        case EAST   -> x + 1;
                        case WEST   -> width - x;
                    };
                }
            }
        }
        
        return load;
    }
    
    /**
     * Tilts the rocks in a given direction
     * Each direction is basically the same code, but the loops and index are differnt
     * 
     * @param rocks
     * @param d
     */
    private static void tilt(Rock[][] rocks, Direction d) {
        final int width = rocks.length,
                  height = rocks[0].length;
        
        switch(d) {
            case NORTH:
                for(int x = 0; x < width; x++) {
                    // find each rock and slide it up
                    int slideToIndex = 0;
                    
                    for(int y = 0; y < height; y++) {
                        switch(rocks[x][y]) {
                            case ROUND:
                                // mobile rock. Slide it, update the index
                                rocks[x][y] = Rock.NONE;
                                rocks[x][slideToIndex] = Rock.ROUND;
                                slideToIndex++;
                                break;
                                
                            case SQUARE:
                                // immobile rock. Update the slide to index
                                slideToIndex = y + 1;
                                break;
                                
                            default:
                        }
                    }
                }
                break;
                
            case SOUTH:
                for(int x = 0; x < width; x++) {
                    // find each rock and slide it down
                    int slideToIndex = height - 1;
                    
                    for(int y = height - 1; y >= 0; y--) {
                        switch(rocks[x][y]) {
                            case ROUND:
                                // mobile rock. Slide it, update the index
                                rocks[x][y] = Rock.NONE;
                                rocks[x][slideToIndex] = Rock.ROUND;
                                slideToIndex--;
                                break;
                                
                            case SQUARE:
                                // immobile rock. Update the slide to index
                                slideToIndex = y - 1;
                                break;
                                
                            default:
                        }
                    }
                }
                break;
                
            case EAST:
                for(int y = 0; y < height; y++) {
                    // find each rock and slide it down
                    int slideToIndex = width - 1;
                    
                    for(int x = width - 1; x >= 0; x--) {
                        switch(rocks[x][y]) {
                            case ROUND:
                                // mobile rock. Slide it, update the index
                                rocks[x][y] = Rock.NONE;
                                rocks[slideToIndex][y] = Rock.ROUND;
                                slideToIndex--;
                                break;
                                
                            case SQUARE:
                                // immobile rock. Update the slide to index
                                slideToIndex = x - 1;
                                break;
                                
                            default:
                        }
                    }
                }
                break;
                
            case WEST:
                for(int y = 0; y < height; y++) {
                    // find each rock and slide it up
                    int slideToIndex = 0;
                    
                    for(int x = 0; x < width; x++) {
                        switch(rocks[x][y]) {
                            case ROUND:
                                // mobile rock. Slide it, update the index
                                rocks[x][y] = Rock.NONE;
                                rocks[slideToIndex][y] = Rock.ROUND;
                                slideToIndex++;
                                break;
                                
                            case SQUARE:
                                // immobile rock. Update the slide to index
                                slideToIndex = x + 1;
                                break;
                                
                            default:
                        }
                    }
                }
                break;
        }
    }
}
