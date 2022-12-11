use std::cmp::max;


pub fn main(input: String) {
    let sacks: Vec<&str> = input.split("\n").collect();

    part2(sacks);
}

fn part2(sacks: Vec<&str>) {
    let mut sum = 0;

    for i in (0..sacks.len()).step_by(3) {
        sum += priority(find(sacks[i + 0], sacks[i + 1], sacks[i + 2]));
    }

    println!("{sum}");
}

/// find the char common to all three strings
fn find(a: &str, b: &str, c: &str) -> char {
    println!("{a}");
    println!("{c}");
    println!("{b}");
    println!("");

    let ac: Vec<char> = a.chars().collect();
    let bc: Vec<char> = b.chars().collect();
    let cc: Vec<char> = c.chars().collect();

    let longest = max(ac.len(), max(bc.len(), cc.len()));

    let mut seen_a: Vec<char> = Vec::new();
    let mut seen_b: Vec<char> = Vec::new();
    let mut seen_c: Vec<char> = Vec::new();

    for i in 0..longest {
        if i < ac.len() {
            let ax = ac[i];

            if seen_b.contains(&ax) && seen_c.contains(&ax) {
                return ax;
            }

            seen_a.push(ax);
        }

        if i < bc.len() {
            let bx = bc[i];

            if seen_a.contains(&bx) && seen_c.contains(&bx) {
                return bx;
            }

            seen_b.push(bx);
        }

        if i < cc.len() {
            let cx = cc[i];

            if seen_a.contains(&cx) && seen_b.contains(&cx) {
                return cx;
            }

            seen_c.push(cx);
        }
    }

    return '.';
}

/// char -> priority
fn priority(c: char) -> u32 {
    if c < 'a' {
        return ((c as u32) - ('A' as u32)) + 27;
    } else {
        return (c as u32) - ('a' as u32) + 1;
    }
}

fn part1(sacks: Vec<&str>) {
    let mut sum = 0;

    for sack in sacks {
        let chars: Vec<char> = sack.chars().collect();
        let second_start = chars.len() / 2;

        let mut seen_a: Vec<char> = Vec::new();
        let mut seen_b: Vec<char> = Vec::new();

        for i in 0..second_start {
            let (a, b) = (chars[i], chars[i + second_start]);

            if seen_b.contains(&a) || a == b {
                sum += priority(a);
                break;
            }

            if seen_a.contains(&b) {
                sum += priority(b);
                break;
            }

            seen_a.push(a);
            seen_b.push(b);
        }
    }

    println!("{sum}");
}