use std::fs;


mod util;
mod year2022;

fn main() {
    year2022::day3::main(fs::read_to_string("input.txt").unwrap());
}
