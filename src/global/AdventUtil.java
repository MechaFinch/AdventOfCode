package global;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Class for general utilities
 * 
 * @author Mechafinch
 */
public class AdventUtil {
	
	public static File DEFAULT = new File("input.txt");
	
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
}
