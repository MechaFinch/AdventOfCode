package year2023;

import java.io.File;
import java.io.IOException;
import java.util.List;

import global.util.AdventUtil;
import global.util.Direction;
import global.util.Pair;

/**
 * Day 10
 * 
 * Pipe Fun
 */
public class Day10 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    /**
     * Convert input into an array of Pipes
     * Navigate along potential loops until one is found
     * Once it is found, follow it again and fill in any space to the side (according to pairity) enclosed.
     * Empty space found next to the path is flood-filled with INSIDE
     * Count and return the number of INSIDE tiles
     * 
     * @param lines
     */
    private static void part2(List<String> lines) {
        // parse grid
        Pipe[][] grid = new Pipe[lines.get(0).length()][lines.size()];
        
        int sx = -1, sy = -1;
        
        for(int y = 0; y < lines.size(); y++) {
            String ln = lines.get(y);
            
            for(int x = 0; x < ln.length(); x++) {
                grid[x][y] = switch(ln.charAt(x)) {
                    case '|'    -> Pipe.VERTICAL;
                    case '-'    -> Pipe.HORIZONTAL;
                    case 'L'    -> Pipe.NORTH_EAST;
                    case 'J'    -> Pipe.NORTH_WEST;
                    case '7'    -> Pipe.SOUTH_WEST;
                    case 'F'    -> Pipe.SOUTH_EAST;
                    case 'S'    -> Pipe.START;
                    default     -> Pipe.NONE;
                };
                
                if(grid[x][y] == Pipe.START) {
                    sx = x;
                    sy = y;
                }
            }
        }
        
        // navigate to find a loop
        int insideCount = 0;
        
        Direction[] directions = new Direction[] {
            Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST
        };
        
        for(Direction d : directions) {
            Pair<Integer, Pair<Integer, Enclosure[][]>> loopData = checkLoop(grid, sx, sy, d); 
            int pathLength = loopData.a();
            
            if(pathLength != -1) {
                // found valid path
                
                // walk path & flood
                int x = sx,
                    y = sy,
                    pairity = (loopData.b().a() > 0) ? 1 : -1; // 1 = RHS, -1 = LHS
                
                Enclosure[][] enc = loopData.b().b();
                
                Direction dir = d;
                
                // starting outgoing
                switch(dir) {
                    case NORTH: flood(enc, x + pairity, y); break;
                    case SOUTH: flood(enc, x - pairity, y); break;
                    case EAST:  flood(enc, x, y + pairity); break;
                    case WEST:  flood(enc, x, y - pairity); break;
                }
                
                out:
                while(true) {
                    // walk along pipe
                    x = dir.convertX(x);
                    y = dir.convertY(y);
                    
                    Pipe p = grid[x][y];
                    
                    // pipes labeled by outgoing
                    switch(p) {
                        case VERTICAL:
                            if(dir == Direction.NORTH) { 
                                flood(enc, x + pairity, y);
                            } else { // south
                                flood(enc, x - pairity, y);
                            }
                            break;
                        
                        case HORIZONTAL:
                            if(dir == Direction.EAST) {
                                flood(enc, x, y + pairity);
                            } else { // west
                                flood(enc, x, y - pairity);
                            }
                            break;
                            
                        case NORTH_EAST:
                            if(dir == Direction.SOUTH) {
                                // south -> east
                                flood(enc, x - pairity, y);
                                flood(enc, x, y + pairity);
                                dir = Direction.EAST;
                            } else {
                                // west -> north
                                flood(enc, x, y - pairity);
                                flood(enc, x + pairity, y);
                                dir = Direction.NORTH;
                            }
                            break;
                            
                        case NORTH_WEST:
                            if(dir == Direction.SOUTH) {
                                // south -> west
                                flood(enc, x - pairity, y);
                                flood(enc, x, y - pairity);
                                dir = Direction.WEST;
                            } else {
                                // east -> north
                                flood(enc, x, y + pairity);
                                flood(enc, x + pairity, y);
                                dir = Direction.NORTH;
                            }
                            break;
                            
                        case SOUTH_EAST:
                            if(dir == Direction.NORTH) {
                                // north -> east
                                flood(enc, x + pairity, y);
                                flood(enc, x, y + pairity);
                                dir = Direction.EAST;
                            } else {
                                // west -> south
                                flood(enc, x, y - pairity);
                                flood(enc, x - pairity, y);
                                dir = Direction.SOUTH;
                            }
                            break;
                            
                        case SOUTH_WEST:
                            if(dir == Direction.NORTH) {
                                // north -> west
                                flood(enc, x + pairity, y);
                                flood(enc, x, y - pairity);
                                dir = Direction.WEST;
                            } else {
                                // east -> south
                                flood(enc, x, y + pairity);
                                flood(enc, x - pairity, y);
                                dir = Direction.SOUTH;
                            }
                            break;
                        
                        case START:
                            // handle starting incoming
                            switch(dir) {
                                case NORTH: flood(enc, x + pairity, y); break;
                                case SOUTH: flood(enc, x - pairity, y); break;
                                case EAST:  flood(enc, x, y + pairity); break;
                                case WEST:  flood(enc, x, y - pairity); break;
                            }
                            break out;
                        
                        default: // shouldn't happen
                    }
                }
                
                // everything should be filled, count INSIDE tiles
                for(int x2 = 0; x2 < enc.length; x2++) {
                    for(int y2 = 0; y2 < enc[0].length; y2++) {
                        if(enc[x2][y2] == Enclosure.INSIDE) insideCount++;
                    }
                }
                
                break;
            }
        }
        
        System.out.println(insideCount);
    }
    
    /**
     * Flood-fills a region with INSIDE
     * 
     * @param grid
     * @param sx
     * @param sy
     */
    private static void flood(Enclosure[][] grid, int sx, int sy) {
        if(grid[sx][sy] != null && grid[sx][sy] != Enclosure.NONE) return;
        
        grid[sx][sy] = Enclosure.INSIDE;
        
        Direction[] directions = new Direction[] {
            Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST
        };
        
        for(Direction d : directions) {
            flood(grid, d.convertX(sx), d.convertY(sy));
        }
    }
    
    /**
     * Convert input into an array of Pipes
     * Navigate along potential loops until one is found
     * Return path length / 2
     * 
     * @param lines
     */
    private static void part1(List<String> lines) {
        // parse grid
        Pipe[][] grid = new Pipe[lines.get(0).length()][lines.size()];
        
        int sx = -1, sy = -1;
        
        for(int y = 0; y < lines.size(); y++) {
            String ln = lines.get(y);
            
            for(int x = 0; x < ln.length(); x++) {
                grid[x][y] = switch(ln.charAt(x)) {
                    case '|'    -> Pipe.VERTICAL;
                    case '-'    -> Pipe.HORIZONTAL;
                    case 'L'    -> Pipe.NORTH_EAST;
                    case 'J'    -> Pipe.NORTH_WEST;
                    case '7'    -> Pipe.SOUTH_WEST;
                    case 'F'    -> Pipe.SOUTH_EAST;
                    case 'S'    -> Pipe.START;
                    default     -> Pipe.NONE;
                };
                
                if(grid[x][y] == Pipe.START) {
                    sx = x;
                    sy = y;
                }
            }
        }
        
        // navigate to find a loop
        int pathLength = -1;
        
        Direction[] directions = new Direction[] {
            Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST
        };
        
        for(Direction d : directions) {
            pathLength = checkLoop(grid, sx, sy, d).a();
            
            if(pathLength != -1) break;
        }
        
        System.out.println(pathLength + ": " + (pathLength / 2));
    }
    
    /**
     * Follows pipes to find a loop. The length, parity, and boundary are determined.
     * If parity is positive, the enclosed space will be on the right hand side when following,
     * if the parity is negative, the enclosed space will be on the left hand side.
     * The boundary is a grid containing only the loop pipe.
     * 
     * @param grid
     * @param sx
     * @param sy
     * @param startDirection
     * @return Pair of {path length, {path parity, path-only grid}}
     */
    private static Pair<Integer, Pair<Integer, Enclosure[][]>> checkLoop(Pipe[][] grid, int sx, int sy, Direction startDirection) {
        Enclosure[][] eGrid = new Enclosure[grid.length][grid[0].length];
        
        Direction dir = startDirection;
        
        System.out.println("Checking " + dir);
        
        // check bounds
        switch(dir) {
            case NORTH:
                if(sy == 0) return new Pair<>(-1, new Pair<>(0, null));
                break;
            
            case SOUTH:
                if(sy == grid[0].length - 1) return new Pair<>(-1, new Pair<>(0, null));
                break;
            
            case EAST:
                if(sx == grid.length - 1) return new Pair<>(-1, new Pair<>(0, null));
                break;
            
            case WEST:
                if(sx == 0) return new Pair<>(-1, new Pair<>(0, null));
                break;
        }
        
        int x = sx,
            y = sy,
            d = 0,
            pairity = 0;
        
        while(true) {
            // walk along pipe
            x = dir.convertX(x);
            y = dir.convertY(y);
            d++;
            
            // check for valid pipe & get next direction
            Pipe p = grid[x][y];
            eGrid[x][y] = Enclosure.BOUNDARY;
            
            //System.out.printf("(%s, %s): %s (%s)%n", x, y, p, d);
            
            // pipes labeled by outgoing, checking against incoming
            switch(p) {
                case VERTICAL:
                    // no change
                    if(dir == Direction.NORTH || dir == Direction.SOUTH) continue;
                    break;
                
                case HORIZONTAL:
                    // no change
                    if(dir == Direction.EAST || dir == Direction.WEST) continue;
                    break;
                
                case NORTH_EAST:
                    // turn
                    if(dir == Direction.SOUTH) { 
                        pairity--; // left
                        dir = Direction.EAST;
                        continue;
                    } else if(dir == Direction.WEST) {
                        pairity++; // right
                        dir = Direction.NORTH;
                        continue;
                    }
                    break;
                
                case NORTH_WEST:
                    // turn
                    if(dir == Direction.SOUTH) { 
                        pairity++; // right
                        dir = Direction.WEST;
                        continue;
                    } else if(dir == Direction.EAST) {
                        pairity--; // left
                        dir = Direction.NORTH;
                        continue;
                    }
                    break;
                
                case SOUTH_WEST:
                    // turn
                    if(dir == Direction.NORTH) { 
                        pairity--; // left
                        dir = Direction.WEST;
                        continue;
                    } else if(dir == Direction.EAST) {
                        pairity++; // right
                        dir = Direction.SOUTH;
                        continue;
                    }
                    break;
                
                case SOUTH_EAST:
                    // turn
                    if(dir == Direction.NORTH) { 
                        pairity++; // right
                        dir = Direction.EAST;
                        continue;
                    } else if(dir == Direction.WEST) {
                        pairity--; // left
                        dir = Direction.SOUTH;
                        continue;
                    }
                    break;
                
                case START: // found a loop!
                    return new Pair<>(d, new Pair<>(pairity, eGrid));
                
                default:
            }
            
            // invalid pipe. end search
            return new Pair<>(-1, new Pair<>(0, null));
        }
    }
}

/**
 * A representation of the pipes that's easier to think about
 */
enum Pipe {
    VERTICAL,
    HORIZONTAL,
    NORTH_EAST,
    NORTH_WEST,
    SOUTH_WEST,
    SOUTH_EAST,
    NONE,
    START
}

/**
 * An enum for part 2, as unconnected pipes can be contained too
 */
enum Enclosure {
    BOUNDARY,
    INSIDE,
    NONE
}
