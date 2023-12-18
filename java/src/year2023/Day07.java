package year2023;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import global.util.AdventUtil;

/**
 * Day 7
 * Camel Cards
 */
public class Day07 {
    public static void run(File f) throws IOException {
        part2(AdventUtil.inputList(f));
    }
    
    private enum HandType {
        FIVE_KIND(6),
        FOUR_KIND(5),
        FULL_HOUSE(4),
        THREE_KIND(3),
        TWO_PAIR(2),
        ONE_PAIR(1),
        HIGH_CARD(0);
        
        public final int value;
        
        private HandType(int value) {
            this.value = value;
        }
    }

    private record Hand(String cards, HandType type, int bid) {
        
    }
    
    /**
     * Compute the strength of each hand, now with jokers
     * Sort by strength
     * Multiply rank by bid & sum to accumulate
     * 
     * @param lines
     */
    private static void part2(List<String> lines) {
        List<Hand> hands = new ArrayList<>(lines.size());
        
        // parse hands
        for(String ln : lines) {
            // determine type
            Map<Character, Integer> characterCounts = new HashMap<>();
            
            // count
            int jc = 0;
            for(int i = 0; i < 5; i++) {
                char c = ln.charAt(i);
                
                // count jokers separately
                if(c == 'J') jc++;
                else {
                    int count = characterCounts.getOrDefault(c, 0) + 1;
                    characterCounts.put(c, count);
                }
            }
            
            // sort
            List<Character> labels = new ArrayList<>(characterCounts.keySet());
            Collections.sort(labels, (a, b) -> {
                return characterCounts.get(b) - characterCounts.get(a);
            });
            
            // determine
            HandType type;
            
            if(labels.size() == 0) {
                // all jokers
                type = HandType.FIVE_KIND;
            } else if(characterCounts.get(labels.get(0)) == 5) {
                type = HandType.FIVE_KIND;
            } else if(characterCounts.get(labels.get(0)) == 4) {
                if(jc != 0) {
                    type = HandType.FIVE_KIND;
                } else {
                    type = HandType.FOUR_KIND;
                }
            } else if(characterCounts.get(labels.get(0)) == 3) {
                if(jc == 2) {
                    type = HandType.FIVE_KIND;
                } else if(jc == 1) {
                    type = HandType.FOUR_KIND;
                } else if(characterCounts.get(labels.get(1)) == 2) {
                    type = HandType.FULL_HOUSE;
                } else {
                    type = HandType.THREE_KIND;
                }
            } else if(characterCounts.get(labels.get(0)) == 2) {
                if(jc == 3) {
                    type = HandType.FIVE_KIND;
                } else if(jc == 2) {
                    type = HandType.FOUR_KIND;
                } else if(jc == 1) {
                    if(characterCounts.get(labels.get(1)) == 2) {
                        type = HandType.FULL_HOUSE;
                    } else {
                        type = HandType.THREE_KIND;
                    }
                } else if(characterCounts.get(labels.get(1)) == 2) {
                    type = HandType.TWO_PAIR;
                } else {
                    type = HandType.ONE_PAIR;
                }
            } else if(jc == 4) {
                type = HandType.FIVE_KIND;
            } else if(jc == 3) {
                type = HandType.FOUR_KIND;
            } else if(jc == 2) {
                type = HandType.THREE_KIND;
            } else if(jc == 1) {
                type = HandType.ONE_PAIR;
            } else {
                type = HandType.HIGH_CARD;
            }
            
            // convert cards such that ascii values follow strength
            String cards = "";
            
            for(int i = 0; i < 5; i++) {
                char c = ln.charAt(i);
                
                if(c > '9') {
                    cards += switch(c) {
                        case 'J' -> '1';
                        case 'T' -> '9' + 1;
                        case 'Q' -> '9' + 3;
                        case 'K' -> '9' + 4;
                        case 'A' -> '9' + 5;
                        default  -> '0';
                    };
                } else {
                    cards += c;
                }
            }
            
            // bid & add
            int bid = Integer.parseInt(ln.substring(6));
            hands.add(new Hand(cards, type, bid));
        }
        
        // sort by strength
        Collections.sort(hands, (a, b) -> {
            if(a.type() == b.type()) {
                for(int i = 0; i < 5; i++) {
                    char ac = a.cards().charAt(i),
                         bc = b.cards().charAt(i);
                    
                    if(ac != bc) {
                        return ac - bc;
                    }
                }
                
                return 0;
            } else {
                return a.type().value - b.type().value;
            }
        });
        
        // compute result
        int sum = 0;
        
        for(int i = 0; i < hands.size(); i++) {
            sum += hands.get(i).bid() * (i + 1);
        }
        
        System.out.println(sum);
    }
    
    /**
     * Compute the strength of each hand
     * Sort by strength
     * Multiply rank by bid & sum to accumulate
     * 
     * @param lines
     */
    private static void part1(List<String> lines) {
        List<Hand> hands = new ArrayList<>(lines.size());
        
        // parse hands
        for(String ln : lines) {
            // determine type
            Map<Character, Integer> characterCounts = new HashMap<>();
            
            // count
            for(int i = 0; i < 5; i++) {
                char c = ln.charAt(i);
                int count = characterCounts.getOrDefault(c, 0) + 1;
                characterCounts.put(c, count);
            }
            
            // sort
            List<Character> labels = new ArrayList<>(characterCounts.keySet());
            Collections.sort(labels, (a, b) -> {
                return characterCounts.get(b) - characterCounts.get(a);
            });
            
            // determine
            HandType type;
            
            if(characterCounts.get(labels.get(0)) == 5) {
                type = HandType.FIVE_KIND;
            } else if(characterCounts.get(labels.get(0)) == 4) {
                type = HandType.FOUR_KIND;
            } else if(characterCounts.get(labels.get(0)) == 3) {
                if(characterCounts.get(labels.get(1)) == 2) {
                    type = HandType.FULL_HOUSE;
                } else {
                    type = HandType.THREE_KIND;
                }
            } else if(characterCounts.get(labels.get(0)) == 2) {
                if(characterCounts.get(labels.get(1)) == 2) {
                    type = HandType.TWO_PAIR;
                } else {
                    type = HandType.ONE_PAIR;
                }
            } else {
                type = HandType.HIGH_CARD;
            }
            
            // convert cards such that ascii values follow strength
            String cards = "";
            
            for(int i = 0; i < 5; i++) {
                char c = ln.charAt(i);
                
                if(c > '9') {
                    cards += switch(c) {
                        case 'T' -> '9' + 1;
                        case 'J' -> '9' + 2;
                        case 'Q' -> '9' + 3;
                        case 'K' -> '9' + 4;
                        case 'A' -> '9' + 5;
                        default  -> '0';
                    };
                } else {
                    cards += c;
                }
            }
            
            // bid & add
            int bid = Integer.parseInt(ln.substring(6));
            hands.add(new Hand(cards, type, bid));
        }
        
        // sort by strength
        Collections.sort(hands, (a, b) -> {
            if(a.type() == b.type()) {
                for(int i = 0; i < 5; i++) {
                    char ac = a.cards().charAt(i),
                         bc = b.cards().charAt(i);
                    
                    if(ac != bc) {
                        return ac - bc;
                    }
                }
                
                return 0;
            } else {
                return a.type().value - b.type().value;
            }
        });
        
        // compute result
        int sum = 0;
        
        for(int i = 0; i < hands.size(); i++) {
            sum += hands.get(i).bid() * (i + 1);
        }
        
        System.out.println(sum);
    }
}

