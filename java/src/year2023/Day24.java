package year2023;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import global.util.AdventUtil;
import global.util.Coord3D;
import global.util.CoordF3D;
import global.util.CoordL3D;

/**
 * Day 24
 * 
 * Lines
 */
public class Day24 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    private record Line(CoordL3D a, CoordL3D b) {
        /**
         * @param other Line to intersect
         * @return Intersection point, or null if none exists
         */
        public CoordF3D intersect(Line other) {
            // https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection#Given_two_points_on_each_line_segment
            double x1x2 = this.a.x() - this.b.x(),
                   x1x3 = this.a.x() - other.a.x(),
                   x3x4 = other.a.x() - other.b.x(),
                   y1y2 = this.a.y() - this.b.y(),
                   y1y3 = this.a.y() - other.a.y(),
                   y3y4 = other.a.y() - other.b.y();
            
            double tn = (x1x3 * y3y4) - (y1y3 * x3x4),
                   td = (x1x2 * y3y4) - (y1y2 * x3x4),
                   un = (x1x3 * y1y2) - (y1y3 * x1x2),
                   ud = (x1x2 * y3y4) - (y1y2 * x3x4);
            
            if(Math.abs(tn) <= Math.abs(td) && Math.signum(tn) == Math.signum(td) &&
               Math.abs(un) <= Math.abs(ud) && Math.signum(un) == Math.signum(ud)) {
                // intersection exists
                double t = tn / td,
                       u = un / td;
                
                double px = this.a.x() + (t * (this.b.x() - this.a.x())),
                       py = this.a.y() + (t * (this.b.y() - this.a.y()));
                
                return new CoordF3D(px, py, 0);
            } else {
                // no intersection
                return null;
            }
        }
    }
    
    /**
     * CURRENTLY DOES NOT WORK
     * coming back to this later
     * 
     * @param inLines
     */
    private static void part2(List<String> inLines) {
        List<Line> lines = parse(inLines, true, null, null);
        int numStones = lines.size(),
            timeMax = 10;
        
        List<CoordL3D> vels = new ArrayList<>(numStones);
        
        for(Line l : lines) vels.add(l.b());
        
        // for each time of first hit
        for(int startTime = 1; startTime < 2; startTime++) {
            // initialize position list
            List<CoordL3D> pos = new ArrayList<>(numStones);
            
            for(int i = 0; i < numStones; i++) {
                pos.add(lines.get(i).a().add(vels.get(i), startTime));
            }
            
            // For each pair of hailstones
            for(int i = 0; i < numStones; i++) {
                for(int j = 0; j < numStones; j++) {
                    if(j == i) continue;
                    
                    CoordL3D vel = tryCombo(pos, vels, i, j, timeMax);
                    
                    if(vel != null) {
                        // found a working velocity
                        CoordL3D startPos = pos.get(i).add(vel, -startTime);
                        
                        System.out.println(startPos.x() + startPos.y() + startPos.z());
                        return;
                    }
                }
            }
        }
    }
    
    /**
     * Attempts a line based on first two hits
     * For each delta from 1 to timeMax, find the stone velocity based on the two hits
     * If all stones can be hit with the given configuration, return velocity
     * If not, return null
     * 
     * @return
     */
    private static CoordL3D tryCombo(List<CoordL3D> ipos, List<CoordL3D> ivels, int firstIndex, int secondIndex, int timeMax) {
        // local modifyable copy
        List<CoordL3D> pos = new ArrayList<>(),
                       vels = new ArrayList<>();
        
        pos.addAll(ipos);
        vels.addAll(ivels);
        
        CoordL3D rockStart = pos.get(firstIndex);
        
        // walk through first hit time
        outer:
        for(long t = 1; t <= timeMax; t++) {
            step(pos, vels, 1);
            
            CoordL3D rockVelRaw = pos.get(secondIndex).subtract(rockStart, 1);
            
            // is this an integer velocity
            if(rockVelRaw.x() % t == 0 &&
               rockVelRaw.y() % t == 0 &&
               rockVelRaw.z() % t == 0) {
                
                // yes
                CoordL3D rockVel = new CoordL3D(
                    rockVelRaw.x() / t,
                    rockVelRaw.y() / t,
                    rockVelRaw.z() / t
                );
                
                // for each other rock
                for(int i = 0; i < pos.size(); i++) {
                    if(i == firstIndex || i == secondIndex) continue;
                    
                    long hitTime = hitTime(rockStart, rockVel, pos.get(i), vels.get(i));
                    
                    //System.out.println(hitTime);
                    
                    if(hitTime <= 0) {
                        // can't hit
                        continue outer;
                    }
                }
                
                // if we're here, all rocks have been hit.
                return rockVel;
            }
        }
        
        return null;
    }
    
    /**
     * Computes at what time the rock and hailstone collide
     * @param rock
     * @param rockVel
     * @param target
     * @param targetVel
     * @return Time to collision, or -1.
     */
    private static long hitTime(CoordL3D rock, CoordL3D rockVel, CoordL3D target, CoordL3D targetVel) {
        /*
         * If the two collide, this must be true:
         * rp + (rv * t) = tp + (tv * t)
         * rp - tp = t(tv - rv)
         * t = (rp - tp) / (tv - rv)
         */
        // avoid div by zero
        if(targetVel.x() == rockVel.x()) return -1;
        
        // compute candidate time
        double t = ((double)rock.x() - (double)target.x()) / ((double)targetVel.x() - (double)rockVel.x());
        long lt = (long) t;
        
        if(Math.abs(t % 1) == 0 && rock.add(rockVel, lt).equals(target.add(targetVel, lt))) {
            System.out.println("SLDJDLKGJDF");
            return lt;
        } else {
            return -1;
        }
    }
    
    /**
     * Steps a list of hailstones
     * 
     * @param hailPosList
     * @param velocityList
     * @param stepSize
     */
    private static void step(List<CoordL3D> hailPosList, List<CoordL3D> velocityList, long stepSize) {
        for(int i = 0; i < hailPosList.size(); i++) {
            hailPosList.set(i, hailPosList.get(i).add(velocityList.get(i), stepSize));
        }
    }
    
    /**
     * Parse to Lines
     * For each pair of Lines, determine the intersection point
     * 
     * @param lines
     */
    private static void part1(List<String> inLines) {
        CoordL3D minl = new CoordL3D(200000000000000l, 200000000000000l, 0),
                 maxl = new CoordL3D(400000000000000l, 400000000000000l, 0);
        
        CoordF3D minf = new CoordF3D(minl.x(), minl.y(), maxl.z()),
                 maxf = new CoordF3D(maxl.x(), maxl.y(), maxl.z());
        
        List<Line> lines = parse(inLines, false, minl, maxl);
        
        int count = 0;
        
        for(int i = 0; i < lines.size(); i++) {
            Line ln1 = lines.get(i);
            
            for(int j = i + 1; j < lines.size(); j++) {
                Line ln2 = lines.get(j);
                
                CoordF3D intersection = ln1.intersect(ln2);
                
                if(intersection != null && inside(intersection, minf, maxf)) {
                    /*
                    System.out.printf(
                        "(%.3f, %.3f) crosses (%.3f, %.3f) at (%.3f, %.3f)%n",
                        ln1.a.x(), ln1.a.y(),
                        ln2.a.x(), ln2.a.y(),
                        intersection.x(), intersection.y()
                    );
                    */
                    
                    count++;
                }
            }
        }
        
        System.out.println(count);
    }
    
    /**
     * @param point
     * @param min
     * @param max
     * @param useZ
     * @return true if point is inside the region defined by min and max
     */
    private static boolean inside(CoordF3D point, CoordF3D min, CoordF3D max) {
        boolean x = point.x() >= min.x() && point.x() <= max.x(),
                y = point.y() >= min.y() && point.y() <= max.y(),
                z = true;
        
        return x && y && z;
    }
    
    /**
     * Parses to a list of Lines
     * 
     * @param inLines input
     * @param useZ if true, Z is parsed. If false, Z is 0
     * @param min 
     * @param max
     * @return
     */
    private static List<Line> parse(List<String> inLines, boolean vel, CoordL3D min, CoordL3D max) {
        List<Line> lines = new ArrayList<>();
        
        for(String ln : inLines) {
            String[] split = ln.split("[@,]");
            
            for(int i = 0; i < split.length; i++) split[i] = split[i].trim();
            
            CoordL3D origin = new CoordL3D(
                Long.parseLong(split[0]),
                Long.parseLong(split[1]),
                Long.parseLong(split[2])
            ),
                     velocity = new CoordL3D(
                Long.parseLong(split[3]),
                Long.parseLong(split[4]),
                Long.parseLong(split[5])
            );
            
            if(vel) {
                // if this is true, the second point in the line is velocity rather than end
                lines.add(new Line(origin, velocity));
            } else {
                // get second at border of test area
                // find which coord will exit first, use for second point
                long txmin = (min.x() - origin.x()) / velocity.x(),
                     txmax = (max.x() - origin.x()) / velocity.x(),
                     tx = Math.max(txmin, txmax); // one will be negative
                
                long tymin = (min.y() - origin.y()) / velocity.y(),
                     tymax = (max.y() - origin.y()) / velocity.y(),
                     ty = Math.max(tymin, tymax); // one will be negative
                
                long t = Math.min(tx, ty);
            
                CoordL3D end = new CoordL3D(
                    origin.x() + (velocity.x() * t),
                    origin.y() + (velocity.y() * t),
                    origin.z() + (velocity.z() * t)
                );
                
                //System.out.printf("(%s, %s) -> (%s, %s)%n", origin.x(), origin.y(), end.x(), end.y());
                
                lines.add(new Line(origin, end));
            }
        }
        
        return lines;
    }
}
