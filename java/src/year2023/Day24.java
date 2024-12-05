package year2023;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import global.util.AdventUtil;
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
    
    static class Part2Thread extends Thread {
        
        static boolean solutionFound = false;
        
        private List<Line> lines;
        private Line lnA, lnB;
        
        private int start, size, step;
        
        Part2Thread(int start, int size, int step, List<Line> lines, Line lnA, Line lnB) {
            this.start = start;
            this.size = size;
            this.step = step;
            this.lines = lines;
            this.lnA = lnA;
            this.lnB = lnB;
        }
        
        @Override
        public void run() {
            for(int tMin = start; !solutionFound; tMin += step) {
                for(int tMax = tMin; tMax < tMin + size; tMax++) {
                    if(tMax % 1000 == 0) {
                        System.out.println("Searching max=" + tMax);
                    }
                    
                    // Search with collision time of lna = tMax
                    for(int tb = 1; tb < tMax; tb++) {
                        if(tryTrajectory(lines, lnB, lnA, tb, tMax)) {
                            solutionFound = true;
                            return;
                        }
                    }
                    
                    // Search with collision time of lnb = tMax
                    for(int ta = 1; ta < tMax; ta++) {
                        if(tryTrajectory(lines, lnA, lnB, ta, tMax)) {
                            solutionFound = true;
                            return;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * @param inLines
     */
    private static void part2(List<String> inLines) {
        /*
         * If every rock must be hit, we can construct our trajectory from any pair of rocks
         * Choosing two rocks, we can search through the hit time of each rock
         */
        List<Line> lines = parse(inLines, true, null, null);
        Line lnA = lines.get(0), lnB = lines.get(1);
        
        List<Thread> threads = new ArrayList<>();
        
        int start = 550000;
        int size = 10000;
        int count = 4;
        
        for(int i = 0; i < count; i++) {
            threads.add(new Part2Thread(start + (i * size), size, size * count, lines, lnA, lnB));
            threads.get(i).start();
        }
        
        for(int i = 0; i < count; i++) {
            try {
                threads.get(i).join();
            } catch(InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Try a trajectory defined by the collision with lnA and lnB given ta < tb
     * @param lines
     * @param ta
     * @param tb
     * @return true if successful
     */
    private static boolean tryTrajectory(List<Line> lines, Line lnA, Line lnB, int ta, int tb) {
        
        // Determine velocity
        CoordL3D posA = lnA.a().add(lnA.b(), ta);
        CoordL3D posB = lnB.a().add(lnB.b(), tb);
        CoordL3D velRaw = posB.subtract(posA, 1);
        
        int deltaT = tb - ta;
        
        // Valid velocities are integers
        if((velRaw.x() % deltaT != 0) ||
           (velRaw.y() % deltaT != 0) ||
           (velRaw.z() % deltaT != 0)) {
            return false;
        }
        
        CoordL3D rockVel = new CoordL3D(
            velRaw.x() / deltaT,
            velRaw.y() / deltaT,
            velRaw.z() / deltaT
        );
        
        CoordL3D rockOrigin = posA.add(rockVel, -ta);
        
        //System.out.print(rockOrigin + " @ " + rockVel + " ");
        
        /*
         * rockOrigin[x] + rockVel[x]*t = hailOrigin[x] + hailVel[x]*t
         * rockOrigin[y] + rockVel[y]*t = hailOrigin[y] + hailVel[y]*t
         * rockOrigin[z] + rockVel[z]*t = hailOrigin[z] + hailVel[z]*t
         * 
         * (rockVel[x] - hailVel[x])*t = hailOrigin[x] - rockOrigin[x]
         * (rockVel[y] - hailVel[y])*t = hailOrigin[y] - rockOrigin[y]
         * (rockVel[z] - hailVel[z])*t = hailOrigin[z] - rockOrigin[z]
         */
        
        // Determine if all are hit
        for(Line ln : lines) {
            CoordL3D hailOrigin = ln.a();
            CoordL3D hailVel = ln.b();
            
            long velDiffX = rockVel.x() - hailVel.x();
            
            if(velDiffX == 0) {
                // parallel
                //System.out.println(" failed: parallel in x with " + ln.a());
                return false;
            }
            
            long dx = hailOrigin.x() - rockOrigin.x();
            
            if(dx % velDiffX != 0) {
                return false;
            }
            
            long t = dx / velDiffX;
            
            long rx = rockOrigin.x() + (t * rockVel.x());
            long hx = hailOrigin.x() + (t * hailVel.x());
            
            if(rx != hx) {
                //System.out.println(" failed: x " + rx + " != " + hx + " for " + ln.a() + " at " + t);
                return false;
            }
            
            long ry = rockOrigin.y() + (t * rockVel.y());
            long hy = hailOrigin.y() + (t * hailVel.y());
            
            if(ry != hy) {
                //System.out.println(" failed: y " + ry + " != " + hy + " for " + ln.a() + " at " + t);
                return false;
            }
            
            long rz = rockOrigin.z() + (t * rockVel.z());
            long hz = hailOrigin.z() + (t * hailVel.z());
            
            if(rz != hz) {
                //System.out.println(" failed: z " + rz + " != " + hz + " for " + ln.a() + " at " + t);
                return false;
            }
        }
        
        // If we make it here, yay
        System.out.println("Found solution " + (rockOrigin.x() + rockOrigin.y() + rockOrigin.z()));
        return true;
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
