package year2023;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import global.util.AdventUtil;
import global.util.Coord2D;
import global.util.Direction;
import global.util.Pair;

/**
 * Day 21
 * 
 * Walkin'
 */
public class Day21 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    /**
     * Data for the map from the perspective of an entrance
     * 
     * @param exits EdgeWalk data for the 3 other sides
     * @param reachable Number of reachable tiles
     * @param steps Steps needed to get all reachable tiles
     */
    private record MapData(List<EdgeWalk> exits, int reachable, int steps) { }
    
    /**
     * Data describing the walk to a side
     * 
     * @param entranceDir Direction as seen by the tile being entered from this walk
     * @param entrancePos Coords of said entrance
     * @param length Number of steps needed in this map
     */
    private record EdgeWalk(Direction entraceDir, Coord2D entrancePos, int length) { }
    
    /**
     * State
     * 
     * @param mapCoord Coordinate in the grid of map copies
     * @param entranceCoord Coordinate in the map of start position
     * @param entranceDir Side being entered from
     * @param stepsLeft Remaining steps
     */
    private record MapState(Coord2D mapCoord, Coord2D entranceCoord, Direction entranceDir, int stepsLeft) { }
    
    /**
     * Parse to grid
     * For each time the walk enters a new grid:
     *  - If entering from a new position, explore the grid copy to determine the minimum number of steps to each non-entrance edge, and how many positions are counted
     *  - If entering from a known position, enqueue known exits with known step costs. If the number of remaining steps is less than that required to fully explore, explore the grid in detail
     * 
     * @param lines
     */
    private static void part2(List<String> lines) {
        int width = lines.get(0).length(),
            height = lines.size(),
            sx = -1, sy = -1;
            
        boolean[][] rocks = new boolean[width][height];
        
        // parse
        for(int y = 0; y < height; y++) {
            String ln = lines.get(y);
            
            for(int x = 0; x < width; x++) {
                char c = ln.charAt(x);
                if(c == '#') rocks[x][y] = true;
                else if(c == 'S') {
                    sx = x;
                    sy = y;
                }
            }
        }
        
        // explore
        Map<Integer, MapData> northMap = new HashMap<>(),   // map data when entering from the north, keyed by x
                              southMap = new HashMap<>(),   // map data when entering from the south, keyed by x
                              eastMap = new HashMap<>(),    // map data when entering from the east, keyed by y
                              westMap = new HashMap<>();    // map data when entering from the west, keyed by y
        
        Set<Coord2D> visitedMaps = new HashSet<>();
        PriorityQueue<MapState> mapQueue = new PriorityQueue<>((a, b) -> {
            return b.stepsLeft() - a.stepsLeft();
        });
        
        // explore starting map
        
        long reachable = 0;
        
        while(!mapQueue.isEmpty()) {
            MapState state = mapQueue.poll();
            
            // stepsLeft is strictly decreasing, so re-visits don't help
            // TODO: edge case if a map is reached with very few steps left each from two directions
            if(visitedMaps.contains(state.mapCoord())) continue;
            visitedMaps.add(state.mapCoord());
            
            MapData data = null;
            
            switch(state.entranceDir) {
                case NORTH:
                    data = northMap.getOrDefault(state.entranceCoord().x(), null);
                    break;
                    
                case SOUTH:
                    data = southMap.getOrDefault(state.entranceCoord().x(), null);
                    break;
                    
                case EAST:
                    data = eastMap.getOrDefault(state.entranceCoord().y(), null);
                    break;
                    
                case WEST:
                    data = westMap.getOrDefault(state.entranceCoord().y(), null);
                    break;
                    
            }
            
            if(data == null) {
                // no existing data. Explore the map
            } else {
                if(data.steps() > state.stepsLeft()) {
                    // we don't have enough steps. Explore the map
                    reachable += exploreLast(rocks, state.entranceCoord(), state.stepsLeft(), width, height);
                } else {
                    // we already know how to get to adjacent grids, and how many places are visitable
                    reachable += data.reachable;
                    
                    // list of 3 exits, in order
                    for(EdgeWalk ew : data.exits) {
                        mapQueue.offer(new MapState(ew.entraceDir().opposite().convert2D(state.mapCoord(), 1),
                                                    ew.entrancePos(),
                                                    ew.entraceDir(),
                                                    state.stepsLeft() - ew.length()));
                    }
                }
            }
        }
    }
    
    /**
     * Explores a map, starting from the given point
     * Computes MapData with EdgeWalks for all four sides
     * 
     * @param rocks
     * @param start
     * @param entrance
     * @param steps
     * @param width
     * @param height
     * @return
     */
    private static MapData exploreFirst(boolean[][] rocks, Coord2D start, int steps, int width, int height) {
        // TODO copy exploreMid
        return null;
    }
    
    /**
     * Explores a map, starting from the given point
     * Computes MapData with EdgeWalks for three sides
     * 
     * @param rocks
     * @param start
     * @param entrance
     * @param steps
     * @param width
     * @param height
     * @return
     */
    private static MapData exploreMid(boolean[][] rocks, Coord2D start, Direction entrance, int steps, int width, int height) {
        boolean[][] visited = new boolean[width][height];
        
        // walk
        Deque<Pair<Coord2D, Integer>> tileQueue = new ArrayDeque<>();
        tileQueue.add(new Pair<>(start, steps));
        
        int count = 0;
        
        while(!tileQueue.isEmpty()) {
            Pair<Coord2D, Integer> tile = tileQueue.poll();
            Coord2D coord = tile.a();
            int stepsLeft = tile.b();
            
            if(visited[coord.x()][coord.y()]) continue;
            
            // mark visited
            visited[coord.x()][coord.y()] = true;
            
            // even steps = can reach
            if(stepsLeft % 2 == 0) {
                count++;
                //reachable[coord.x()][coord.y()] = true;
            }
            stepsLeft--;
            if(stepsLeft < 0) continue;
            
            // enqueue neighbors
            Coord2D n = Direction.NORTH.convert2D(coord, 1),
                    s = Direction.SOUTH.convert2D(coord, 1),
                    e = Direction.EAST.convert2D(coord, 1),
                    w = Direction.WEST.convert2D(coord, 1);
            
            if(n.checkBounds(width, height) && !visited[n.x()][n.y()] && !rocks[n.x()][n.y()]) tileQueue.offer(new Pair<>(n, stepsLeft));
            if(s.checkBounds(width, height) && !visited[s.x()][s.y()] && !rocks[s.x()][s.y()]) tileQueue.offer(new Pair<>(s, stepsLeft));
            if(e.checkBounds(width, height) && !visited[e.x()][e.y()] && !rocks[e.x()][e.y()]) tileQueue.offer(new Pair<>(e, stepsLeft));
            if(w.checkBounds(width, height) && !visited[w.x()][w.y()] && !rocks[w.x()][w.y()]) tileQueue.offer(new Pair<>(w, stepsLeft));
        }
        
        return null;
    }
    
    /**
     * Explores a map, starting from the given point.
     * Does not compute MapData.
     * Original part 1 walk moved here
     * 
     * @param rocks
     * @param start
     * @param steps
     * @return
     */
    private static int exploreLast(boolean[][] rocks, Coord2D start, int steps, int width, int height) {
        boolean[][] visited = new boolean[width][height];
        
        // walk
        Deque<Pair<Coord2D, Integer>> tileQueue = new ArrayDeque<>();
        tileQueue.add(new Pair<>(start, steps));
        
        int count = 0;
        
        while(!tileQueue.isEmpty()) {
            Pair<Coord2D, Integer> tile = tileQueue.poll();
            Coord2D coord = tile.a();
            int stepsLeft = tile.b();
            
            if(visited[coord.x()][coord.y()]) continue;
            
            // mark visited
            visited[coord.x()][coord.y()] = true;
            
            // even steps = can reach
            if(stepsLeft % 2 == 0) {
                count++;
                //reachable[coord.x()][coord.y()] = true;
            }
            stepsLeft--;
            if(stepsLeft < 0) continue;
            
            // enqueue neighbors
            Coord2D n = Direction.NORTH.convert2D(coord, 1),
                    s = Direction.SOUTH.convert2D(coord, 1),
                    e = Direction.EAST.convert2D(coord, 1),
                    w = Direction.WEST.convert2D(coord, 1);
            
            if(n.checkBounds(width, height) && !visited[n.x()][n.y()] && !rocks[n.x()][n.y()]) tileQueue.offer(new Pair<>(n, stepsLeft));
            if(s.checkBounds(width, height) && !visited[s.x()][s.y()] && !rocks[s.x()][s.y()]) tileQueue.offer(new Pair<>(s, stepsLeft));
            if(e.checkBounds(width, height) && !visited[e.x()][e.y()] && !rocks[e.x()][e.y()]) tileQueue.offer(new Pair<>(e, stepsLeft));
            if(w.checkBounds(width, height) && !visited[w.x()][w.y()] && !rocks[w.x()][w.y()]) tileQueue.offer(new Pair<>(w, stepsLeft));
        }
        
        return count;
    }
    
    /**
     * Parse to grid
     * Explore only new tiles outwards. If a tile is reached on an odd step, count it.
     * 
     * @param lines
     */
    private static void part1(List<String> lines) {
        int width = lines.get(0).length(),
            height = lines.size(),
            sx = -1, sy = -1;
        
        boolean[][] rocks = new boolean[width][height];
        
        // parse
        for(int y = 0; y < height; y++) {
            String ln = lines.get(y);
            
            for(int x = 0; x < width; x++) {
                char c = ln.charAt(x);
                if(c == '#') rocks[x][y] = true;
                else if(c == 'S') {
                    sx = x;
                    sy = y;
                }
            }
        }
        
        System.out.println(exploreLast(rocks, new Coord2D(sx, sy), 64, width, height));
    }
}
