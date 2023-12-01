use std::str::FromStr;

#[derive(Debug, Copy, Clone, Eq, PartialEq)]
pub struct LPos {
    pub x: i64,
    pub y: i64
}

impl LPos {
    pub fn add(&self, other: &LPos) -> LPos {
        return LPos {
            x: self.x + other.x,
            y: self.y + other.y
        };
    }
}

impl std::hash::Hash for LPos {
    fn hash<H: std::hash::Hasher>(&self, hasher: &mut H) {
        hasher.write_i64((self.x << 32).wrapping_add(self.y));
    }
}

impl nohash_hasher::IsEnabled for LPos {}

#[derive(Debug, Copy, Clone, Eq, PartialEq)]
pub struct Pos {
    pub x: i32,
    pub y: i32
}

impl Pos {
    pub fn add(&self, other: &Pos) -> Pos {
        return Pos {
            x: self.x + other.x,
            y: self.y + other.y
        };
    }
}

impl std::hash::Hash for Pos {
    fn hash<H: std::hash::Hasher>(&self, hasher: &mut H) {
        hasher.write_i32((self.x << 16).wrapping_add(self.y));
    }
}

impl nohash_hasher::IsEnabled for Pos {}

#[derive(Debug, Copy, Clone, Eq, PartialEq)]
pub struct Pos3 {
    pub x: i32,
    pub y: i32,
    pub z: i32
}

impl Pos3 {
    pub fn add(&self, other: &Pos3) -> Pos3 {
        return Pos3 {
            x: self.x + other.x,
            y: self.y + other.y,
            z: self.z + other.z
        };
    }
}

impl std::hash::Hash for Pos3 {
    fn hash<H: std::hash::Hasher>(&self, hasher: &mut H) {
        hasher.write_i32((self.x << 22).wrapping_add(self.y << 11).wrapping_add(self.z));
    }
}

impl nohash_hasher::IsEnabled for Pos3 {}

#[derive(Debug, Clone, PartialEq)]
pub struct CPU {
    pub ip: usize,
    pub x: i32,
    pub inst: Instruction,
    pub stage: i32,
    pub program: Vec<Instruction>,
    pub halted: bool
}

#[derive(Debug, Copy, Clone, PartialEq)]
pub struct Instruction {
    pub op: Opcode,
    pub p0: i32
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