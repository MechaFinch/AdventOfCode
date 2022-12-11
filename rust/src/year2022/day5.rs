
pub fn main(input: String) {
    let lines: Vec<&str> = input.lines().collect();

    // I was going to try doing this year in my assembly language, but then I realized I would need
    // to write a string manipulation library, rewrite the dynamic memory allocator, and other
    // nonsense we take for granted here. This sort of string parsing bs is why I decided against it.

    let mut i = 0;

    // find crate numbering so we know how many there are
    while lines[i].contains("[") { i += 1; }
    let start = i + 2;

    let count = lines[i].split(" ").filter(|s| !s.is_empty()).count();

    let mut stacks: Vec<Vec<char>> = Vec::new();

    for _ in 0..count {
        stacks.push(Vec::new());
    }

    // work backwards to make literal stacks
    for i in (0..(start - 1)).rev() {
        let chars: Vec<char> = lines[i].chars().collect();

        for j in 0..count {
            let c = chars[(j * 4) + 1];

            if 'A' <= c && c <= 'Z' {
                stacks[j].push(c);
            }
        }
    }

    // apply operations
    part2(&lines, &mut stacks, start);

    for stack in &stacks {
        for c in stack {
            print!("{c}");
        }

        println!();
    }
    println!();

    for stack in &stacks {
        let c = stack[stack.len() - 1];
        print!("{c}");
    }

    println!();
}

fn part2(lines: &Vec<&str>, stacks: &mut Vec<Vec<char>>, start: usize) {
    let mut crane: Vec<char> = Vec::new();

    for i in start..lines.len() {
        let command: Vec<&str> = lines[i].split(" ").collect();

        let num = command[1].parse::<usize>().unwrap();
        let src = command[3].parse::<usize>().unwrap() - 1;
        let dst = command[5].parse::<usize>().unwrap() - 1;
        
        crane.clear();

        // get
        for _ in 0..num {
            crane.push(stacks[src].pop().unwrap());
        }

        // place
        for _ in 0..num {
            stacks[dst].push(crane.pop().unwrap());
        }
    }
}

fn part1(lines: &Vec<&str>, stacks: &mut Vec<Vec<char>>, start: usize) {
    // apply operations
    for i in start..lines.len() {
        let command: Vec<&str> = lines[i].split(" ").collect();

        let num = command[1].parse::<usize>().unwrap();
        let src = command[3].parse::<usize>().unwrap() - 1;
        let dst = command[5].parse::<usize>().unwrap() - 1;

        for _ in 0..num {
            // fuck you rust
            // there is literally no reason you should not be able to put this as one line
            let fuck_you = stacks[src].pop().unwrap();
            stacks[dst].push(fuck_you);
        }
    }
}