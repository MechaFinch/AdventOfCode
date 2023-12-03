package year2020;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import global.util.AdventUtil;

public class Day20 {
	
	public static void main(String[] args) throws IOException {
		System.out.println(part2(AdventUtil.inputLines(AdventUtil.DEFAULT)));
	}
	
	/**
	 * Part 2
	 * 
	 * @param input
	 * @return
	 */
	public static int part2(String[] input) {
		Map<Integer, List<Integer>> sideMap = generateSideMap(input);
		Map<Integer, List<String>> imageMap = generateImageMap(input);
		
		// Setup the image
		int size = (int) Math.sqrt(sideMap.keySet().size());
		int[][] image = new int[size][size];
		
		image[0][0] = sideMap.keySet().toArray(new Integer[0])[0];
		image = findImage(image, sideMap, imageMap);
		
		// Construct the full image
		List<String> fullImage = new ArrayList<>();
		
		for(int iy = 0; iy < size; iy++) { // image y vs pixel y
			for(int py = 0; py < 8; py++) {
				String line = "";
				
				for(int x = 0; x < size; x++) {
					line += imageMap.get(image[x][iy]).get(py);
				}
				
				fullImage.add(line);
			}
		}
		
		String[] monster = new String[] {
			"                  # ",
			"#    ##    ##    ###",
			" #  #  #  #  #  #   "
		};
		
		// Count # in the image
		int roughness = 0;
		for(int i = 0; i < fullImage.size(); i++) {
			String s = fullImage.get(i);
			s = s.replaceAll("\\.", "");
			
			roughness += s.length();
		}
		
		// Permute rotation & flipping
		for(int f = 0; f < 2; f++) {
			for(int d = 0; d < 4; d++) {
				// Find and count sea monsters
				int numMonsters = 0;
				
				for(int y = 0; y < fullImage.size() - 2; y++) {
					outer:
					for(int x = 0; x < fullImage.get(y).length() - 19; x++) {
						// Identify
						for(int i = 0; i < 3; i++) {
							for(int j = 0; j < 20; j++) {
								char m = monster[i].charAt(j),
									 p = fullImage.get(y + i).charAt(x + j);
								
								if(m == '#' && p != '#') continue outer;
							}
						}
						
						// Found one
						numMonsters++;
					}
				}
				
				if(numMonsters > 0) {
					prettyPrintImage(fullImage);
					return roughness - (numMonsters * 15);
				}
				
				// Rotate for the next iteration
				rotate(fullImage, 1);
			}
			
			// Flip for the next iteration
			Collections.reverse(fullImage);
		}
		
		return 0;
	}
	
	/**
	 * Part 1
	 * 
	 * @param input
	 * @return
	 */
	public static long part1(String[] input) {
		Map<Integer, List<Integer>> sideMap = generateSideMap(input);
		Map<Integer, List<String>> imageMap = generateImageMap(input);
		
		// Construct the image
		int size = (int) Math.sqrt(sideMap.keySet().size());
		int[][] image = new int[size][size];
		
		image[0][0] = sideMap.keySet().toArray(new Integer[0])[0];
		image = findImage(image, sideMap, imageMap);
		
		// Print solution
		System.out.println(Arrays.deepToString(image));
		System.out.println();
		prettyPrintSides(image, sideMap);
		
		long product = image[0][0];
		product *= image[0][size - 1];
		product *= image[size - 1][0];
		product *= image[size - 1][size - 1];
		
		return product;
	}
	
	/**
	 * Pretty prints an image
	 * 
	 * @param image
	 */
	public static void prettyPrintImage(List<String> image) {
		for(String s : image) {
			System.out.println(s);
		}
	}
	
	/**
	 * Pretty prints the sides
	 * 
	 * @param image
	 * @param sideMap
	 */
	public static void prettyPrintSides(int[][] image, Map<Integer, List<Integer>> sideMap) {
		int size = image.length;
		
		for(int y = 0; y < size; y++) {
			for(int i = 0; i < 10; i++) {
				for(int x = 0; x < size; x++) {
					if(image[x][y] == 0) {
						System.out.print("           ");
					} else {
						List<Integer> sides = sideMap.get(image[x][y]);
						
						if(i == 0) {
							System.out.print(" " + filledBinary(sides.get(0)).replaceAll("1", "#").replaceAll("0", "."));
						} else if(i == 9) {
							System.out.print(" " + filledBinary(reverseInt(sides.get(2))).replaceAll("1", "#").replaceAll("0", "."));
						} else {
							System.out.print(" " + (((sides.get(3) >> i) & 1) == 1 ? "#" : "."));
							System.out.print("        " + (((sides.get(1) >> (9 - i)) & 1) == 1 ? "#" : "."));
						}
					}
				}
				
				System.out.println();
			}
			
			System.out.println();
		}
	}
	
	/**
	 * Returns the filled 10 bit binary string of i
	 * 
	 * @param i
	 * @return
	 */
	public static String filledBinary(int i) {
		return String.format("%10s", Integer.toBinaryString(i)).replaceAll(" ", "0");
	}
	
	/**
	 * Recursively finds the image based on possible adjacent images
	 * Returns null if it couldn't fill the image
	 * 
	 * @param currentImage
	 * @param sideMap
	 * @return
	 */
	public static int[][] findImage(int[][] currentImage, Map<Integer, List<Integer>> sideMap, Map<Integer, List<String>> imageMap) {
		// System.out.println("\n         " + Arrays.deepToString(currentImage));
		// prettyPrintImage(currentImage, sideMap);
		
		int size = currentImage.length;
		
		// Find sides we need to match
		boolean filled = true;
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				// For occupied places, find unoccupied adjacents and try to fill it in
				if(currentImage[x][y] != 0) {
					// Check adjacents
					int[][] newImage = null;
					boolean empty = true;
					
					if(x != 0 && currentImage[x - 1][y] == 0) { // Left
						// System.out.println("Finding left");
						
						newImage = findImageSub(currentImage, sideMap, imageMap, x, y, 3);
						if(newImage != null) return newImage;
					} else { // Shift image right to fit left if possible
						// Check right side
						for(int i = 0; i < size; i++) {
							if(currentImage[size - 1][i] != 0) {
								empty = false;
								break;
							}
						}
						
						if(empty) {
							// Shift the image over
							newImage = copy2d(currentImage);
							
							for(int i = size - 1; i > 0; i--) {
								for(int j = 0; j < size; j++) {
									newImage[i][j] = newImage[i - 1][j];
								}
							}
							
							// Clear left side
							for(int i = 0; i < size; i++) {
								newImage[0][i] = 0;
							}
							
							// System.out.println("Finding left, shifted");
							
							// Try
							newImage = findImageSub(newImage, sideMap, imageMap, x + 1, y, 3);
							if(newImage != null) return newImage;
						}
					}
					
					if(x != size - 1 && currentImage[x + 1][y] == 0) { // Right
						// System.out.println("Finding right");
						
						newImage = findImageSub(currentImage, sideMap, imageMap, x, y, 1);
						if(newImage != null) return newImage;
					}
					
					if(y != 0 && currentImage[x][y - 1] == 0) { // Up
						// System.out.println("Finding up");
						
						newImage = findImageSub(currentImage, sideMap, imageMap, x, y, 0);
						if(newImage != null) return newImage;
					} else { // Shift image down if possible
						// Check bottom
						empty = true;
						for(int i = 0; i < size; i++) {
							if(currentImage[i][size - 1] != 0) {
								empty = false;
								break;
							}
						}
						
						if(empty) {
							// Shift image
							newImage = copy2d(currentImage);
							
							for(int i = size - 1; i > 0; i--) {
								for(int j = 0; j < size; j++) {
									newImage[j][i] = newImage[j][i - 1];
								}
							}
							
							// Clear top
							for(int i = 0; i < size; i++) {
								newImage[i][0] = 0;
							}
							
							// System.out.println("Finding up, shifted");
							
							// Try
							newImage = findImageSub(newImage, sideMap, imageMap, x, y + 1, 0);
							if(newImage != null) return newImage;
						}
					}
					
					if(y != size - 1 && currentImage[x][y + 1] == 0) { // Down
						// System.out.println("Finding down");
						
						newImage = findImageSub(currentImage, sideMap, imageMap, x, y, 2);
						if(newImage != null) return newImage;
					}
				} else filled = false;
			}
		}
		
		if(filled) {
			// System.out.println("Image filled");
			return currentImage;
		}
		
		// System.out.println("Main failed");
		return null;
	}
	
	/**
	 * Tries to fill the image based on the current side
	 * 
	 * @param currentImage
	 * @param sideMap
	 * @param used
	 * @param x1
	 * @param y1
	 * @param direction
	 * @return
	 */
	public static int[][] findImageSub(int[][] currentImage, Map<Integer, List<Integer>> sideMap, Map<Integer, List<String>> imageMap, int x1, int y1, int direction) {
		// System.out.println("(" + x1 + ", " + y1 + ")   " + Arrays.deepToString(currentImage));
		
		int side = sideMap.get(currentImage[x1][y1]).get(direction);
		
		// Find which have been used
		List<Integer> used = new ArrayList<>();
		for(int[] col : currentImage) {
			for(int id : col) {
				if(id != 0) used.add(id);
			}
		}
		
		// Search the map for possible sides
		for(int id : sideMap.keySet()) {
			if(used.contains(id)) continue;
			
			// Find a side that matches
			List<Integer> sides = sideMap.get(id);
			List<String> imageBody = imageMap.get(id);
			for(int i = 0; i < sides.size(); i++) {
				int n = sides.get(i),
					r = reverseInt(n);
				
				// Check normal and reverse
				if(side == n || side == r) {
					// System.out.println(filledBinary(side));
					// System.out.println(filledBinary(n));
					
					if(side == r) { // We only have to rotate
						// Rotate to match
						Collections.rotate(sides, 2 - (i - direction));
						rotate(imageBody, 2 - (i - direction));
					} else { // We have to flip
						flip(sides);
						Collections.reverse(imageBody);
						
						Collections.rotate(sides, i + direction);
						rotate(imageBody, i + direction);
					}
					
					int[][] imageCopy = currentImage.clone();
					
					// Put in image
					switch(direction) {
						case 0: // Up
							imageCopy[x1][y1 - 1] = id;
							break;
							
						case 1: // Right
							imageCopy[x1 + 1][y1] = id;
							break;
							
						case 2: // Down
							imageCopy[x1][y1 + 1] = id;
							break;
							
						case 3: // Left
							imageCopy[x1 - 1][y1] = id;
							break;
					}
					
					// System.out.println("Recursing");
					
					// Find the rest of the image based on this one
					imageCopy = findImage(imageCopy, sideMap, imageMap);
					if(imageCopy != null) return imageCopy;
				}
			}
		}
		
		// System.out.println("Sub failed");
		return null;
	}
	
	/**
	 * Returns a copy of the source
	 * 
	 * @param source
	 * @return
	 */
	public static int[][] copy2d(int[][] source) {
		int[][] dest = new int[source.length][source[0].length];
		
		for(int i = 0; i < source.length; i++) {
			dest[i] = source[i].clone();
		}
		
		return dest;
	}
	
	/**
	 * Reverses a 10 bit int
	 * 
	 * @param i
	 * @return
	 */
	public static int reverseInt(int i) {
		return Integer.parseInt(new StringBuilder(filledBinary(i)).reverse().toString(), 2);
	}
	
	/**
	 * Rotates an image body
	 * 
	 * @param image
	 * @param distance
	 */
	public static void rotate(List<String> image, int distance) {
		distance = Math.abs(distance % 4); // Correct range
		
		if(distance == 0) return;
		
		// List to build into
		List<String> newImage = new ArrayList<>();
		int size = image.size();
		
		if(distance == 2) { // Reverse string direction and add in reverse order
			for(int i = image.size() - 1; i >= 0; i--) {
				newImage.add(new StringBuilder(image.get(i)).reverse().toString());
			}
		} else {
			// By line
			for(int i = 0; i < size; i++) {
				String s = "";
				
				// By character
				for(int j = 0; j < size; j++) {
					if(distance == 1) { // Right
						s += image.get(size - j - 1).charAt(i);
					} else { // Left
						s += image.get(j).charAt(size - i - 1);
					}
				}
				
				newImage.add(s);
			}
		}
		
		image.clear();
		Collections.addAll(image, newImage.toArray(new String[0]));
	}
	
	/**
	 * We can use Collections.rotate but flip needs its own stuff
	 * 
	 * @param list
	 */
	public static void flip(List<Integer> list) {
		// We need to flip all values
		for(int i = 0; i < 4; i++) {
			// Convert to 10 bit binary string, reverse, convert back to int
			list.set(i, reverseInt(list.get(i)));
		}
		
		// Put things where they need to go
		Collections.swap(list, 0, 2);
	}
	
	/**
	 * Generates a map from ID to each side, listed clockwise from the top
	 * Sides are represented in binary with the left as the MSB when at the top
	 * 
	 * @param input
	 * @return
	 */
	public static Map<Integer, List<Integer>> generateSideMap(String[] input) {
		Map<Integer, List<Integer>> map = new HashMap<>();
		
		// Images are 10x10 with blank and ID lines
		for(int i = 0; i < input.length; i += 12) {
			// Isolate ID
			int id = Integer.parseInt(input[i].substring(input[i].indexOf(' ') + 1, input[i].length() - 1));
			
			// Build sides
			// top/bottom convert # and . to 1 and 0, bottom reverses the string
			int top = Integer.parseInt(input[i + 1].replaceAll("#", "1").replaceAll("\\.", "0"), 2),
				bottom = Integer.parseInt(new StringBuilder(input[i + 10].replaceAll("#", "1").replaceAll("\\.", "0")).reverse().toString(), 2),
				left = 0,
				right = 0;
			
			// Build left & right
			for(int j = 1; j < 11; j++) {
				left |= (input[i + j].charAt(0) == '#' ? 1 : 0) << (j - 1);
				right |= (input[i + j].charAt(9) == '#' ? 1 : 0) << (10 - j);
			}
			
			// Make list and put in map
			map.put(id, Arrays.stream(new int[] {top, right, bottom, left}).boxed().collect(Collectors.toList()));
		}
		
		return map;
	}
	
	/**
	 * Generates a map from ID to interior image, line by line top to bottom
	 * 
	 * @param input
	 * @return
	 */
	public static Map<Integer, List<String>> generateImageMap(String[] input) {
		Map<Integer, List<String>> map = new HashMap<>();
		
		// Images are 9x9 + sides and blank & ID lines
		for(int i = 0; i < input.length; i += 12) {
			// ID line
			int id = Integer.parseInt(input[i].substring(input[i].indexOf(' ') + 1, input[i].length() - 1));
			List<String> list = new ArrayList<>();
			
			// Go by line, removing sides
			for(int j = 2; j < 10; j++) {
				String l = input[i + j];
				list.add(l.substring(1, l.length() - 1));
			}
			
			map.put(id, list);
		}
		
		return map;
	}
}
