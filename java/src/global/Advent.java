package global;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import global.util.AdventUtil;

/**
 * hololive advent (real)
 */
public class Advent {
    public static void main(String[] args) throws IOException {
        Path p = Paths.get(AdventUtil.DEFAULT_PATH);
        
        long startTime = System.nanoTime();
        
        year2023.Day12.run(p.toFile());
        
        long endTime = System.nanoTime();
        long timeMillis = (endTime - startTime) / 1_000_000;
        
        System.out.printf("%nElapsed Time: %d ms", timeMillis);
    }
}
