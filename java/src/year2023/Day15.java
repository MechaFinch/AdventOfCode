package year2023;

import java.io.File;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import global.util.AdventUtil;

/**
 * Day 15
 * 
 * HASHing
 */
public class Day15 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    /**
     * A box containing lenses
     */
    private static class Box {
        public LinkedList<Lens> lenses = new LinkedList<>();
        
        public Box() {
            this.lenses = new LinkedList<>();
        }
        
        /**
         * Adds a lens to the box
         * 
         * @param l
         */
        public void put(Lens l) {
            // replace if same label present
            for(int i = 0; i < this.lenses.size(); i++) {
                Lens cl = this.lenses.get(i);
            
                if(cl.label().equals(l.label())) {
                    this.lenses.set(i, l);
                    return;
                }
            }
            
            // not present
            this.lenses.add(l);
        }
        
        /**
         * Removes a lens
         * 
         * @param label
         */
        public void remove(String label) {
            for(int i = 0; i < this.lenses.size(); i++) {
                Lens l = this.lenses.get(i);
                
                if(l.label().equals(label)) {
                    this.lenses.remove(i);
                    return;
                }
            }
        }
        
        /**
         * Gets the total focusing power of the lenses in the box according to its number
         * 
         * @param boxNumber
         * @return
         */
        public long getFocusingPower(int boxNumber) {
            long fp = 0;
            
            for(int i = 0; i < this.lenses.size(); i++) {
                Lens l = this.lenses.get(i);
                
                fp += (boxNumber + 1) * (i + 1) * l.focalLength();
            }
            
            return fp;
        }
    }

    /**
     * A lens
     */
    private record Lens(String label, long focalLength) {
        
    }
    
    /**
     * Steps have 2-3 parts:
     * 1. Label. Hash this for box index
     * 2. Operation. = -> set box to value; - -> remove lens w/ label if present
     * 3. Value (if applicable)
     * 
     * If a box already has a given label when setting, replace
     * Lenses are ordered within boxes.
     * 
     * Find the FP of all lenses: sum((box num + 1) * (lens index + 1) * focal length)
     * 
     * @param lines
     */
    private static void part2(List<String> lines) {
        String line = lines.get(0);
        Box[] boxes = new Box[256];
        
        // init boxes
        for(int i = 0; i < boxes.length; i++) {
            boxes[i] = new Box();
        }
        
        // follow instructions
        for(String step : line.split(",")) {
            int eqIndex = step.indexOf("="),
                miIndex = step.indexOf("-"),
                labelLen = eqIndex == -1 ? miIndex : eqIndex;
            
            boolean opEq = miIndex == -1;
            
            String label = step.substring(0, labelLen);
            
            int boxNum = (int) hash(label);
            
            if(opEq) {
                // put
                boxes[boxNum].put(new Lens(label, Integer.parseInt(step.substring(eqIndex + 1))));
            } else {
                // remove
                boxes[boxNum].remove(label);
            }
            
            // debug
            /*
            System.out.println("After " + step);
            for(int i = 0; i < boxes.length; i++) {
                if(boxes[i].lenses.size() != 0) { 
                    System.out.print("Box " + i + ":");
                    
                    for(Lens l : boxes[i].lenses) {
                        System.out.print(" " + l);
                    }
                    
                    System.out.println();
                }
            }
            System.out.println();
            */
        }
        
        // find FP
        long fp = 0;
        
        for(int i = 0; i < boxes.length; i++) {
            fp += boxes[i].getFocusingPower(i);
        }
        
        System.out.println(fp);
    }
    
    /**
     * HASH each comma-separated step & sum the HASHes
     * 
     * @param lines
     */
    private static void part1(List<String> lines) {
        String line = lines.get(0);
        long sum = 0;
        
        for(String step : line.split(",")) {
            sum += hash(step);
        }
        
        System.out.println(sum);
    }
    
    /**
     * HASHes a string
     * 
     * @param s
     * @return
     */
    private static long hash(String s) {
        long v = 0;
        
        for(char c : s.toCharArray()) {
            v = ((v + c) * 17) % 256;
        }
        
        return v;
    }
}
