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
	 * Reads the input file as an arraylist of ints
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<Integer> intList(File f) throws IOException {
		return (ArrayList<Integer>) intStream(f).boxed().collect(Collectors.toList());
	}
	
	/**
	 * Reads the input file as an araraylist of ints, with a custom mapping function
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
	 * Reads the input file as an arraylist of longs
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<Long> longList(File f) throws IOException {
		return (ArrayList<Long>) longStream(f).boxed().collect(Collectors.toList());
	}
	
	/**
	 * Reads the input file as an arraylist of longs, with a custom mapping function
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
	 * Reads the input file as a stream of ints
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static IntStream intStream(File f) throws IOException {
		return stringStream(f).mapToInt(s -> Integer.parseInt(s));
	}
	
	/**
	 * Reads the input file as a stream of ints, with a custom mapping function
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
	 * Reads the input file as a stream of longs
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static LongStream longStream(File f) throws IOException {
		return stringStream(f).mapToLong(s -> Long.parseLong(s));
	}
	
	/**
	 * Reads the input file as a stream of longs, with a custom mapping function
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
	 * Reads the input file as an array of ints
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static int[] inputInts(File f) throws IOException {
		return intStream(f).toArray();
	}
	
	/**
	 * Reads the input file as an array of ints, with a custom mapping function
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
	 * Reads the input file as an array of longs
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static long[] inputLongs(File f) throws IOException {
		return longStream(f).toArray();
	}
	
	/**
	 * Reads the input file as an array of longs, with a custom mapping function
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
