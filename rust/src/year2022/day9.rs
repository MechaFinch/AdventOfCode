
#[derive(Debug, Copy, Clone, PartialEq)]
struct Pos {
    x: i32,
    y: i32
}

pub fn main(input: String) {
    part2(input);
}

fn part1(input: String) {
    simulate(input, 1);
}

fn part2(input: String) {
    simulate(input, 9);
}

/// Simulate a rope according to the input. tsize = number of tail nodes
fn simulate(input: String, tsize: usize) {
    let mut hpos = Pos { x: 0, y: 0 };
    let mut tpos = Pos { x: 0, y: 0 };

    let mut tail: Vec<Pos> = Vec::new();

    for _ in 0..tsize {
        tail.push(tpos);
    }

    let mut thist: Vec<Pos> = Vec::new();
    let mut count = 0;

    for ln in input.lines() {
        let split: Vec<&str> = ln.split(" ").collect();

        let dir: char = split[0].chars().collect::<Vec<char>>()[0];
        let dist = split[1].parse::<i32>().unwrap();

        for _ in 0..dist {
            //tpos = tail[0];
            //println!("{hpos:?} {tpos:?}");

            // update head & first tail
            hpos = update_head(hpos, dir);
            tpos = update_tail(hpos, tail[0]);
            tail[0] = tpos;
            
            // update the rest of the tail
            for i in 1..tsize {
                tpos = update_tail(tpos, tail[i]);
                tail[i] = tpos;
            }
            
            // track last tail pos
            if !thist.contains(&tpos) {
                thist.push(tpos);
                count += 1;
            }
        }
    }

    println!("{count}");
}

/// Returns the new head position
fn update_head(hpos: Pos, dir: char) -> Pos {
    return match dir {
        'U' => Pos { x: hpos.x, y: hpos.y + 1 },
        'D' => Pos { x: hpos.x, y: hpos.y - 1 },
        'L' => Pos { x: hpos.x - 1, y: hpos.y },
        'R' => Pos { x: hpos.x + 1, y: hpos.y },
        _   => hpos
    }
}

/// Returns the new tail position given the new head position and old tail position
fn update_tail(hpos: Pos, tpos: Pos) -> Pos {
    let dx = hpos.x - tpos.x;
    let dy = hpos.y - tpos.y;

    // no change
    if dx.abs() <= 1 && dy.abs() <= 1 {
        return tpos;
    }

    // change
    let mut ndx = 0;
    let mut ndy = 0;

    if dx > 0 {
        ndx = 1;
    } else if dx < 0 {
        ndx = -1;
    }

    if dy > 0 {
        ndy = 1;
    } else if dy < 0 {
        ndy = -1;
    }

    return Pos { x: tpos.x + ndx, y: tpos.y + ndy };
}