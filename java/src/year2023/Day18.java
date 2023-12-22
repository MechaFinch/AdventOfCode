package year2023;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import global.util.AdventUtil;
import global.util.Coord2D;
import global.util.CoordF2D;
import global.util.CoordL2D;
import global.util.Direction2D;

/**
 * Day 18
 * 
 * minecraft
 */
public class Day18 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    private record Instruction(Direction2D dir, int dist) { }
    
    /**
     * Parse input
     * Follow instructions to generate points. These points need to be on the outside, not e.g. the top left of each square
     * Apply Gauss' Shoelace Formula (https://gamedev.stackexchange.com/questions/151034/how-to-compute-the-area-of-an-irregular-shape)
     * This can also do part 1 (and do it 10x faster) by using parse1 instead of parse2
     * 
     * @param lines
     */
    private static void part2(List<String> lines) {
        List<Instruction> instructions = parse2(lines);
        
        // follow instructions to generate points
        CoordL2D pos = new CoordL2D(0, 0);
        Direction2D prevDir = instructions.get(instructions.size() - 1).dir();
        
        List<CoordF2D> points = new ArrayList<>();
        
        for(Instruction i : instructions) {            
            // To get the full area, we need points on the outer corners rather than the middle/top-left only
            // the area is right-hand-interior for both inputs on both parts, that isn't technically guaranteed though
            switch(i.dir().pairity(prevDir)) {
                case -1:    // LH corner
                    switch(i.dir()) {
                        case NORTH:
                            points.add(new CoordF2D(pos.x() - 0.5, pos.y() - 0.5));
                            break;
                            
                        case SOUTH:
                            points.add(new CoordF2D(pos.x() + 0.5, pos.y() + 0.5));
                            break;
                            
                        case EAST:
                            points.add(new CoordF2D(pos.x() + 0.5, pos.y() - 0.5));
                            break;
                            
                        case WEST:
                            points.add(new CoordF2D(pos.x() - 0.5, pos.y() + 0.5));
                            break;
                    }
                    break;
                    
                case 1:     // RH corner
                    switch(i.dir()) {
                        case NORTH:
                            points.add(new CoordF2D(pos.x() - 0.5, pos.y() + 0.5));
                            break;
                            
                        case SOUTH:
                            points.add(new CoordF2D(pos.x() + 0.5, pos.y() - 0.5));
                            break;
                            
                        case EAST:
                            points.add(new CoordF2D(pos.x() - 0.5, pos.y() - 0.5));
                            break;
                            
                        case WEST:
                            points.add(new CoordF2D(pos.x() + 0.5, pos.y() + 0.5));
                            break;
                    }
                    break;
                    
                default:    // straight
                    switch(i.dir()) {
                        case NORTH:
                            points.add(new CoordF2D(pos.x() - 0.5, pos.y()));
                            break;
                            
                        case SOUTH:
                            points.add(new CoordF2D(pos.x() + 0.5, pos.y()));
                            break;
                            
                        case EAST:
                            points.add(new CoordF2D(pos.x(), pos.y() - 0.5));
                            break;
                            
                        case WEST:
                            points.add(new CoordF2D(pos.x(), pos.y() + 0.5));
                            break;
                    }
                    break;
            }
            
            pos = i.dir().convertL2D(pos, (long) i.dist());
            prevDir = i.dir();
        }
        
        // tie your shoelaces
        double area = 0;
        
        for(int i = 0; i < points.size(); i++) {
            area += (points.get(i).x() * points.get((i + 1) % points.size()).y()) -
                    (points.get(i).y() * points.get((i + 1) % points.size()).x());
        }
        
        System.out.println((long) Math.abs(area / 2));
    }
    
    /**
     * Parse input
     * For each line, dig n tiles in the given direction
     * Determine the area of the shape dug out
     * 
     * @param lines
     */
    private static void part1(List<String> lines) {
        List<Instruction> instructions = parse1(lines);
        
        // follow instructions to determine area size & interior handedness
        int x = 0, y = 0,
            minX = 0, maxX = 0,
            minY = 0, maxY = 0,
            pairity = 0; // determined that both inputs are RHS interior at (1, 1)
        
        Direction2D lastDirection = Direction2D.EAST; // both inputs start with R
        
        for(Instruction i : instructions) {
            // apply instruction
            int dx = i.dir().convertX(0, i.dist()),
                dy = i.dir().convertY(0, i.dist());
            
            x += dx;
            y += dy;
            
            minX = Math.min(x, minX);
            minY = Math.min(y, minY);
            maxX = Math.max(x, maxX);
            maxY = Math.max(y, maxY);
            
            // sanity check
            /*
            if(i.dir().pairity(lastDirection) == 0 && i.dir() != lastDirection) {
                throw new IllegalStateException("no pairity for you");
            }
            */
            
            pairity += i.dir().pairity(lastDirection);
            lastDirection = i.dir();
        }
        
        // follow instructions to build boundary
        boolean[][] grid = new boolean[1 + maxX - minX][1 + maxY - minY];
        
        x = 0 - minX;
        y = 0 - minY;
        for(Instruction i : instructions) {
            if(i.dir() == Direction2D.NORTH || i.dir() == Direction2D.SOUTH) { 
                int dy = i.dir().convertY(0, i.dist()),
                    dys = (dy > 0) ? 1 : -1;
                
                for(int yOffs = 0; yOffs < Math.abs(dy); yOffs++) {
                    grid[x][y + (yOffs * dys)] = true;
                }
                
                y += dy;
            } else {
                int dx = i.dir().convertX(0, i.dist()),
                    dxs = (dx > 0) ? 1 : -1;
                
                for(int xOffs = 0; xOffs < Math.abs(dx); xOffs++) {
                    grid[x + (xOffs * dxs)][y] = true;
                }
                
                x += dx;
            }
        }
        
        // flood fill interior
        Deque<Coord2D> toFillQueue = new ArrayDeque<>();
        toFillQueue.offer(new Coord2D(1 - minX, 1 - minY));
        
        while(!toFillQueue.isEmpty()) {
            Coord2D p = toFillQueue.poll();
            
            if(grid[p.x()][p.y()]) continue;
            grid[p.x()][p.y()] = true;
            
            toFillQueue.offer(Direction2D.NORTH.convert2D(p, 1));
            toFillQueue.offer(Direction2D.SOUTH.convert2D(p, 1));
            toFillQueue.offer(Direction2D.EAST.convert2D(p, 1));
            toFillQueue.offer(Direction2D.WEST.convert2D(p, 1));
        }
        
        // count filled area
        long count = 0;
        for(int y2 = 0; y2 < grid[0].length; y2++) {
            for(int x2 = 0; x2 < grid.length; x2++) {
                count += grid[x2][y2] ? 1 : 0;
                
                System.out.print(grid[x2][y2] ? "#" : ".");
            }
            System.out.println();
        }
        System.out.println();
        
        System.out.println(count);
    }
    
    /**
     * Parses the input into Instructions for part 1
     * 
     * @param lines
     * @return
     */
    private static List<Instruction> parse2(List<String> lines) {
        List<Instruction> instructions = new ArrayList<>(lines.size());
        
        for(String ln : lines) {
            String[] sp = ln.split(" ");
            
            int val = Integer.parseInt(sp[2].substring(2, sp[2].length() - 1), 16);
            
            instructions.add(new Instruction(
                switch(val & 0x0F) {
                    case 1  -> Direction2D.SOUTH; // D
                    case 2  -> Direction2D.WEST;  // L
                    case 3  -> Direction2D.NORTH; // U
                    default -> Direction2D.EAST; // R
                },
                val >> 4
            ));
            
            //System.out.println(instructions.get(instructions.size() - 1));
        }
        
        return instructions;
    }
    
    /**
     * Parses the input into Instructions for part 1
     * 
     * @param lines
     * @return
     */
    private static List<Instruction> parse1(List<String> lines) {
        List<Instruction> instructions = new ArrayList<>(lines.size());
        
        for(String ln : lines) {
            String[] sp = ln.split(" ");
            
            instructions.add(new Instruction(
                switch(sp[0]) {
                    case "U"    -> Direction2D.NORTH;
                    case "D"    -> Direction2D.SOUTH;
                    case "L"    -> Direction2D.WEST;
                    default     -> Direction2D.EAST; // "R"
                },
                Integer.parseInt(sp[1])
            ));
        }
        
        return instructions;
    }
}
