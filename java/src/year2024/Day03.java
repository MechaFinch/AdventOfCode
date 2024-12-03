package year2024;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import global.util.AdventUtil;

public class Day03 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    private static void part2(List<String> lines) {
        // put in one string
        String s = lines.stream().reduce("", (a, b) -> a + b);
        
        // find instructions
        Pattern p = Pattern.compile("mul\\(\\d+,\\d+\\)|do\\(\\)|don\\'t\\(\\)");
        Matcher m = p.matcher(s);
        
        int sum = 0;
        boolean enabled = true;
        
        // interpret
        while(m.find()) {
            String inst = m.group();
            if(inst.equals("don't()")) {
                enabled = false;
            } else if(inst.equals("do()")) {
                enabled = true;
            } else if(enabled) {
                String[] split = m.group().split(",");
                int a = Integer.parseInt(split[0].substring(4));
                int b = Integer.parseInt(split[1].substring(0, split[1].length() - 1));
                sum += a * b;
            }
        }
        
        System.out.println(sum);
    }
    
    private static void part1(List<String> lines) {
        // put in one string
        String s = lines.stream().reduce("", (a, b) -> a + b);
        
        // find mul(num, num)
        Pattern p = Pattern.compile("mul\\(\\d+,\\d+\\)");
        Matcher m = p.matcher(s);
        
        int sum = 0;
        
        while(m.find()) {
            // interpret mul(num, num)
            String[] inst = m.group().split(",");
            int a = Integer.parseInt(inst[0].substring(4));
            int b = Integer.parseInt(inst[1].substring(0, inst[1].length() - 1));
            sum += a * b;
        }
        
        System.out.println(sum);
    }
}
