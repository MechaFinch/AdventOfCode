
pub fn main(input: String) {
    let res = part1(input, 14);

    println!("{res}");
}

fn part1(input: String, bsize: usize) -> usize {
    let mut buffer: Vec<char> = Vec::new();
    let chars: Vec<char> = input.chars().collect();

    // first set
    for i in 0..bsize {
        buffer.push(chars[i]);
    }

    // the rest
    for i in (bsize - 1)..chars.len() {
        buffer[bsize - 1] = chars[i];

        if all_different(&buffer) {
            return i + 1;
        }

        rotate_left(&mut buffer);
    }

    return 0;
}

/// Rotates the contents of the buffer left
fn rotate_left(buffer: &mut Vec<char>) {
    for i in 0..(buffer.len() - 1) {
        buffer[i] = buffer[i + 1];
    }
}

/// Returns true if every element is different
fn all_different(buffer: &Vec<char>) -> bool {
    let mut seen: Vec<char> = Vec::new();

    for i in 0..buffer.len() {
        let c = buffer[i];

        if seen.contains(&c) { return false; }

        seen.push(c);
    }

    return true;
}