package year2024;

import java.io.File;
import java.io.IOException;
import java.util.List;

import global.util.AdventUtil;
import global.util.Coord2D;

public class Day04 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    private static void part2(List<String> lines) {
        // to (x, y) indexed array
        char[][] chars = new char[lines.get(0).length()][lines.size()];
        
        for(int i = 0; i < lines.size(); i++) {
            char[] ln = lines.get(i).toCharArray();
            
            for(int j = 0; j < ln.length; j++) {
                chars[j][i] = ln[j];
            }
        }
        
        int count = 0;
        
        for(int x = 1; x < chars.length - 1; x++) {
            for(int y = 1; y < chars[0].length - 1; y++) {
                // center
                if(chars[x][y] != 'A') {
                    continue;
                }
                
                // left diagonal
                if(!((chars[x - 1][y - 1] == 'M' && chars[x + 1][y + 1] == 'S') ||
                     (chars[x - 1][y - 1] == 'S' && chars[x + 1][y + 1] == 'M'))) {
                    continue;
                }
                
                // right diagonal
                if(!((chars[x - 1][y + 1] == 'M' && chars[x + 1][y - 1] == 'S') ||
                     (chars[x - 1][y + 1] == 'S' && chars[x + 1][y - 1] == 'M'))) {
                    continue;
                }
                
                count++;
            }
        }
        
        System.out.println(count);
    }
    
    private static void part1(List<String> lines) {
        // to (x, y) indexed array
        char[][] chars = new char[lines.get(0).length()][lines.size()];
        
        for(int i = 0; i < lines.size(); i++) {
            char[] ln = lines.get(i).toCharArray();
            
            for(int j = 0; j < ln.length; j++) {
                chars[j][i] = ln[j];
            }
        }
        
        // search
        List<Coord2D> directions = List.of(
            new Coord2D(-1, 0), new Coord2D(1, 0),      // +/- x
            new Coord2D(0, -1), new Coord2D(0, 1),      // +/- y
            new Coord2D(-1, -1), new Coord2D(1, -1),    // upwards diagonal
            new Coord2D(-1, 1), new Coord2D(1, 1)       // downwards diagonal
        );
        
        String target = "XMAS";
        int count = 0;
        
        // Checking each direction
        for(Coord2D dir : directions) {
            // From each origin
            for(int ox = 0; ox < chars.length; ox++) {
                searching:
                for(int oy = 0; oy < chars[0].length; oy++) {
                    Coord2D loc = new Coord2D(ox, oy);
                    
                    for(int i = 0; i < target.length(); i++) {
                        if(!loc.checkBounds(chars.length, chars[0].length) || chars[loc.x()][loc.y()] != target.charAt(i)) {
                            continue searching;
                        }
                        
                        loc = loc.add(dir, 1);
                    }
                    
                    count++;
                }
            }
        }
        
        System.out.println(count);
    }
}
