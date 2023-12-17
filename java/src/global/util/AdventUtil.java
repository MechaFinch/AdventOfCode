package global.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Class for general utilities
 * 
 * Were I to re-do this I would make these all Lists and not ArrayLists, but I don't feel like refactoring all of 2020 for that
 * 
 * @author Mechafinch
 */
public class AdventUtil {
	
    public static final String DEFAULT_PATH = "input.txt";
    
	public static File DEFAULT = new File(DEFAULT_PATH);
	
	public static final List<String> DIGIT_WORDS = Arrays.asList(new String[] {
	       "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
	});
	
	public static final Map<String, Character> DIGIT_WORD_MAP = new HashMap<>();
	
	static {
	    DIGIT_WORD_MAP.put("zero", '0');
	    DIGIT_WORD_MAP.put("one", '1');
	    DIGIT_WORD_MAP.put("two", '2');
	    DIGIT_WORD_MAP.put("three", '3');
	    DIGIT_WORD_MAP.put("four", '4');
	    DIGIT_WORD_MAP.put("five", '5');
	    DIGIT_WORD_MAP.put("six", '6');
	    DIGIT_WORD_MAP.put("seven", '7');
	    DIGIT_WORD_MAP.put("eight", '8');
	    DIGIT_WORD_MAP.put("nine", '9');
	}
	
	/**
	 * Reads the input file as an arraylist of strings
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<String> inputList(File f) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(f));
		ArrayList<String> s = (ArrayList<String>) br.lines().collect(Collectors.toList());
		br.close();
		return s;
	}
	
	/**
	 * Reads the input file as an arraylist of ints, one per line
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<Integer> intList(File f) throws IOException {
		return (ArrayList<Integer>) intStream(f).boxed().collect(Collectors.toList());
	}
	
	/**
	 * Reads the input file as an araraylist of ints, one per line, with a custom mapping function
	 * 
	 * @param f
	 * @param mapper
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<Integer> intList(File f, ToIntFunction<? super String> mapper) throws IOException {
		return (ArrayList<Integer>) intStream(f, mapper).boxed().collect(Collectors.toList());
	}
	
	/**
	 * Reads the input file as a list of lists of ints; each sub-list being one line
	 * 
	 * @param f
	 * @param sep separator regex expression
	 * @return
	 * @throws IOException
	 */
	public static List<List<Integer>> intListList(File f, String sep) throws IOException {
	    List<List<Integer>> listList = new ArrayList<>();
	    
	    for(String ln : inputLines(f)) {
	        listList.add(toIntList(ln, sep));
	    }
	    
	    return listList;
	}
	
	/**
	 * Reads the input file as a list of lists of ints with a custom mapping function; each sub-list being one line
	 * 
	 * @param f
	 * @param sep separator regex expression
	 * @param mapper
	 * @return
	 * @throws IOException
	 */
	public static List<List<Integer>> intListList(File f, String sep, ToIntFunction<? super String> mapper) throws IOException {
	    List<List<Integer>> listList = new ArrayList<>();
        
        for(String ln : inputLines(f)) {
            listList.add(toIntList(ln, sep, mapper));
        }
        
        return listList;
    }
	
	/**
	 * Parses a string into a list of ints, separated by the given regex
	 * 
	 * @param s
	 * @param sep
	 * @return
	 */
	public static List<Integer> toIntList(String s, String sep) {
	    return Arrays.stream(s.split(sep))
                     .mapToInt(Integer::parseInt)
                     .boxed()
                     .toList();
	}
	
	/**
     * Parses a string into a list of ints, separated by the given regex, using a custom mapping function
     * 
     * @param s
     * @param sep
     * @return
     */
    public static List<Integer> toIntList(String s, String sep, ToIntFunction<? super String> mapper) {
        return Arrays.stream(s.split(sep))
                     .mapToInt(mapper)
                     .boxed()
                     .toList();
    }
	
	/**
	 * Reads the input file as an arraylist of longs, one per line
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<Long> longList(File f) throws IOException {
		return (ArrayList<Long>) longStream(f).boxed().collect(Collectors.toList());
	}
	
	/**
	 * Reads the input file as an arraylist of longs, one per line, with a custom mapping function
	 * 
	 * @param f
	 * @param mapper
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<Long> longList(File f, ToLongFunction<? super String> mapper) throws IOException {
		return (ArrayList<Long>) longStream(f, mapper).boxed().collect(Collectors.toList());
	}
	
	/**
     * Reads the input file as a list of lists of longs; each sub-list being one line
     * 
     * @param f
     * @param sep separator regex expression
     * @return
     * @throws IOException
     */
    public static List<List<Long>> longListList(File f, String sep) throws IOException {
        List<List<Long>> listList = new ArrayList<>();
        
        for(String ln : inputLines(f)) {
            listList.add(toLongList(ln, sep));
        }
        
        return listList;
    }
    
    /**
     * Reads the input file as a list of lists of longs with a custom mapping function; each sub-list being one line
     * 
     * @param f
     * @param sep separator regex expression
     * @param mapper
     * @return
     * @throws IOException
     */
    public static List<List<Long>> longListList(File f, String sep, ToLongFunction<? super String> mapper) throws IOException {
        List<List<Long>> listList = new ArrayList<>();
        
        for(String ln : inputLines(f)) {
            listList.add(toLongList(ln, sep, mapper));
        }
        
        return listList;
    }
    
    /**
     * Parses a string into a list of ints, separated by the given regex
     * 
     * @param s
     * @param sep
     * @return
     */
    public static List<Long> toLongList(String s, String sep) {
        return Arrays.stream(s.split(sep))
                     .mapToLong(Long::parseLong)
                     .boxed()
                     .toList();
    }
    
    /**
     * Parses a string into a list of ints, separated by the given regex, using a custom mapping function
     * 
     * @param s
     * @param sep
     * @return
     */
    public static List<Long> toLongList(String s, String sep, ToLongFunction<? super String> mapper) {
        return Arrays.stream(s.split(sep))
                     .mapToLong(mapper)
                     .boxed()
                     .toList();
    }
	
	/**
	 * Reads the input file as a stream of strings
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static Stream<String> stringStream(File f) throws IOException {
		return inputList(f).stream();
	}
	
	/**
	 * Reads the input file as a stream of ints, one per line
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static IntStream intStream(File f) throws IOException {
		return stringStream(f).mapToInt(s -> Integer.parseInt(s));
	}
	
	/**
	 * Reads the input file as a stream of ints, one per line, with a custom mapping function
	 * 
	 * @param f
	 * @param mapper
	 * @return
	 * @throws IOException
	 */
	public static IntStream intStream(File f, ToIntFunction<? super String> mapper) throws IOException {
		return stringStream(f).mapToInt(mapper);
	}
	
	/**
	 * Reads the input file as a stream of longs, one per line
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static LongStream longStream(File f) throws IOException {
		return stringStream(f).mapToLong(s -> Long.parseLong(s));
	}
	
	/**
	 * Reads the input file as a stream of longs, one per line, with a custom mapping function
	 * 
	 * @param f
	 * @param mapper
	 * @return
	 * @throws IOException
	 */
	public static LongStream longStream(File f, ToLongFunction<? super String> mapper) throws IOException {
		return stringStream(f).mapToLong(mapper);
	}
	
	/**
	 * Reads the input file as an array of strings
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static String[] inputLines(File f) throws IOException {
		return inputList(f).toArray(new String[0]);
	}
	
	/**
	 * Reads the input file as an array of ints, one per line
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static int[] inputInts(File f) throws IOException {
		return intStream(f).toArray();
	}
	
	/**
	 * Reads the input file as an array of ints, one per line, with a custom mapping function
	 * 
	 * @param f
	 * @param mapper
	 * @return
	 * @throws IOEXception
	 */
	public static int[] inputInts(File f, ToIntFunction<? super String> mapper) throws IOException {
		return intStream(f, mapper).toArray();
	}
	
	/**
	 * Reads the input file as an array of longs, one per line
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static long[] inputLongs(File f) throws IOException {
		return longStream(f).toArray();
	}
	
	/**
	 * Reads the input file as an array of longs, one per line, with a custom mapping function
	 * 
	 * @param f
	 * @param mapper
	 * @return
	 * @throws IOException
	 */
	public static long[] inputLongs(File f, ToLongFunction<? super String> mapper) throws IOException {
		return longStream(f, mapper).toArray();
	}
}
