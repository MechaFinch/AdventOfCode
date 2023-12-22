package year2023;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import global.util.AdventUtil;
import global.util.Direction2D;
import global.util.Pair;

/**
 * Day 16
 * 
 * Laser Optics
 */
public class Day16 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    /**
     * Mirrors & splitters, described left to right
     */
    private enum Mirror {
        UP,         // /
        DOWN,       // \
        VERTICAL,   // |
        HORIZONTAL, // -
        NONE;       // .
    }

    /**
     * Describes the contents of a tile, both the object and the light
     */
    private static class Tile {
        public final Mirror contents;
        
        private Map<Direction2D, Boolean> incoming,
                                        outgoing;
        
        private boolean hasLight;
        
        public Tile(Mirror contents) {
            this.contents = contents;
            
            this.incoming = new HashMap<>();
            this.outgoing = new HashMap<>();
            this.hasLight = false;
            
            this.incoming.put(Direction2D.NORTH, false);
            this.incoming.put(Direction2D.SOUTH, false);
            this.incoming.put(Direction2D.EAST, false);
            this.incoming.put(Direction2D.WEST, false);
            
            this.outgoing.put(Direction2D.NORTH, false);
            this.outgoing.put(Direction2D.SOUTH, false);
            this.outgoing.put(Direction2D.EAST, false);
            this.outgoing.put(Direction2D.WEST, false);
        }
        
        public boolean hasLight() { return this.hasLight; }
        
        /**
         * Adds an incoming light ray, and returns 0-2 outgoing ray directions.
         * In the Pair, a Direction will be present if it is newly outgoing, and null if not used. It is allowed that Pair.b is non-null while Pair.a is null.
         * The Direction passed is the perspective of the light. Light moving West shall pass West to this function.
         * 
         * @param d
         * @return
         */
        public Pair<Direction2D, Direction2D> addIncoming(Direction2D d) {
            Direction2D newDir1 = null,
                      newDir2 = null;
            
            this.hasLight = true;
            
            // if we've already had incoming from the direction, no change
            if(this.incoming.get(d)) {
                return new Pair<>(newDir1, newDir2);
            }
            
            this.incoming.put(d, true);
            
            Direction2D o1, o2;
            
            switch(this.contents) {
                case UP:
                    o1 = switch(d) {
                        case NORTH  -> Direction2D.EAST;
                        case SOUTH  -> Direction2D.WEST;
                        case EAST   -> Direction2D.NORTH;
                        case WEST   -> Direction2D.SOUTH;
                    };
                    
                    if(!this.outgoing.get(o1)) newDir1 = o1;
                    this.outgoing.put(o1, true);
                    break;
                    
                case DOWN:
                    o1 = switch(d) {
                        case NORTH  -> Direction2D.WEST;
                        case SOUTH  -> Direction2D.EAST;
                        case EAST   -> Direction2D.SOUTH;
                        case WEST   -> Direction2D.NORTH;
                    };
                    
                    if(!this.outgoing.get(o1)) newDir1 = o1;
                    this.outgoing.put(o1, true);
                    break;
                    
                case VERTICAL:
                    o1 = switch(d) {
                        case NORTH  -> Direction2D.NORTH;
                        case SOUTH  -> Direction2D.SOUTH;
                        case EAST   -> Direction2D.NORTH;
                        case WEST   -> Direction2D.NORTH;
                    };
                    
                    o2 = switch(d) {
                        case NORTH  -> Direction2D.NORTH;
                        case SOUTH  -> Direction2D.SOUTH;
                        case EAST   -> Direction2D.SOUTH;
                        case WEST   -> Direction2D.SOUTH;
                    };
                    
                    if(!this.outgoing.get(o1)) newDir1 = o1;
                    this.outgoing.put(o1, true);
                    if(!this.outgoing.get(o2)) newDir2 = o2;
                    this.outgoing.put(o2, true);
                    break;
                    
                case HORIZONTAL:
                    o1 = switch(d) {
                        case NORTH  -> Direction2D.EAST;
                        case SOUTH  -> Direction2D.EAST;
                        case EAST   -> Direction2D.EAST;
                        case WEST   -> Direction2D.WEST;
                    };
                    
                    o2 = switch(d) {
                        case NORTH  -> Direction2D.WEST;
                        case SOUTH  -> Direction2D.WEST;
                        case EAST   -> Direction2D.EAST;
                        case WEST   -> Direction2D.WEST;
                    };
                    
                    if(!this.outgoing.get(o1)) newDir1 = o1;
                    this.outgoing.put(o1, true);
                    if(!this.outgoing.get(o2)) newDir2 = o2;
                    this.outgoing.put(o2, true);
                    break;
                    
                case NONE:
                    if(!this.outgoing.get(d)) newDir1 = d;
                    this.outgoing.put(d, true);
                    break;
            }
            
            return new Pair<>(newDir1, newDir2);
        }
    }
    
    /**
     * Parse input into a grid of mirrors & splitters, determine where light goes following their rules
     * 
     * @param lines
     */
    private static void part2(List<String> lines) {
        Tile[][] grid = new Tile[lines.get(0).length()][lines.size()];
        
        // parse
        for(int y = 0; y < lines.size(); y++) {
            String ln = lines.get(y);
            
            for(int x = 0; x < ln.length(); x++) {
                Mirror m = switch(ln.charAt(x)) {
                    case '/'    -> Mirror.UP;
                    case '\\'   -> Mirror.DOWN;
                    case '|'    -> Mirror.VERTICAL;
                    case '-'    -> Mirror.HORIZONTAL;
                    default     -> Mirror.NONE;
                };
                
                grid[x][y] = new Tile(m);
            }
        }
        
        // generate starting coords & directions
        List<Pair<Pair<Integer, Integer>, Direction2D>> startingConditions = new ArrayList<>();
        
        // top & bottom
        for(int x = 0; x < grid.length; x++) {
            startingConditions.add(new Pair<>(new Pair<>(x, 0), Direction2D.SOUTH));
            startingConditions.add(new Pair<>(new Pair<>(x, grid[0].length - 1), Direction2D.NORTH));
        }
        
        // left & right
        for(int y = 0; y < grid[0].length; y++) {
            startingConditions.add(new Pair<>(new Pair<>(0, y), Direction2D.EAST));
            startingConditions.add(new Pair<>(new Pair<>(grid.length - 1, y), Direction2D.WEST));
        }
        
        // for each starting condition
        long max = 0;
        for(Pair<Pair<Integer, Integer>, Direction2D> scon : startingConditions) {
            // run light stuff
            stepLight(grid, scon.a().a(), scon.a().b(), scon.b());
            
            // count energized
            long sum = 0;
            
            for(int y = 0; y < grid[0].length; y++) {
                for(int x = 0; x < grid.length; x++) {
                    sum += grid[x][y].hasLight() ? 1 : 0;
                }
            }
            
            if(sum > max) max = sum;
            
            // reset grid
            for(int x = 0; x < grid.length; x++) {
                for(int y = 0; y < grid[0].length; y++) {
                    grid[x][y] = new Tile(grid[x][y].contents);
                }
            }
        }
        
        System.out.println(max);
    }
    
    /**
     * Parse input into a grid of mirrors & splitters, determine where light goes following their rules
     * 
     * @param lines
     */
    private static void part1(List<String> lines) {
        Tile[][] grid = new Tile[lines.get(0).length()][lines.size()];
        
        // parse
        for(int y = 0; y < lines.size(); y++) {
            String ln = lines.get(y);
            
            for(int x = 0; x < ln.length(); x++) {
                Mirror m = switch(ln.charAt(x)) {
                    case '/'    -> Mirror.UP;
                    case '\\'   -> Mirror.DOWN;
                    case '|'    -> Mirror.VERTICAL;
                    case '-'    -> Mirror.HORIZONTAL;
                    default     -> Mirror.NONE;
                };
                
                grid[x][y] = new Tile(m);
            }
        }
        
        /*
        for(int y = 0; y < grid[0].length; y++) {
            for(int x = 0; x < grid.length; x++) {
                System.out.print(switch(grid[x][y].contents) {
                    case UP         -> "/";
                    case DOWN       -> "\\";
                    case VERTICAL   -> "|";
                    case HORIZONTAL -> "-";
                    default         -> ".";
                });
            }
            System.out.println();
        }
        System.out.println();
        */
        
        // run light stuff
        stepLight(grid, 0, 0, Direction2D.EAST);
        
        // count energized
        long sum = 0;
        
        for(int y = 0; y < grid[0].length; y++) {
            for(int x = 0; x < grid.length; x++) {
                /*
                if(grid[x][y].hasLight()) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
                */
                
                sum += grid[x][y].hasLight() ? 1 : 0;
            }
            //System.out.println();
        }
        //System.out.println();
        
        System.out.println(sum);
    }
    
    /**
     * Steps the light along. Recurses at splitters.
     * 
     * @param x
     * @param y
     * @param d
     */
    private static void stepLight(Tile[][] grid, int x, int y, Direction2D d) {
        while(true) {
            // check bounds
            if(x < 0 || x >= grid.length || y < 0 || y >= grid[0].length) return;
            
            // apply tile action
            //System.out.println("(" + x + ", " + y + ") " + d + " " + grid[x][y].contents);
            Pair<Direction2D, Direction2D> newOutgoing = grid[x][y].addIncoming(d);
            
            Direction2D d1 = newOutgoing.a(),
                      d2 = newOutgoing.b();
            
            if(d1 != null && d2 != null) {
                // split. recurse
                stepLight(grid, (int) d1.convertX(x, 1), (int) d1.convertY(y, 1), d1);
                stepLight(grid, (int) d2.convertX(x, 1), (int) d2.convertY(y, 1), d2);
                return;
            } else if(d1 != null) {
                // 1 way to go. Continue & loop
                d = d1;
            } else if(d2 != null) {
                // 1 way to go. Continue & loop
                d = d2;
            } else {
                // nowhere new.
                return;
            }
            
            x = (int) d.convertX(x, 1);
            y = (int) d.convertY(y, 1);
        }
    }
}
