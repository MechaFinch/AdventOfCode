use std::collections::HashSet;

use crate::util::LPos;

struct Tube {
    filled: HashSet<LPos>,
    highest: i64,
    lowest: i64
}

impl Tube {
    fn place_single(&mut self, x: i64, y: i64) {
        self.filled.insert(LPos { x: x, y: y });
    }

    /// place rock type t with top-left position p
    fn place(&mut self, t: i32, p: LPos) {
        match t {
            0   => { // -
                self.place_single(p.x, p.y);
                self.place_single(p.x + 1, p.y);
                self.place_single(p.x + 2, p.y);
                self.place_single(p.x + 3, p.y);
            }
            
            1   => { // +
                self.place_single(p.x + 1, p.y);
                self.place_single(p.x, p.y - 1);
                self.place_single(p.x + 1, p.y - 1);
                self.place_single(p.x + 2, p.y - 1);
                self.place_single(p.x + 1, p.y - 2);
            }

            2   => { // _|
                self.place_single(p.x + 2, p.y);
                self.place_single(p.x + 2, p.y - 1);
                self.place_single(p.x + 2, p.y - 2);
                self.place_single(p.x + 1, p.y - 2);
                self.place_single(p.x, p.y - 2);
            }

            3   => { // |
                self.place_single(p.x, p.y);
                self.place_single(p.x, p.y - 1);
                self.place_single(p.x, p.y - 2);
                self.place_single(p.x, p.y - 3);
            }

            4   => { // #
                self.place_single(p.x, p.y);
                self.place_single(p.x + 1, p.y);
                self.place_single(p.x, p.y - 1);
                self.place_single(p.x + 1, p.y - 1);
            }

            _   => {}
        }

        // track highest
        if p.y > self.highest {
            self.highest = p.y;
        }

        // keep things nice and tidy so we don't run out of memory
        // if things are placed such that a line is fully blocked, remove everything below it
        let check_start = match t {
            0   => p.y,
            1   => p.y - 2,
            2   => p.y - 2,
            3   => p.y - 2,
            4   => p.y - 1,
            _   => p.y
        };

        for y in check_start..=p.y {
            let mut full = true;

            for x in 1..=7 {
                if !self.filled.contains(&LPos { x: x, y: y }) {
                    full = false;
                    break;
                }
            }

            if full {
                // clear below
                for y2 in self.lowest..y {
                    for x in 1..=7 {
                        self.filled.remove(&LPos { x: x, y: y2 });
                    }
                }

                //println!("cleared to {y}");

                self.lowest = y;
            }
        }
    }

    fn check_single(&self, x: i64, y: i64) -> bool {
        return x > 0 && x < 8 && y > 0 && !self.filled.contains(&LPos { x: x, y: y });
    }

    /// check if rock type t at top-left position p can move down
    fn check_down(&self, t: i32, p: LPos) -> bool {
        match t {
            0   => { // -
                return self.check_single(p.x, p.y - 1) &&
                       self.check_single(p.x + 1, p.y - 1) &&
                       self.check_single(p.x + 2, p.y - 1) &&
                       self.check_single(p.x + 3, p.y - 1);
            }
            
            1   => { // +
                return self.check_single(p.x, p.y - 2) &&
                       self.check_single(p.x + 1, p.y - 3) &&
                       self.check_single(p.x + 2, p.y - 2);
            }

            2   => { // _|
                return self.check_single(p.x, p.y - 3) &&
                       self.check_single(p.x + 1, p.y - 3) &&
                       self.check_single(p.x + 2, p.y - 3);
            }

            3   => { // |
                return self.check_single(p.x, p.y - 4);
            }

            4   => { // #
                return self.check_single(p.x, p.y - 2) &&
                       self.check_single(p.x + 1, p.y - 2);
            }

            _   => {}
        }

        return false;
    }
    
    /// check if rock type t at top-left position p can move left
    fn check_left(&self, t: i32, p: LPos) -> bool {
        match t {
            0   => { // -
                return self.check_single(p.x - 1, p.y);
            }
            
            1   => { // +
                return self.check_single(p.x, p.y) &&
                       self.check_single(p.x - 1, p.y - 1) &&
                       self.check_single(p.x, p.y - 2);
            }

            2   => { // _|
                return self.check_single(p.x + 1, p.y) &&
                       self.check_single(p.x + 1, p.y - 1) &&
                       self.check_single(p.x - 1, p.y - 2);
            }

            3   => { // |
                return self.check_single(p.x - 1, p.y) &&
                       self.check_single(p.x - 1, p.y - 1) &&
                       self.check_single(p.x - 1, p.y - 2) &&
                       self.check_single(p.x - 1, p.y - 3);
            }

            4   => { // #
                return self.check_single(p.x - 1, p.y) &&
                       self.check_single(p.x - 1, p.y - 1);
            }

            _   => {}
        }

        return false;
    }
    
    /// check if rock type t at top-left position p can move right
    fn check_right(&self, t: i32, p: LPos) -> bool {
        match t {
            0   => { // -
                return self.check_single(p.x + 4, p.y);
            }
            
            1   => { // +
                return self.check_single(p.x + 2, p.y) &&
                       self.check_single(p.x + 3, p.y - 1) &&
                       self.check_single(p.x + 2, p.y - 2);
            }

            2   => { // _|
                return self.check_single(p.x + 3, p.y) &&
                       self.check_single(p.x + 3, p.y - 1) &&
                       self.check_single(p.x + 3, p.y - 2);
            }

            3   => { // |
                return self.check_single(p.x + 1, p.y) &&
                       self.check_single(p.x + 1, p.y - 1) &&
                       self.check_single(p.x + 1, p.y - 2) &&
                       self.check_single(p.x + 1, p.y - 3);
            }

            4   => { // #
                return self.check_single(p.x + 2, p.y) &&
                       self.check_single(p.x + 2, p.y - 1);
            }

            _   => {}
        }

        return false;
    }
}

pub fn main(input: String) {
    part1(input);
}

fn part2(_input: String) {
    // to solve part 2, you either need to run part1's code for 265 hours or recognize patterns in the rocks
    // ill do the latter later
}

fn part1(input: String) {
    let mut tube = Tube {
        filled: HashSet::new(),
        highest: 0,
        lowest: 0
    };

    let mut highest = 0;

    let chars: Vec<char> = input.trim().chars().collect();
    let mut char_index = 0;

    // for each rock
    'rocks:
    for i in 0..35_000_000 {
        /*
        // show rocks
        for y in (0..=highest).rev() {
            for x in 0..=8 {
                if x == 0 || x == 8 {
                    print!("|");
                } else if y == 0 {
                    print!("-");
                } else if tube.filled.contains(&LPos { x: x, y: y }) {
                    print!("#");
                } else {
                    print!(".");
                }
            }
            println!();
        }
        println!();
        */

        let rock_type = (i % 5) as i32;
        let mut rock_pos = match rock_type {
            0   => LPos { x: 3, y: highest + 4 },
            1   => LPos { x: 3, y: highest + 6 },
            2   => LPos { x: 3, y: highest + 6 },
            3   => LPos { x: 3, y: highest + 7 },
            4   => LPos { x: 3, y: highest + 5 },
            _   => panic!()
        };

        // for each jet
        loop {
            let c = chars[char_index];
            char_index += 1;
            
            if char_index == chars.len() {
                char_index = 0;
            }

            // try to move left/right
            if c == '>' { // right
                if tube.check_right(rock_type, rock_pos) {
                    rock_pos = LPos { x: rock_pos.x + 1, y : rock_pos.y };
                }
            } else { // left
                if tube.check_left(rock_type, rock_pos) {
                    rock_pos = LPos { x: rock_pos.x - 1, y : rock_pos.y };
                }
            }

            // try to move down
            if tube.check_down(rock_type, rock_pos) {
                rock_pos = LPos { x: rock_pos.x, y: rock_pos.y - 1 };
            } else {
                tube.place(rock_type, rock_pos);
                
                if rock_pos.y > highest {
                    highest = rock_pos.y;
                }

                continue 'rocks;
            }
        }
    }

    println!("{highest}");
}