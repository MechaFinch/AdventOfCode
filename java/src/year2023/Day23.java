package year2023;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import global.util.AdventUtil;
import global.util.Coord2D;
import global.util.Direction2D;
import global.util.Pair;

/**
 * Day 23
 * 
 * Hiking
 */
public class Day23 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    private enum MapTile {
        PATH,
        FOREST,
        SLOPE_NORTH,
        SLOPE_SOUTH,
        SLOPE_EAST,
        SLOPE_WEST
    }
    
    private static class Node {
        Coord2D pos;
        
        Set<Coord2D> seen;
        
        // this -> other node with length 
        Set<Pair<Node, Integer>> edges;
        
        public Node(Coord2D pos) {
            this.pos = pos;
            
            this.seen = new HashSet<>();
            this.edges = new HashSet<>();
            
            this.seen.add(this.pos);
        }
    }
    
    /**
     * @param point Current position
     * @param origin Node being walked from
     * @param distance Number of tiles traveled
     * @param forward true if this path can be walked from origin to point
     * @param reverse true if this path can be walked from point to origin
     */
    private record WalkData(Coord2D point, Node origin, int distance, boolean forward, boolean reverse) { }
    
    private static void part2(List<String> lines) {
        Pair<Node, Node> parseResults = parse(lines, true); // set to false for part 1
        
        Node in = parseResults.a(),
             out = parseResults.b();
        
        // no more DAG :(
        
        // naive approach? works fine
        int d = searchRecursive(new HashSet<>(), in, 0, out);
        
        System.out.println(d);
    }
    
    /**
     * Depth first search of each possible path
     * 
     * @param used Set of seen nodes in path
     * @param n Current node
     * @param d Current distance
     * @param t Target node
     * @return
     */
    private static int searchRecursive(Set<Node> used, Node n, int d, Node t) {
        if(n.equals(t)) {
            return d;
        }
        
        int m = -1;
        used.add(n);
        
        for(Pair<Node, Integer> e : n.edges) {
            if(!used.contains(e.a())) {
                int d2 = searchRecursive(used, e.a(), d + e.b(), t);
                
                if(d2 > m) m = d2;
            }
        }
        
        used.remove(n);
        
        return m;
    }
    
    /**
     * Parse input into grid
     * Convert input into a junction graph, with nodes being any path with less than 2 walls, edges weighted by distance
     * Find the longest walk from the start to the end of the graph
     * 
     * While this implementation has far superior time complexity to the part 2 implementation, the graphs are so small
     * they perform the same for part 1
     * 
     * @param lines
     */
    private static void part1(List<String> lines) {
        Pair<Node, Node> parseResults = parse(lines, false);
        
        Node in = parseResults.a(),
             out = parseResults.b();
        
        // although not an explicit constraint, the slopes seem to be used to make this a DAG
        // so use the DAG shortest path algorithm (with negative distance)
        // first, topological sort
        Deque<Node> stack = new ArrayDeque<>();
        Set<Node> pSeen = new HashSet<>(),
                  tSeen = new HashSet<>();
        
        topSort(stack, pSeen, tSeen, in);
        
        // Second, find shortest path
        Map<Node, Integer> distMap = new HashMap<>();
        distMap.put(in, 0);
        
        while(!stack.isEmpty()) {
            Node n = stack.pop();
            int d1 = distMap.get(n);
            
            for(Pair<Node, Integer> e : n.edges) {
                Node n2 = e.a();
                int weight = -e.b(),
                    d2 = distMap.getOrDefault(n2, Integer.MAX_VALUE);
                
                if(d2 > d1 + weight) {
                    distMap.put(n2, d1 + weight);
                }
            }
        }
        
        System.out.println(-distMap.get(out));
    }
    
    /**
     * Topologically sorts a node
     * 
     * @param stack
     * @param pSeen permanant seen
     * @param tSeen temporary seen, for cycle detection
     * @param n
     */
    private static void topSort(Deque<Node> stack, Set<Node> pSeen, Set<Node> tSeen, Node n) {
        if(pSeen.contains(n)) return;
        if(tSeen.contains(n)) throw new IllegalStateException("Cycle Detected");
        
        tSeen.add(n);
        
        for(Pair<Node, Integer> e : n.edges) {
            topSort(stack, pSeen, tSeen, e.a());
        }
        
        tSeen.remove(n);
        pSeen.add(n);
        stack.push(n);
    }
    
    /**
     * Parses the input into a graph
     * 
     * @param lines
     * @return (entrance, exit)
     */
    private static Pair<Node, Node> parse(List<String> lines, boolean allowSlopeClimb) {
        // parse to grid
        int width = lines.get(0).length(),
            height = lines.size();
        
        MapTile[][] map = new MapTile[width][height];
        
        for(int y = 0; y < height; y++) {
            String ln = lines.get(y);
            
            for(int x = 0; x < width; x++) {
                map[x][y] = switch(ln.charAt(x)) {
                    case '.'    -> MapTile.PATH;
                    case '^'    -> MapTile.SLOPE_NORTH;
                    case 'v'    -> MapTile.SLOPE_SOUTH;
                    case '>'    -> MapTile.SLOPE_EAST;
                    case '<'    -> MapTile.SLOPE_WEST;
                    default     -> MapTile.FOREST;
                };
            }
        }
        
        // construct graph
        Coord2D exitPoint = new Coord2D(width - 2, height - 1);
        Node entrance = new Node(new Coord2D(1, 0)),
             exit = new Node(exitPoint);
        
        Deque<WalkData> tileQueue = new ArrayDeque<>();
        Map<Coord2D, Node> nodeMap = new HashMap<>();
        
        tileQueue.offer(new WalkData(new Coord2D(1, 1), entrance, 1, true, true));
        nodeMap.put(new Coord2D(1, 0), entrance);
        nodeMap.put(exitPoint, exit);
        
        while(!tileQueue.isEmpty()) {
            WalkData data = tileQueue.poll();
            
            // if we found a node, connect it
            if(nodeMap.containsKey(data.point)) {
                Node dest = nodeMap.get(data.point);
                
                if(data.origin == dest) continue;
                
                if(data.forward || allowSlopeClimb) data.origin.edges.add(new Pair<>(dest, data.distance));
                if(data.reverse || allowSlopeClimb) dest.edges.add(new Pair<>(data.origin, data.distance));
                continue;
            }
            
            data.origin.seen.add(data.point);
            
            // get adjacency data
            Coord2D pNorth = Direction2D.NORTH.convert2D(data.point, 1),
                    pSouth = Direction2D.SOUTH.convert2D(data.point, 1),
                    pEast = Direction2D.EAST.convert2D(data.point, 1),
                    pWest = Direction2D.WEST.convert2D(data.point, 1);
            
            MapTile tNorth = map[pNorth.x()][pNorth.y()],
                    tSouth = map[pSouth.x()][pSouth.y()],
                    tEast = map[pEast.x()][pEast.y()],
                    tWest = map[pWest.x()][pWest.y()],
                    cTile = map[data.point.x()][data.point.y()];
            
            int numForest = 0;
            if(tNorth == MapTile.FOREST) numForest++;
            if(tSouth == MapTile.FOREST) numForest++;
            if(tEast == MapTile.FOREST) numForest++;
            if(tWest == MapTile.FOREST) numForest++;
            
            Node newOrigin = data.origin;
            boolean newForward = data.forward,
                    newReverse = data.reverse;
            int newDistance = data.distance + 1;
            
            if(numForest != 2) {
                // junction. Create a node
                Node dest = new Node(data.point);
                nodeMap.put(data.point, dest);
                if(data.forward || allowSlopeClimb) data.origin.edges.add(new Pair<>(dest, data.distance));
                if(data.reverse || allowSlopeClimb) dest.edges.add(new Pair<>(data.origin, data.distance));
                
                newOrigin = dest;
                newForward = true;
                newReverse = true;
                newDistance = 1;
            }
            
            // add valid unseen tiles
            if(tNorth != MapTile.FOREST && !data.origin.seen.contains(pNorth)) {
                tileQueue.offer(new WalkData(
                    pNorth,
                    newOrigin,
                    newDistance,
                    newForward && cTile != MapTile.SLOPE_SOUTH,
                    newReverse && cTile != MapTile.SLOPE_NORTH
                ));
            }
            
            if(tSouth != MapTile.FOREST && !data.origin.seen.contains(pSouth)) {
                tileQueue.offer(new WalkData(
                    pSouth,
                    newOrigin,
                    newDistance,
                    newForward && cTile != MapTile.SLOPE_NORTH,
                    newReverse && cTile != MapTile.SLOPE_SOUTH
                ));
            }
            
            if(tEast != MapTile.FOREST && !data.origin.seen.contains(pEast)) {
                tileQueue.offer(new WalkData(
                    pEast,
                    newOrigin,
                    newDistance,
                    newForward && cTile != MapTile.SLOPE_WEST,
                    newReverse && cTile != MapTile.SLOPE_EAST
                ));
            }
            
            if(tWest != MapTile.FOREST && !data.origin.seen.contains(pWest)) {
                tileQueue.offer(new WalkData(
                    pWest,
                    newOrigin,
                    newDistance,
                    newForward && cTile != MapTile.SLOPE_EAST,
                    newReverse && cTile != MapTile.SLOPE_WEST
                ));
            }
        }
        
        /*
        System.out.println();
        for(Node n : nodeMap.values()) {
            String eString = "";
            boolean b = false;
            
            for(Pair<Node, Integer> e : n.edges) {
                eString += (b ? ", " : "") + e.a().pos + "(" + e.b() + ")";
                b = true;
            }
            
            System.out.printf("%s -> {%s}%n", n.pos, eString);
        }
        System.out.println();
        */
        
        return new Pair<>(entrance, exit);
    }
}
