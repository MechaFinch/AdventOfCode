package year2023;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
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
        List<Long> seeds = AdventUtil.toLongList(lines.get(0).substring(7), " ");
        
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
        
        /* naive version
         * ran in the background while writing fast version, completes in 4 minutes
         * fast version completes in 4.5 seconds
         *
        part2Naive(mapList, seeds);
        */
        
        // map for quick seed checks
        TreeMap<Long, Long> seedMap = new TreeMap<>();
        
        for(int i = 0; i < seeds.size(); i += 2) {
            // key = start, value = length
            seedMap.put(seeds.get(i), seeds.get(i + 1));
        }
        
        // linear search from smallest location value
        for(int l = 0; l < Long.MAX_VALUE; l++) {
            // apply maps in reverse
            long val = l;
            
            for(int i = mapList.size() - 1; i >= 0; i--) {
                val = mapList.get(i).getReverese(val);
            }
            
            Entry<Long, Long> seedRange = seedMap.floorEntry(val);
            
            // is it in range
            if(seedRange != null && seedRange.getKey() + seedRange.getValue() > val) {
                System.out.println(l);
                return;
            }
        }
    }
    
    /**
     * Naive approach to run in the background while working on a better approach
     * 
     * @param mapList
     * @param seeds
     */
    private static void part2Naive(List<AgricultureMap> mapList, List<Long> seeds) {
        // find location numbers
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
        List<Long> seeds = AdventUtil.toLongList(lines.get(0).substring(7), " ");
        
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
    private HashMap<Long, Long> sourceLengthMap;
    
    private TreeMap<Long, Long> sourceDestMap,
                                destSourceMap;
    
    public AgricultureMap() {
        this.sourceDestMap = new TreeMap<>();
        this.destSourceMap = new TreeMap<>();
        this.sourceLengthMap = new HashMap<>();
    }
    
    public void addMapping(long destStart, long sourceStart, long length) {
        this.sourceDestMap.put(sourceStart, destStart);
        this.destSourceMap.put(destStart, sourceStart);
        this.sourceLengthMap.put(sourceStart, length);
    }
    
    /**
     * Finds a source which corresponds to this destination
     * 
     * @param destination
     * @return
     */
    public long getReverese(long destination) {
        Entry<Long, Long> destStartBox = this.destSourceMap.floorEntry(destination);
        
        // below smallest?
        if(destStartBox == null) {
            return destination;
        }
        
        long destStart = destStartBox.getKey(),
             sourceStart = destStartBox.getValue(),
             length = this.sourceLengthMap.get(sourceStart);
        
        if(destStart + length <= destination) {
            // no mapping
            return destination;
        } else {
            // apply mapping
            return sourceStart + (destination - destStart);
        }
    }
    
    /**
     * Gets the mapped value of the input
     * 
     * @param source
     * @return
     */
    public long get(long source) {
        // find candidate mapping
        Entry<Long, Long> sourceStartBox = this.sourceDestMap.floorEntry(source);
        
        if(sourceStartBox == null) {
            // source is below smallest mapping
            return source;
        }
        
        long sourceStart = sourceStartBox.getKey(),
             destStart = sourceStartBox.getValue(),
             length = this.sourceLengthMap.get(sourceStart);
        
        if(sourceStart + length <= source) {
            // no mapping present
            return source;
        } else {
            // apply mapping
            return destStart + (source - sourceStart);
        }
    }
}
