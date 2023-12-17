package year2023;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
 * Day 17
 * 
 * this code sucks ass
 */
public class Day17 {
    public static void run(File f) throws IOException {
        part1(AdventUtil.inputList(f));
    }
    
    /**
     * Parse to a grid, pathfind to the end.
     * Returns a value off by 1 for part 1
     * I have no fucking clue why
     * 
     * @param lines
     */
    private static void part1(List<String> lines) {
        final int MIN = 4, // 1 for part 1, 4 for part 2
                  MAX = 10; // 3 for part 1, 10 for part 2
        
        int[][] grid = new int[lines.get(0).length()][lines.size()];
        
        for(int y = 0; y < lines.size(); y++) {
            String ln = lines.get(y);
            
            for(int x = 0; x < ln.length(); x++) {
                grid[x][y] = ln.charAt(x) - '0';
            }
        }
        
        int width = grid.length,
            height = grid[0].length,
            gx = width - 1,
            gy = height - 1;
        
        // A* with extra nonsense
        Set<SearchPoint> openSet = new HashSet<>();
        
        Map<SearchPoint, SearchPoint> prev = new HashMap<>();
        
        Map<SearchPoint, Integer> gScore = new HashMap<>(),
                                  fScore = new HashMap<>();
        
        SearchPoint start = new SearchPoint(0, 0, Direction.NORTH, 0),
                    end = null;
        
        openSet.add(start);
        gScore.put(start, 0);
        fScore.put(start, heuristic(start, gx, gy));
        
        while(!openSet.isEmpty()) {
            SearchPoint current = Collections.min(openSet, (a, b) -> {
                return fScore.getOrDefault(a, Integer.MAX_VALUE) - fScore.getOrDefault(b, Integer.MAX_VALUE);
            });
            
            //System.out.println(prev.get(current) + "\t-> " + current);
            
            if(current.x() == gx && current.y() == gy) {
                System.out.println("Found");
                end = current;
                break;
            }
            
            openSet.remove(current);
            
            for(SearchPoint n : current.neighbors(width, height, MIN, MAX)) {
                int cost = 0;
                
                switch(n.d()) {
                    case NORTH:
                        for(int y = current.y() - 1; y >= n.y(); y--) cost += grid[n.x()][y];
                        break;
                        
                    case SOUTH:
                        for(int y = current.y() + 1; y <= n.y(); y++) cost += grid[n.x()][y];
                        break;
                        
                    case EAST:
                        for(int x = current.x() + 1; x <= n.x(); x++) cost += grid[x][n.y()];
                        break;
                        
                    case WEST:
                        for(int x = current.x() - 1; x >= n.x(); x--) cost += grid[x][n.y()];
                        break;
                }
                
                int tGScore = gScore.get(current) + cost;
                
                if(tGScore < gScore.getOrDefault(n, Integer.MAX_VALUE)) {
                    if(prev.containsKey(n)) {
                        //System.out.println("REPLACED PREVIOUS FOR " + n + ":\t" + prev.get(n) + "\t-> " + current);
                    }
                    
                    prev.put(n, current);
                    gScore.put(n, tGScore);
                    fScore.put(n, tGScore + heuristic(n, gx, gy));
                    
                    if(!openSet.contains(n)) {
                        openSet.add(n);
                    }
                }
            }
        }
        
        // construct path
        SearchPoint p = end,
                    pp = end;
        char[][] pathGrid = new char[width][height];
        int cost = 0;
        
        while((p = prev.get(p)) != null) {
            int x = p.x(),
                y = p.y();
            
            //System.out.printf("(%s, %s) -> (%s, %s) %s%n", x, y, pp.x(), pp.y(), pp.d());
            
            switch(pp.d()) {
                case NORTH:
                    for(int yoffs = 1; yoffs <= y - pp.y(); yoffs++) { 
                        pathGrid[x][y - yoffs] = '^';
                        cost += grid[x][y - yoffs];
                    }
                    break;
                    
                case SOUTH:
                    for(int yoffs = 1; yoffs <= pp.y() - y; yoffs++) { 
                        pathGrid[x][y + yoffs] = 'v';
                        cost += grid[x][y + yoffs];
                    }
                    break;
                    
                case EAST:
                    for(int xoffs = 1; xoffs <= pp.x() - x; xoffs++) { 
                        pathGrid[x + xoffs][y] = '>';
                        cost += grid[x + xoffs][y];
                    }
                    break;
                    
                case WEST:
                    for(int xoffs = 1; xoffs <= x - pp.x(); xoffs++) { 
                        pathGrid[x - xoffs][y] = '<';
                        cost += grid[x - xoffs][y];
                    }
                    break;
            }
            
            pp = p;
        }
        
        //cost -= grid[0][0];
        
        // print path
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                System.out.print(pathGrid[x][y] == 0 ? '.' : pathGrid[x][y]);
            }
            System.out.println();
        }
        System.out.println();
        
        System.out.println(cost);
    }
    
    private static int heuristic(SearchPoint sp, int gx, int gy) {
        return 0;
        //return Math.abs(gx - sp.x()) + Math.abs(gy - sp.y());
    }
}

record SearchPoint(int x, int y, Direction d, int a) {
    public List<SearchPoint> neighbors(int width, int height, int minDist, int maxDist) {
        List<SearchPoint> ns = new ArrayList<>();
        
        for(int offs = minDist; offs <= maxDist; offs++) {
            switch(this.d) {
                case NORTH:
                case SOUTH:
                    if(this.x + offs < width) ns.add(new SearchPoint(this.x + offs, this.y, Direction.EAST, offs));
                    if(this.x - offs >= 0) ns.add(new SearchPoint(this.x - offs, this.y, Direction.WEST, offs));
                    break;
                    
                case EAST:
                case WEST:
                    if(this.y - offs >= 0) ns.add(new SearchPoint(this.x, this.y - offs, Direction.NORTH, offs));
                    if(this.y + offs < height) ns.add(new SearchPoint(this.x, this.y + offs, Direction.SOUTH, offs));
                    break;
            }
        }
        
        return ns;
    }
}
