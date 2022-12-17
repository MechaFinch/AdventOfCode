

use std::collections::HashSet;

use crate::util::Pos;

pub fn main(input: String) {
    let mut filled: HashSet<Pos> = HashSet::new();
    let mut highest_y = 0;
    let mut min_x = 100_000;
    let mut max_x = -100_000;

    for ln in input.lines() {
        let coords: Vec<&str> = ln.split(" -> ").collect();

        let mut start = parse_coord(coords[0]);

        if start.y > highest_y {
            highest_y = start.y;
        }

        if start.x > max_x {
            max_x = start.x;
        }

        if start.x < min_x {
            min_x = start.x;
        }

        for i in 1..coords.len() {
            let end = parse_coord(coords[i]);

            if end.y > highest_y {
                highest_y = end.y;
            }

            if end.x > max_x {
                max_x = end.x;
            }

            if end.x < min_x {
                min_x = end.x;
            }

            // vertical or horizontal
            if start.x == end.x { // vertical
                let x = start.x;
                let sy;
                let ey;

                // go least to greatest
                if start.y < end.y {
                    sy = start.y;
                    ey = end.y;
                } else {
                    sy = end.y;
                    ey = start.y;
                }

                for y in sy..=ey {
                    let p = Pos { x: x, y: y };
                    filled.insert(p);
                }
            } else { // horizontal
                let y = start.y;
                let sx;
                let ex;

                // go least to greatest
                if start.x < end.x {
                    sx = start.x;
                    ex = end.x;
                } else {
                    sx = end.x;
                    ex = start.x;
                }

                for x in sx..=ex {
                    let p = Pos { x: x, y: y };
                    filled.insert(p);
                }
            }

            start = end;
        }
    }

    for y in 0..=highest_y {
        for x in min_x..=max_x {
            let p = Pos { x: x, y: y };

            if filled.contains(&p) {
                print!("#");
            } else {
                print!(".");
            }
        }

        println!();
    }
    println!("\n");

    part2(&mut filled, highest_y);

    for y in 0..=highest_y {
        for x in min_x..=max_x {
            let p = Pos { x: x, y: y };

            if filled.contains(&p) {
                print!("#");
            } else {
                print!(".");
            }
        }

        println!();
    }
}

fn part2(filled: &mut HashSet<Pos>, highest: i32) {
    let mut count = 0;

    // until something goes into the void
    'out:
    loop {
        let mut sp = Pos {
            x: 500,
            y: 0
        };

        // simulate
        'at_rest:
        loop {
            // did we hit the floor
            if sp.y > highest {
                filled.insert(sp);
                break 'at_rest;
            }

            let down = Pos {
                x: sp.x,
                y: sp.y + 1
            };

            let left = Pos {
                x: sp.x - 1,
                y: sp.y + 1
            };

            let right = Pos {
                x: sp.x + 1,
                y: sp.y + 1
            };

            // try to move
            if !filled.contains(&down) {
                sp = down;
            } else if !filled.contains(&left) {
                sp = left;
            } else if !filled.contains(&right) {
                sp = right;
            } else { // could not move
                filled.insert(sp);

                // are we done
                if sp.x == 500 && sp.y == 0 {
                    count += 1;
                    break 'out;
                } else {
                    break 'at_rest;
                }
            }
        }

        count += 1;
    }

    println!("{count}");
}

fn part1(filled: &mut HashSet<Pos>, highest: i32) {
    let mut count = 0;

    // until something goes into the void
    'out:
    loop {
        let mut sp = Pos {
            x: 500,
            y: 0
        };

        // simulate
        'at_rest:
        loop {
            // did we fall into the void
            if sp.y > highest {
                break 'out;
            }

            let down = Pos {
                x: sp.x,
                y: sp.y + 1
            };

            let left = Pos {
                x: sp.x - 1,
                y: sp.y + 1
            };

            let right = Pos {
                x: sp.x + 1,
                y: sp.y + 1
            };

            // try to move
            if !filled.contains(&down) {
                sp = down;
            } else if !filled.contains(&left) {
                sp = left;
            } else if !filled.contains(&right) {
                sp = right;
            } else { // could not move
                filled.insert(sp);
                break 'at_rest;
            }
        }

        count += 1;
    }

    println!("{count}");
}

/// x,y -> Pos
fn parse_coord(coord: &str) -> Pos {
    let parts: Vec<&str> = coord.split(",").collect();

    return Pos {
        x: parts[0].parse().unwrap(),
        y: parts[1].parse().unwrap()
    };
}