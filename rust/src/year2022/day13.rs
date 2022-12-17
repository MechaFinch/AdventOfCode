use std::cmp::Ordering;


#[derive(Debug, Clone, PartialEq, Eq)]
struct List {
    val: i32,
    contents: Vec<List>
}

impl List {
    fn print(&self) {
        if self.val != -1 {
            let v = self.val;
            print!("{v}");
        } else {
            print!("[");

            for i in 0..self.contents.len() {
                self.contents[i].print();

                if i != self.contents.len() - 1 {
                    print!(", ");
                }
            }

            print!("]");
        }
    }
}

impl Ord for List {
    fn cmp(&self, other: &Self) -> Ordering {
        return match compare(self, other) {
            -1  => Ordering::Greater,
            0   => Ordering::Equal,
            1   => Ordering::Less,
            _   => panic!("impossible case")
        };
    }
}

impl PartialOrd for List {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        return Some(self.cmp(other));
    }
}

#[derive(Debug, Clone)]
struct Pair {
    left: List,
    right: List
}

pub fn main(input: String) {
    let mut pairs: Vec<Pair> = Vec::new();

    // convert to pairs of lists
    let lines: Vec<&str> = input.lines().collect();
    let mut i = 0;
    while i < lines.len() {
        let a = parse(lines[i].trim().to_string());
        let b = parse(lines[i + 1].trim().to_string());

        pairs.push(Pair {
            left: a,
            right: b
        });

        i += 3;
    }

    part2(pairs);
}

fn part1(pairs: Vec<Pair>) {
    let mut sum = 0;

    let mut i = 0;
    while i < pairs.len() {
        let l = &pairs[i].left;
        let r = &pairs[i].right;

        if compare(l, r) == 1{
            sum += i + 1;
        }

        i += 1;
    }

    println!("{sum}");
}

fn part2(pairs: Vec<Pair>) {
    // unpack from pairs
    let mut packets: Vec<List> = Vec::new();

    for pair in pairs {
        packets.push(pair.left);
        packets.push(pair.right);
    }

    // dividers
    let div1 = List {
        val: -1,
        contents: vec![
            List {
                val: -1,
                contents: vec![
                    List {
                        val: 2,
                        contents: Vec::new()
                    }
                ]
            }
        ]
    };

    let div2 = List {
        val: -1,
        contents: vec![
            List {
                val: -1,
                contents: vec![
                    List {
                        val: 6,
                        contents: Vec::new()
                    }
                ]
            }
        ]
    };

    packets.push(div1.clone());
    packets.push(div2.clone());

    // sort
    packets.sort();

    /*
    for i in 0..packets.len() {
        packets[i].print();
        println!();
    }
    */

    // find index of packets
    let key = (find(&packets, div1) + 1) * (find(&packets, div2) + 1);

    println!("{key}");
}

/// gets the index of target in packets
fn find(packets: &Vec<List>, target: List) -> i32 {
    for i in 0..packets.len() {
        if packets[i].eq(&target) {
            return i as i32;
        }
    }

    return -1;
}

/// comapres two Lists. Returns 1 if they are in order, -1 if they are not, 0 if inconclusive
fn compare(left: &List, right: &List) -> i32 {
    /*
    left.print();
    println!();
    right.print();
    println!("\n");
    */

    // if both are integers
    if left.val != -1 && right.val != -1 {
        if left.val < right.val {
            return 1;
        } else if left.val > right.val {
            return -1;
        } else {
            return 0;
        }
    }

    // if both are lists
    if left.val == -1 && right.val == -1 {
        let mut i = 0;
        while i < left.contents.len() && i < right.contents.len() {
            let c = compare(&left.contents[i], &right.contents[i]);

            if c != 0 {
                return c;
            }

            i += 1;
        }

        // no decision made, check lengths
        if left.contents.len() < right.contents.len() {
            return 1;
        } else if left.contents.len() > right.contents.len() {
            return -1;
        } else {
            return 0;
        }
    }

    // if one is an integer
    if left.val == -1 { // right is an integer
        return compare(left, &List { val: -1, contents: vec![right.clone()] });
    } else { // left is an integer
        return compare(&List { val: -1, contents: vec![left.clone()] }, right);
    }
}

/// Parses a List from an input line
fn parse(line: String) -> List {
    if line == "[]" {
        return List {
            val: -1,
            contents: Vec::new()
        };
    }

    //println!("{line}");
    let has_commas = line.contains(",");
    let has_brackets = line.contains("[");
    
    // single number
    if !has_commas && !has_brackets {
        return List {
            val: line.parse().unwrap(),
            contents: Vec::new()
        }
    }

    let mut contents: Vec<List> = Vec::new();
    let chars: Vec<char> = line.chars().collect();
    let mut nesting = 0;

    // single list
    if line.starts_with("[") && line.ends_with("]") {
        let mut min_nested_comma = 1000;

        for c in &chars {
            if *c == ',' && nesting < min_nested_comma {
                min_nested_comma = nesting;
            } else if *c == '[' {
                nesting += 1;
            } else if *c == ']' {
                nesting -= 1;
            }
        }

        if min_nested_comma != 0 {
            return parse(line[1..line.len() - 1].trim().to_string() + ",");
        }
    }

    // split into contents
    nesting = 0;
    let mut start = 0;
    let mut end = 0;

    while end < line.len() {
        let c = chars[end];

        // un-nested comma = part done
        if c == ',' && nesting == 0 {
            contents.push(parse(line[start..end].trim().to_string()));
            start = end + 1;
        } else if c == '[' { // opening bracket = + nesting
            nesting += 1;
        } else if c == ']' { // closing bracket = - nesting
            nesting -= 1;
        }

        end += 1;
    }

    //contents.push(parse(line[start..end].trim().to_string()));

    return List {
        val: -1,
        contents: contents
    }
}