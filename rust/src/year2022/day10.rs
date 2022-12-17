
use crate::util::*;

pub fn main(input: String) {
    let cpu = CPU::load(input.lines().collect());

    part2(cpu);
}

fn part2(mut cpu: CPU) {
    let mut display: Vec<Vec<bool>> = Vec::new();

    // 6 rows 40 columns
    for i in 0..6 {
        display.push(Vec::new());
        for _ in 0..40 {
            display[i].push(false);
        }
    }

    // draw
    let mut i: usize = 0;
    while !cpu.halted && i < (40 * 6) {
        // flip bit if needed
        let row = i / 40;
        let col = i % 40;

        if (cpu.x - col as i32).abs() <= 1 {
            display[row][col] = true;
        }

        cpu.step();
        i += 1;
    }

    // display
    for r in 0..6 {
        for c in 0..40 {
            if display[r][c] {
                print!("#");
            } else {
                print!(".");
            }
        }

        println!();
    }
}

fn part1(mut cpu: CPU) {
    let mut sum = 0;
    
    // execute all instructions
    let mut i = 0;
    while !cpu.halted && i < 221 {
        // capture state
        if ((i + 1) - 20) % 40 == 0 {
            sum += (i as i32 + 1) * cpu.x;
        }

        cpu.step();
        i += 1;
    }

    println!("{sum}");
}