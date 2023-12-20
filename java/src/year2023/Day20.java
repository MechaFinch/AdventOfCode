package year2023;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import global.util.AdventUtil;
import global.util.Pair;

/**
 * Day 20
 * 
 * Circuits!
 * and also the fucking halting problem???
 */
public class Day20 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    private enum PulseType {
        HIGH,
        LOW
    }
    
    record Pulse(String source, String target, PulseType type) { }
    
    private interface Module {
        /**
         * Sends a pulse to the Module, returning resulting pulses
         * 
         * @param source Name of pulse source
         * @param p Pulse value
         * @return List of names and pulses to send
         */
        public List<Pulse> sendPulse(Pulse p);
        
        /**
         * Gets this module's targets
         * 
         * @return
         */
        public List<String> getTargets();
    }
    
    /**
     * On a low pulse, toggles and sends new state
     */
    private static class FFModule implements Module {
        private String name;
        private boolean state;
        private List<String> targets;
        
        public FFModule(String name, List<String> targets) {
            this.name = name;
            this.targets = targets;
            
            this.state = false;
        }
        
        @Override
        public List<String> getTargets() { return this.targets; }
        
        @Override
        public List<Pulse> sendPulse(Pulse p) {
            List<Pulse> outputs = new ArrayList<>();
            
            if(p.type == PulseType.LOW) {
                this.state = !this.state;
                
                for(String t : this.targets) {
                    outputs.add(new Pulse(this.name, t, this.state ? PulseType.HIGH : PulseType.LOW));
                }
            }
            
            return outputs;
        }
    }
    
    /**
     * Tracks last recieved pulse from each source
     * On pulse recieved, update then send low if all high and high otherwise
     */
    private static class ConModule implements Module {
        String name;
        private List<String> targets;
        private Map<String, PulseType> lastRecievedMap;
        
        public ConModule(String name, List<String> targets) {
            this.name = name;
            this.targets = targets;
            
            this.lastRecievedMap = new HashMap<>();
        }
        
        /**
         * Adds a source to the module's tracking list
         * 
         * @param s
         */
        public void addSource(String s) {
            this.lastRecievedMap.put(s, PulseType.LOW);
        }
        
        @Override
        public List<String> getTargets() { return this.targets; }
        
        @Override
        public List<Pulse> sendPulse(Pulse p) {
            List<Pulse> outputs = new ArrayList<>();
            
            // update
            this.lastRecievedMap.put(p.source, p.type);
            
            // check
            PulseType ot = PulseType.LOW;
            
            for(PulseType rt : this.lastRecievedMap.values()) {
                if(rt == PulseType.LOW) {
                    ot = PulseType.HIGH;
                    break;
                }
            }
            
            // build output
            for(String t : this.targets) {
                outputs.add(new Pulse(this.name, t, ot));
            }
            
            return outputs;
        }
    }
    
    /**
     * Echoes its pulse to target modules
     */
    private static class BroadcastModule implements Module {
        private List<String> targets;
        
        public BroadcastModule(List<String> targets) {
            this.targets = targets;
        }
        
        @Override
        public List<String> getTargets() { return this.targets; }
        
        @Override
        public List<Pulse> sendPulse(Pulse p) {
            List<Pulse> outputs = new ArrayList<>();
            
            for(String t : this.targets) {
                outputs.add(new Pulse("broadcaster", t, p.type));
            }
            
            return outputs;
        }
    }
    
    /**
     * Analyze the circuit to determine how long things take
     * 
     * @param lines
     */
    private static void part2(List<String> lines) {
        Map<String, Module> modules = parse(lines);
        
        // from static analysis: tt, qz, cq, and jx need to output HIGH simultaneously
        // find their periods, then find the LCM
        List<Long> cycleLengths = new ArrayList<>(4);
        
        List<String> targets = List.of("tt", "qz", "cq", "jx");
        Set<String> found = new HashSet<>();
        
        // simulate until each has output a HIGH
        Deque<Pulse> pulseQueue = new ArrayDeque<>();
        long numPresses = 0;
        
        out:
        while(true) {
            numPresses++;
            pulseQueue.add(new Pulse("button", "broadcaster", PulseType.LOW)); // button pulse
            
            while(!pulseQueue.isEmpty()) {
                Pulse p = pulseQueue.poll();
                
                if(modules.containsKey(p.target)) {
                    List<Pulse> sent = modules.get(p.target).sendPulse(p);
                    
                    for(Pulse sp : sent) {
                        if(p.type == PulseType.HIGH && targets.contains(p.source)) {
                            if(!found.contains(p.source)) {
                                cycleLengths.add(numPresses);
                                found.add(p.source);
                                
                                if(found.size() == targets.size()) break out;
                            }
                        }
                        
                        pulseQueue.offer(sp);
                    }
                }
            }
        }
        
        System.out.println(cycleLengths);
        
        // find least common multiple of all cycles
        System.out.println(AdventUtil.lcm(cycleLengths));
    }
    
    /**
     * Parse input
     * run 1000 button presses
     * 
     * @param lines
     */
    private static void part1(List<String> lines) {
        // parse
        Map<String, Module> modules = parse(lines);
        
        // run
        long numLow = 0,
             numHigh = 0;
        
        Deque<Pulse> pulseQueue = new ArrayDeque<>();
        
        for(int i = 0; i < 1000; i++) {
            pulseQueue.add(new Pulse("button", "broadcaster", PulseType.LOW)); // button pulse
            
            while(!pulseQueue.isEmpty()) {
                Pulse p = pulseQueue.poll();
                
                if(p.type == PulseType.LOW) { 
                    numLow++;
                } else {
                    numHigh++;
                }
                
                //System.out.println(p);
                
                if(modules.containsKey(p.target)) {
                    List<Pulse> sent = modules.get(p.target).sendPulse(p);
                    
                    for(Pulse sp : sent) {
                        pulseQueue.offer(sp);
                    }
                }
            }
        }
        
        System.out.println(numLow * numHigh);
    }
    
    /**
     * Parses input into modules
     * 
     * @param lines
     * @return
     */
    private static Map<String, Module> parse(List<String> lines) {
        Map<String, Module> modules = new HashMap<>();
        Set<String> conMods = new HashSet<>();
        
        // parse
        for(String ln : lines) {
            String[] split = ln.split(" -> ");
            String name = split[0],
                   targetString = split[1];
            
            char prefix = name.charAt(0);
            name = name.substring(1);
            
            // get targets
            List<String> targets = new ArrayList<>();
            for(String s : targetString.split(",")) {
                s = s.trim();
                targets.add(s);
            }
            
            // make modules
            switch(prefix) {
                case '%':
                    // flip flop
                    modules.put(name, new FFModule(name, targets));
                    break;
                
                case '&':
                    // conjunction
                    modules.put(name, new ConModule(name, targets));
                    conMods.add(name);
                    break;
                
                default:
                    // no prefix
                    if(name.equals("roadcaster")) {
                        modules.put("broadcaster", new BroadcastModule(targets));
                    } else {
                        throw new IllegalArgumentException("Invalid module: " + ln);
                    }
            }
        }
        
        // assign conjunction inputs
        for(Entry<String, Module> m : modules.entrySet()) {
            for(String t : m.getValue().getTargets()) {
                if(conMods.contains(t)) {
                    ((ConModule) modules.get(t)).addSource(m.getKey());
                }
            }
        }
        
        return modules;
    }
}
