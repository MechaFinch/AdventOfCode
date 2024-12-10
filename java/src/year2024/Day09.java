package year2024;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import global.util.AdventUtil;

public class Day09 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    private static void part2(List<String> lines) {
        // Determine disk contents
        char[] map = lines.get(0).toCharArray();
        
        int id = 0;
        List<Integer> disk = new ArrayList<>();
        List<Integer> filePositions = new ArrayList<>();
        List<Integer> fileSizes = new ArrayList<>();
        
        for(int i = 0; i < map.length; i++) {
            int v = map[i] - '0';
            
            if((i & 1) == 1) {
                // Digit is free space
                for(int j = 0; j < v; j++) {
                    disk.add(-1);
                }
            } else {
                // Digit is occupied space
                filePositions.add(disk.size());
                fileSizes.add(v);
                
                for(int j = 0; j < v; j++) {
                    disk.add(id);
                }
                
                id++;
            }
        }
        
        /*
        for(int i = 0; i < disk.size(); i++) {
            int v = disk.get(i);
            
            if(v == -1) {
                System.out.print(".");
            } else {
                System.out.print(disk.get(i));
            }
        }
        System.out.println();
        */
        
        // Compact disk
        for(int f = filePositions.size() - 1; f > 0; f--) {
            // Find a spot for the file
            int fileStart = filePositions.get(f);
            int fileSize = fileSizes.get(f);
            int destStart = 0;
            boolean found = false;
            
            for(int i = 0, span = 0; i < fileStart; i++) {
                if(disk.get(i) != -1) {
                    span = 0;
                    destStart = i + 1;
                } else {
                    span++;
                    
                    if(span >= fileSize) {
                        found = true;
                        break;
                    }
                }
            }
            
            if(found) {
                // found a spot, move file
                for(int i = 0; i < fileSize; i++) {
                    disk.set(destStart + i, disk.get(fileStart + i));
                    disk.set(fileStart + i, -1);
                }
            }
        }
        
        /*
        for(int i = 0; i < disk.size(); i++) {
            int v = disk.get(i);
            
            if(v == -1) {
                System.out.print(".");
            } else {
                System.out.print(disk.get(i));
            }
        }
        System.out.println();
        */
        
        // Compute checksum
        long sum = 0;
        
        for(int i = 0; i < disk.size(); i++) {
            int v = disk.get(i);
            
            if(v == -1) {
                continue;
            }
            
            sum += ((long) i) * ((long) v);
        }
        
        System.out.println(sum);
    }
    
    private static void part1(List<String> lines) {
        // Determine disk contents
        char[] map = lines.get(0).toCharArray();
        
        int id = 0;
        List<Integer> disk = new ArrayList<>();
        
        for(int i = 0; i < map.length; i++) {
            int v = map[i] - '0';
            
            if((i & 1) == 1) {
                // Digit is free space
                for(int j = 0; j < v; j++) {
                    disk.add(-1);
                }
            } else {
                // Digit is occupied space
                for(int j = 0; j < v; j++) {
                    disk.add(id);
                }
                
                id++;
            }
        }
        
        // Compact disk
        int dest = 0;
        while(disk.get(dest) != -1) dest++;
        
        for(int src = disk.size() - 1; dest < src; src--) {
            // If disk[src] is an ID, move it to dest, then increment dest to the next open space
            int s = disk.get(src);
            if(s != -1) {
                disk.set(dest, s);
                disk.set(src, -1);
                while(disk.get(dest) != -1) dest++;
            } else {
                // src is empty, do nothing
            }
        }
        
        // Compute checksum
        long sum = 0;
        
        for(int i = 0; i < disk.size(); i++) {
            int v = disk.get(i);
            
            if(v == -1) {
                // stop when empty spot reached
                break;
            }
            
            sum += ((long) i) * ((long) v);
        }
        
        System.out.println(sum);
    }
}
