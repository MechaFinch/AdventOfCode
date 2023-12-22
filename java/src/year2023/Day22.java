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
import global.util.Coord3D;
import global.util.Direction3D;
import global.util.Pair;

/**
 * Day 22
 * 
 * Tetris
 */
public class Day22 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    private static class Block {
        Coord3D origin;
        Direction3D dir;
        int length;
        boolean settled, counted;
        String id;
        
        Set<Block> supporters = new HashSet<>(),   // nodes supprting this
                   supporting = new HashSet<>();   // nodes this supports
        
        public Block(Coord3D origin, Direction3D dir, int length, String id) {
            this.origin = origin;
            this.dir = dir;
            this.length = length;
            this.id = id;
            
            this.settled = false;
            this.counted = false;
            this.supporters = new HashSet<>();
            this.supporting = new HashSet<>();
        }
        
        @Override
        public String toString() { return this.id; }
    }
    
    private static void part2(List<String> lines) {
        Pair<Map<Coord3D, Block>, Pair<Coord3D, Coord3D>> parseResults = parse(lines);
        
        Map<Coord3D, Block> blockMap = parseResults.a();
        Coord3D minCoord = parseResults.b().a(),
                maxCoord = parseResults.b().b();
        
        fall(blockMap, minCoord, maxCoord);
        
        // For each block, walk its supported tree according to which blocks are supported only by it
        long sum = 0;
        
        for(Block b : new HashSet<>(blockMap.values())) {
            //System.out.println(b);
            Deque<Block> blockQueue = new ArrayDeque<>();
            Set<Block> gone = new HashSet<>();
            
            lb2:
            for(Block b2 : b.supporting) {
                if(!gone.contains(b2)) {
                    for(Block b3 : b2.supporters) {
                        if(b3 != b && !gone.contains(b3)) {
                            continue lb2;
                        }
                    }
                    
                    gone.add(b2);
                    blockQueue.offer(b2);
                }
            }
            
            while(!blockQueue.isEmpty()) {
                Block cb = blockQueue.poll();
                sum++;
                
                //System.out.println("\t" + cb);
                
                lb2:
                for(Block b2 : cb.supporting) {
                    if(!gone.contains(b2)) {
                        for(Block b3 : b2.supporters) {
                            if(b3 != cb && !gone.contains(b3)) {
                                continue lb2;
                            }
                        }
                        
                        gone.add(b2);
                        blockQueue.offer(b2);
                    }
                }
            }
        }
        
        System.out.println(sum);
    }
    
    /**
     * Parse input into brick grid
     * Make bricks fall into place
     * Construct a support graph (directed graph from supporting to supported)
     * Count leaf nodes (zero supported)
     * 
     * @param lines
     */
    private static void part1(List<String> lines) {
        Pair<Map<Coord3D, Block>, Pair<Coord3D, Coord3D>> parseResults = parse(lines);
        
        Map<Coord3D, Block> blockMap = parseResults.a();
        Coord3D minCoord = parseResults.b().a(),
                maxCoord = parseResults.b().b();
        
        fall(blockMap, minCoord, maxCoord);
        
        //visualize(blockMap, minCoord, maxCoord);
        
        // count non-supporter blocks
        int count = 0;
        
        for(Block b : blockMap.values()) {
            if(b.counted) continue;
            
            //System.out.printf("%s supporting %s%n", b.id, b.supporting);
            
            b.counted = true;
            boolean canRem = true;
            
            if(b.supporting.size() == 0) canRem = true;
            else {
                // check that the blocks being supported have other supporters
                for(Block b2 : b.supporting) {
                    if(b2.supporters.size() <= 1) {
                        canRem = false;
                        break;
                    }
                }
            }
            
            if(canRem) {
                count++;
            }
        }
        
        System.out.println(count);
    }
    
    /**
     * Settles the blocks into their positions
     * 
     * @param blockMap
     * @param minCoord
     * @param maxCoord
     */
    private static void fall(Map<Coord3D, Block> blockMap, Coord3D minCoord, Coord3D maxCoord) {
        // make blocks fall
        for(int z = minCoord.z(); z <= maxCoord.z(); z++) {
            for(int x = minCoord.x(); x <= maxCoord.x(); x++) {
                for(int y = minCoord.y(); y <= maxCoord.y(); y++) {
                    //System.out.println(new Coord3D(x, y, z));
                    Block b = blockMap.getOrDefault(new Coord3D(x, y, z), null);
                    
                    // is there a block, and is it unprocessed
                    if(b != null && !b.settled) {
                        b.settled = true;
                        
                        //System.out.println(b + " " + b.origin + " " + b.length + " " + b.dir);
                        
                        if(z == 0) {
                            // supported by ground
                            continue;
                        }
                        
                        // remove from map
                        switch(b.dir) {
                            case SOUTH:
                                int end = b.origin.y() + b.length;
                                for(int y2 = b.origin.y(); y2 <= end; y2++) {
                                    blockMap.remove(new Coord3D(x, y2, z));
                                }
                                break;
                            
                            case EAST:
                                end = b.origin.x() + b.length;
                                for(int x2 = b.origin.x(); x2 <= end; x2++) {
                                    blockMap.remove(new Coord3D(x2, y, z));
                                }
                                break;
                            
                            case UP:
                                end = b.origin.z() + b.length;
                                for(int z2 = b.origin.z(); z2 <= end; z2++) {
                                    blockMap.remove(new Coord3D(x, y, z2));
                                }
                                break;
                            
                            default:
                        }
                        
                        // fall
                        Block supporter = null;
                        
                        out:
                        while(true) {
                            // check below block
                            switch(b.dir) {
                                case SOUTH:
                                    int end = b.origin.y() + b.length;
                                    boolean supported = false;
                                    for(int y2 = b.origin.y(); y2 <= end; y2++) {
                                        supporter = blockMap.getOrDefault(new Coord3D(x, y2, b.origin.z() - 1), null);
                                        
                                        if(supporter != null) {
                                            //System.out.println(b + " supported by " + supporter + " at " + new Coord3D(x, y2, z - 1));
                                            supported = true;
                                            supporter.supporting.add(b);
                                            b.supporters.add(supporter);
                                        }
                                    }
                                    
                                    if(supported) {
                                        break out;
                                    }
                                    break;
                                
                                case EAST:
                                    end = b.origin.x() + b.length;
                                    supported = false;
                                    for(int x2 = b.origin.x(); x2 <= end; x2++) {
                                        supporter = blockMap.getOrDefault(new Coord3D(x2, y, b.origin.z() - 1), null);
                                        
                                        if(supporter != null) {
                                            //System.out.println(b + " supported by " + supporter + " at " + new Coord3D(x2, y, z - 1));
                                            supported = true;
                                            supporter.supporting.add(b);
                                            b.supporters.add(supporter);
                                        }
                                    }
                                    
                                    if(supported) {
                                        break out;
                                    }
                                    break;
                                    
                                case UP:
                                    supporter = blockMap.getOrDefault(new Coord3D(x, y, b.origin.z() - 1), null);
                                    
                                    if(supporter != null) {
                                        //System.out.println(b + " supported by " + supporter + " at " + new Coord3D(x, y, z - 1));
                                        supporter.supporting.add(b);
                                        b.supporters.add(supporter);
                                        break out;
                                    }
                                    break;
                                
                                default:
                                    // invalid
                            }
                            
                            // not supported yet, move down 1
                            b.origin = new Coord3D(b.origin.x(), b.origin.y(), b.origin.z() - 1);
                            
                            if(b.origin.z() == 0) break;
                        }
                        
                        // replace in map
                        switch(b.dir) {
                            case SOUTH:
                                int end = b.origin.y() + b.length;
                                for(int y2 = b.origin.y(); y2 <= end; y2++) {
                                    Coord3D pos = new Coord3D(x, y2, b.origin.z());
                                    if(blockMap.containsKey(pos)) throw new IllegalStateException("overwrote block");
                                    blockMap.put(pos, b);
                                }
                                break;
                            
                            case EAST:
                                end = b.origin.x() + b.length;
                                for(int x2 = b.origin.x(); x2 <= end; x2++) {
                                    Coord3D pos = new Coord3D(x2, y, b.origin.z());
                                    if(blockMap.containsKey(pos)) throw new IllegalStateException("overwrote block");
                                    blockMap.put(pos, b);
                                }
                                break;
                            
                            case UP:
                                end = b.origin.z() + b.length;
                                for(int z2 = b.origin.z(); z2 <= end; z2++) {
                                    Coord3D pos = new Coord3D(x, y, z2);
                                    if(blockMap.containsKey(pos)) throw new IllegalStateException("overwrote block");
                                    blockMap.put(pos, b);
                                }
                                break;
                            
                            default:
                        }
                    }
                }
            }
        }
    }
    
    private static void visualize(Map<Coord3D, Block> blockMap, Coord3D min, Coord3D max) {
        // front view
        System.out.println("front");
        for(int z = max.z(); z >= 0; z--) {
            for(int x = min.x(); x <= max.x(); x++) {
                char c = '.';
                
                for(int y = min.y(); y <= max.y(); y++) {
                    Block b = blockMap.getOrDefault(new Coord3D(x, y, z), null);
                    
                    if(b != null) {
                        c = (c == '.') ? b.id.charAt(0) : (c == b.id.charAt(0) ? c : '?');
                    }
                }
                
                System.out.print(c);
            }
            System.out.println();
        }
        System.out.println();
        
        // side view
        System.out.println("side");
        for(int z = max.z(); z >= 0; z--) {
            for(int y = min.y(); y <= max.y(); y++) {
                char c = '.';
                
                for(int x = min.x(); x <= max.x(); x++) {
                    Block b = blockMap.getOrDefault(new Coord3D(x, y, z), null);
                    
                    if(b != null) {
                        c = (c == '.') ? b.id.charAt(0) : (c == b.id.charAt(0) ? c : '?');
                    }
                }
                
                System.out.print(c);
            }
            System.out.println();
        }
        System.out.println();
    }
    
    /**
     * parses the input
     * 
     * @param lines
     * @return Pair of a map from coord to block, and a Pair of Coord3D representing the minimum and maximum coordinates
     */
    private static Pair<Map<Coord3D, Block>, Pair<Coord3D, Coord3D>> parse(List<String> lines) {
        Map<Coord3D, Block> cubeMap = new HashMap<>();
        
        Coord3D min = new Coord3D(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE),
                max = new Coord3D(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
        
        int count = 0;
        
        for(String ln : lines) {
            String[] split = ln.split("[,~]");
            
            int x0 = Integer.parseInt(split[0]),
                y0 = Integer.parseInt(split[1]),
                z0 = Integer.parseInt(split[2]) - 1, // correct so that 0 = on ground rather than 0 = the ground
                x1 = Integer.parseInt(split[3]),
                y1 = Integer.parseInt(split[4]),
                z1 = Integer.parseInt(split[5]) - 1;
            
            String id = "" + (char)('A' + count++);
            
            if(x0 < min.x()) min = new Coord3D(x0, min.y(), min.z());
            if(y0 < min.y()) min = new Coord3D(min.x(), y0, min.z());
            if(z0 < min.z()) min = new Coord3D(min.x(), min.y(), z0);
            if(x1 > max.x()) max = new Coord3D(x1, max.y(), max.z());
            if(y1 > max.y()) max = new Coord3D(max.x(), y1, max.z());
            if(z1 > max.z()) max = new Coord3D(max.x(), max.y(), z1);
            
            Coord3D origin = new Coord3D(x0, y0, z0);
            Block block;
            
            if(x0 != x1) {
                block = new Block(origin, Direction3D.EAST, x1 - x0, id);
                
                // block extends in x
                for(int x3 = x0; x3 <= x1; x3++) {
                    cubeMap.put(new Coord3D(x3, y0, z0), block);
                }
            } else if(y0 != y1) {
                block = new Block(origin, Direction3D.SOUTH, y1 - y0, id);
                
                // block extends in y
                for(int y3 = y0; y3 <= y1; y3++) {
                    cubeMap.put(new Coord3D(x0, y3, z0), block);
                }
            } else {
                block = new Block(origin, Direction3D.UP, z1 - z0, id);
                
                // block extends in z, or not at all
                for(int z3 = z0; z3 <= z1; z3++) {
                    cubeMap.put(new Coord3D(x0, y0, z3), block);
                }
            }
        }
        
        return new Pair<>(cubeMap, new Pair<>(min, max));
    }
}
