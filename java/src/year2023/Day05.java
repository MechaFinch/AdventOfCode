package year2023;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import global.util.AdventUtil;

/**
 * Day 5
 * Agriculture
 */
public class Day05 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    /**
     * Seed values are ranges
     * If searching from seed -> location, requires a couple billion checks
     * Searching locations -> seeds may be much faster
     * 
     * @param lines
     */
    private static void part2(List<String> lines) {
        // parse seed list
        List<Long> seeds = Arrays.stream(lines.get(0).substring(7).split(" "))
                                 .mapToLong(Long::parseLong)
                                 .boxed()
                                 .collect(Collectors.toList());
        
        // parse maps
        List<AgricultureMap> mapList = new ArrayList<>();
        AgricultureMap workingMap = null;
        
        for(String ln : lines) {
            // skip start & empty lines
            if(ln.isBlank() || ln.startsWith("seeds")) continue;
            
            if(ln.endsWith("map:")) {
                // start a new map
                if(workingMap != null) {
                    mapList.add(workingMap);
                }
                
                workingMap = new AgricultureMap();
            } else {
                // parse a mapping
                String[] split = ln.split(" ");
                
                long d = Long.parseLong(split[0]),
                     s = Long.parseLong(split[1]),
                     l = Long.parseLong(split[2]);
                
                workingMap.addMapping(d, s, l);
            }
        }
        
        // don't forget last map
        mapList.add(workingMap);
        
        // I started writing code to search backwards from location to seed, but the naive version completed before I got very far (7.5 minutes)
        part2Naive(mapList, seeds);
    }
    
    /**
     * Naive approach to run in the background while working on a better approach
     * @param mapList
     * @param seeds
     */
    private static void part2Naive(List<AgricultureMap> mapList, List<Long> seeds) {
        // find location numbers
        List<Long> locationNumbers = new ArrayList<>(seeds.size());
        long minLocation = Long.MAX_VALUE;
        
        //System.out.println("se -> so -> fe -> wa -> li -> te -> hu -> location");
        
        for(int i = 0; i < seeds.size(); i += 2) {
            long start = seeds.get(i),
                 length = seeds.get(i + 1);
            
            System.out.println("Processing from " + start + " for " + length);
            
            for(long offset = 0; offset < length; offset++) {
                long val = start + offset;
                
                if(offset % 10_000_000l == 0) {
                    System.out.println(offset);
                }
                
                for(AgricultureMap am : mapList) {
                    val = am.get(val);
                }
                
                if(val < minLocation) minLocation = val;
            }
        }
        
        System.out.println(minLocation);
    }
    
    /**
     * Parse maps described by the input
     * Apply a series of mappings to each seed
     * @param lines
     */
    private static void part1(List<String> lines) {
        // parse seed list
        List<Long> seeds = Arrays.stream(lines.get(0).substring(7).split(" "))
                                 .mapToLong(Long::parseLong)
                                 .boxed()
                                 .collect(Collectors.toList());
        
        // parse maps
        List<AgricultureMap> mapList = new ArrayList<>();
        AgricultureMap workingMap = null;
        
        for(String ln : lines) {
            // skip start & empty lines
            if(ln.isBlank() || ln.startsWith("seeds")) continue;
            
            if(ln.endsWith("map:")) {
                // start a new map
                if(workingMap != null) {
                    mapList.add(workingMap);
                }
                
                workingMap = new AgricultureMap();
            } else {
                // parse a mapping
                String[] split = ln.split(" ");
                
                long d = Long.parseLong(split[0]),
                     s = Long.parseLong(split[1]),
                     l = Long.parseLong(split[2]);
                
                workingMap.addMapping(d, s, l);
            }
        }
        
        // don't forget last map
        mapList.add(workingMap);
        
        // find location numbers
        List<Long> locationNumbers = new ArrayList<>(seeds.size());
        long minLocation = Long.MAX_VALUE;
        
        //System.out.println("se -> so -> fe -> wa -> li -> te -> hu -> location");
        
        for(long seedNum : seeds) {
            
            // apply each mapping
            for(AgricultureMap am : mapList) {
                //System.out.print(seedNum + " -> ");
                seedNum = am.get(seedNum);
            }
            
            //System.out.println(seedNum);
            
            if(seedNum < minLocation) minLocation = seedNum;
        }
        
        System.out.println(minLocation);
    }
}

/**
 * Represents and applys agricultural mappings
 */
class AgricultureMap {
    private TreeMap<Long, Long> sourceLengthMap,
                                sourceDestMap,
                                destSourceMap;
    
    public AgricultureMap() {
        this.sourceDestMap = new TreeMap<>();
        this.sourceLengthMap = new TreeMap<>();
    }
    
    public void addMapping(long destStart, long sourceStart, long length) {
        this.sourceDestMap.put(sourceStart, destStart);
        this.sourceLengthMap.put(sourceStart, length);
    }
    
    /**
     * Finds a source which corresponds to this destination
     * 
     * @param destination
     * @return
     */
    public long getReverese(long destination) {
        return 0;
    }
    
    /**
     * Gets the mapped value of the input
     * 
     * @param source
     * @return
     */
    public long get(long source) {
        // find candidate mapping
        Long sourceStartBox = this.sourceLengthMap.floorKey(source);
        
        if(sourceStartBox == null) {
            // source is below smallest mapping
            return source;
        }
        
        long sourceStart = this.sourceLengthMap.floorKey(source),
             length = this.sourceLengthMap.get(sourceStart),
             destStart = this.sourceDestMap.get(sourceStart);
        
        if(sourceStart + length <= source) {
            // no mapping present
            return source;
        } else {
            // apply mapping
            return destStart + (source - sourceStart);
        }
    }
}
