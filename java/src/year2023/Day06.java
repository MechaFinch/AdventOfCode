package year2023;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import global.util.AdventUtil;

/**
 * Day 6
 * Boat Racing
 */
public class Day06 {
    public static void run(File f) throws IOException {
        part1(AdventUtil.inputList(f));
    }
    
    /**
     * Count the number of times that fail while incrementing, multiply by two, subtract from time alotted
     * Accumulate by multiplication
     * 
     * Part 2 achieved by removing spaces from the input & using longs instead of ints
     * 
     * @param lines
     */
    private static void part1(List<String> lines) {
        List<Long> timeList = parseLine(lines.get(0)),
                   distanceList = parseLine(lines.get(1));
        
        long acc = 1;
        
        // for each race
        for(int i = 0; i < timeList.size(); i++) {
            long timeAllowed = timeList.get(i),
                 distanceRecord = distanceList.get(i);
            
            // for each time
            for(int j = 0; j < timeAllowed; j++) {
                long distanceAchieved = j * (timeAllowed - j);
                
                if(distanceAchieved > distanceRecord) {
                    //System.out.printf("Beaten by %s, %s ways%n", j, timeAllowed - ((j - 1) * 2) - 1);
                    acc *= timeAllowed - ((j - 1) * 2) - 1;
                    break;
                }
            }
        }
        
        System.out.println(acc);
    }
    
    private static List<Long> parseLine(String ln) {
        String[] split = ln.substring(9)
                           .trim()
                           .split("\\s+");
        
        return Arrays.stream(split)
                     .mapToLong(Long::parseLong)
                     .boxed()
                     .toList();
    }
}
