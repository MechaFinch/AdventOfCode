use std::{fs, time::Instant};


mod util;
mod year2022;

fn main() {
    let input = fs::read_to_string("input.txt").unwrap();
    let start_time = Instant::now();

    year2022::day17::main(input);

    let duration = start_time.elapsed();

    println!("\n\nin {:?}", duration);
}
