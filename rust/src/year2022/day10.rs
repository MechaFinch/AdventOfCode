use std::str::FromStr;


#[derive(Debug, Clone, PartialEq)]
pub struct CPU {
    ip: usize,
    x: i32,
    inst: Instruction,
    stage: i32,
    program: Vec<Instruction>,
    halted: bool
}

#[derive(Debug, Copy, Clone, PartialEq)]
pub struct Instruction {
    op: Opcode,
    p0: i32
}

#[derive(Debug, Copy, Clone, PartialEq)]
pub enum Opcode {
    NOOP,
    ADDX
}

impl FromStr for Instruction {
    fn from_str(input: &str) -> Result<Instruction, Self::Err> {
        let split = input.split(" ").collect::<Vec<&str>>();

        match split[0].to_uppercase().as_str() {
            "NOOP"  => { // NOOP
                return Ok(Instruction {
                    op: Opcode::NOOP,
                    p0: 0
                });
            }

            "ADDX"  => { // ADDX <val>
                if split.len() > 1 {
                    return Ok(Instruction {
                        op: Opcode::ADDX,
                        p0: split[1].parse::<i32>().unwrap()
                    });
                } else {
                    return Err("Missing argument 0 for ADDX".to_string());
                }
            }

            _   => { // bad
                return Err("Bad opcode".to_string());
            }
        }
    }

    type Err = String;
}

impl CPU {
    /// Execute a single step
    pub fn step(&mut self) {
        if self.halted { return; }

        #[cfg(feature="cpudebug")]
        let dx = self.x;

        match self.inst.op {
            Opcode::NOOP    => {
                // no staging here
                #[cfg(feature="cpudebug")]
                println!("NOOP {dx}");

                self.next();
            }

            Opcode::ADDX    => {
                // x += 1 after one stage
                if self.stage == 1 {
                    self.x += self.inst.p0;
                    self.next();

                    #[cfg(feature="cpudebug")] {
                        let px = self.x;
                        println!("ADDX WRITE {dx} -> {px}");
                    }
                } else {
                    #[cfg(feature="cpudebug")]
                    println!("ADDX READ {dx}");
                    
                    self.stage += 1;
                }
            }
        }
    }

    /// Gets the next instruction
    fn next(&mut self) {
        self.ip += 1;

        if self.ip >= self.program.len() {
            println!("halted: out of instructions");
            self.halted = true;
        } else {
            self.inst = self.program[self.ip];
        }

        self.stage = 0;
    }

    /// Parses an input program
    pub fn load(program: Vec<&str>) -> CPU {
        let mut instructions: Vec<Instruction> = Vec::new();

        for ln in program {
            instructions.push(Instruction::from_str(ln).unwrap());
        }

        return CPU {
            ip: 0,
            x: 1,
            inst: instructions[0],
            stage: 0,
            program: instructions,
            halted: false
        };
    }
}

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