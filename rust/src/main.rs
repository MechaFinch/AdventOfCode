use std::{fs, time::Instant, thread};

#[allow(dead_code)]
mod util;

mod year2022;

fn main() {
    let input = fs::read_to_string("input.txt").unwrap();
    let start_time = Instant::now();

    run_with_large_stack(input);

    let duration = start_time.elapsed();

    println!("\n\nin {:?}", duration);
}

#[allow(dead_code)]
fn run_with_large_stack(input: String) {
    let builder = thread::Builder::new().stack_size(80 * 1024 * 1024);
    let handler = builder.spawn(|| {
        year2022::day18::main(input);
    }).unwrap();
    handler.join().unwrap();
}