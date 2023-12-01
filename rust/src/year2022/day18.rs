use std::{collections::HashSet, slice::Iter, cmp};

use crate::util::Pos3;

#[derive(Copy, Clone, Hash, Eq, PartialEq)]
struct Face {
    p: Pos3,
    d: Direction
}

#[derive(Copy, Clone, Hash, Eq, PartialEq)]
enum Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST,
    UP,
    DOWN
}

impl Direction {
    fn iterator() -> Iter<'static, Direction> {
        static DIRECTIONS: [Direction; 6] = [Direction::NORTH, Direction::SOUTH, Direction::EAST, Direction::WEST, Direction::UP, Direction::DOWN];
        return DIRECTIONS.iter();
    }

    fn pos_offset(&self) -> Pos3 {
        match self {
            Direction::NORTH    => Pos3 { x: 0, y: 0, z: 1 },
            Direction::SOUTH    => Pos3 { x: 0, y: 0, z: -1 },
            Direction::EAST     => Pos3 { x: 1, y: 0, z: 0 },
            Direction::WEST     => Pos3 { x: -1, y: 0, z: 0 },
            Direction::UP       => Pos3 { x: 0, y: 1, z: 0 },
            Direction::DOWN     => Pos3 { x: 0, y: -1, z: 0 },
        }
    }
}

pub fn main(input: String) {
    let mut cubes: HashSet<Pos3> = HashSet::new();
    let mut cube;

    for line in input.lines() {
        let split: Vec<&str> = line.split(",").collect();

        cube = Pos3 {
            x: split[0].parse().unwrap(),
            y: split[1].parse().unwrap(),
            z: split[2].parse().unwrap()
        };

        cubes.insert(cube);
    }

    part2(cubes);
}

fn part2(cubes: HashSet<Pos3>) {
    // part 1
    let mut faces: HashSet<Face> = HashSet::new();
    
    for cube in &cubes {
        for offset in Direction::iterator() {
            let off_cube = cube.add(&offset.pos_offset());

            if !cubes.contains(&off_cube) {
                let face = Face {
                    p: off_cube,
                    d: *offset
                };
    
                faces.insert(face);
            }
        }
    }

    // find x/y/z bounds of the shape, then try to fill the area around it. Record all cubes that
    // can be touched by expanding according to the rules. Filter out any faces that do not come
    // from those cubes

    // find bounds
    let mut min_x = 100_000;
    let mut min_y = 100_000;
    let mut min_z = 100_000;
    let mut max_x = 0;
    let mut max_y = 0;
    let mut max_z = 0;

    for cube in &cubes {
        min_x = cmp::min(min_x, cube.x);
        min_y = cmp::min(min_y, cube.y);
        min_z = cmp::min(min_z, cube.z);
        max_x = cmp::max(max_x, cube.x);
        max_y = cmp::max(max_y, cube.y);
        max_z = cmp::max(max_z, cube.z);
    }

    min_x -= 1;
    min_y -= 1;
    min_z -= 1;
    max_x += 1;
    max_y += 1;
    max_z += 1;

    println!("{min_x} {max_x} {min_y} {max_y} {min_z} {max_z}");

    // fill space
    let mut reachable_cubes: HashSet<Pos3> = HashSet::new();
    fill_space(&Pos3 { x: min_x, y: min_y, z: min_z }, &cubes, &mut reachable_cubes, min_x, max_x, min_y, max_y, min_z, max_z);

    // filter faces
    for face in &faces.clone() {
        if !reachable_cubes.contains(&face.p) {
            faces.remove(&face);
        }
    }

    let count = faces.len();
    println!("{count}");
}

/// fill space by recursively exploring cubes. If a cube is not in the cubes set, it is recursed to
fn fill_space(cube: &Pos3, cubes: &HashSet<Pos3>, reachable: &mut HashSet<Pos3>, min_x: i32, max_x: i32, min_y: i32, max_y: i32, min_z: i32, max_z: i32) {
    // for each adjacent
    for offset in Direction::iterator() {
        let off_cube = cube.add(&offset.pos_offset());

        // bounds check
        if off_cube.x >= min_x && off_cube.x <= max_x &&
           off_cube.y >= min_y && off_cube.y <= max_y &&
           off_cube.z >= min_z && off_cube.z <= max_z {
            // wasn't already checked
            if reachable.insert(off_cube) {
                // recurse if not droplet cube
                if !cubes.contains(&off_cube) {
                    fill_space(&off_cube, cubes, reachable, min_x, max_x, min_y, max_y, min_z, max_z);
                }
            }
        }
    }
}

fn part1(cubes: HashSet<Pos3>) {
    let mut found: HashSet<Face> = HashSet::new();
    
    for cube in &cubes {
        for offset in Direction::iterator() {
            let off_cube = cube.add(&offset.pos_offset());

            if !cubes.contains(&off_cube) {
                let face = Face {
                    p: off_cube,
                    d: *offset
                };
    
                found.insert(face);
            }
        }
    }

    let count = found.len();

    println!("{count}");
}