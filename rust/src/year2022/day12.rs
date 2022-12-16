use std::collections::HashMap;

use priority_queue::PriorityQueue;

#[derive(Debug, Copy, Clone, Hash, Eq, PartialEq)]
struct Pos {
    x: i32,
    y: i32
}

pub fn main(input: String) {
    let mut x;
    let mut y = 0;

    let mut start = Pos { x: 0, y: 0 };
    let mut dest = Pos { x: 0, y: 0 };

    let mut map: Vec<Vec<i32>> = Vec::new();
    let mut aset: Vec<Pos> = Vec::new();

    // build map
    for ln in input.lines() {
        let mut line: Vec<i32> = Vec::new();
        x = 0;

        for c in ln.chars() {
            if c == 'S' || c == 'a' {
                start = Pos { x: x, y: y };
                aset.push(start);
                line.push(0);
            } else if c == 'E' {
                dest = Pos { x: x, y: y };
                line.push(25);
            } else {
                line.push((c as i32) - ('a' as i32));
            }

            x += 1;
        }

        map.push(line);
        y += 1;
    }

    part2(map, aset, dest);
}

fn part2(map: Vec<Vec<i32>>, aset: Vec<Pos>, dest: Pos) {
    let mut min = 100_000;

    for a in aset {
        let len = a_star(&map, a, dest).len();

        if len != 0 && len < min {
            min = len;
        }
    }

    println!("{min}");
}

fn part1(map: Vec<Vec<i32>>, start: Pos, dest: Pos) {
    println!("{start:?} {dest:?}");

    for y in 0..map.len() {
        for x in 0..map[0].len() {
            let c: char = (map[y][x] as u8 + ('a' as u8)) as char;
            print!("{c}");
        }
        println!();
    }
    println!();

    let path = a_star(&map, start, dest);

    let l = path.len();

    if l == 0 {
        println!("FAILURE");
    } else {
        println!("{l}");
    }
}

/// negative manhattan distance
/// so that closer = higher number
fn heuristic(p: Pos, dest: Pos) -> i32 {
    return -((p.x - dest.x).abs() + (p.y - dest.y).abs());
}

/// get from the map
fn get(map: &Vec<Vec<i32>>, p: Pos) -> i32 {
    // check bounds
    if p.x < 0 || p.y < 0 || p.x >= map[0].len() as i32 || p.y >= map.len() as i32 {
        return 100;
    }

    return map[p.y as usize][p.x as usize];
}

/// Constructs a path Vec for a position and parent map
fn construct_path(node: Pos, from_map: HashMap<Pos, Pos>) -> Vec<Pos> {
    let mut path: Vec<Pos> = Vec::new();
    let mut current = &node;

    let keys: Vec<&Pos> = from_map.keys().collect();

    while keys.contains(&current) {
        current = from_map.get(current).unwrap();
        path.push(*current);
    }

    return path;
}

/// Returns the neighboring nodes
fn neighbors(map: &Vec<Vec<i32>>, node: Pos) -> Vec<Pos> {
    let mut neighbors: Vec<Pos> = Vec::new();
    let n = get(map, node);
    let up = Pos { x: node.x, y: node.y + 1 };
    let down = Pos { x: node.x, y: node.y - 1 };
    let left = Pos { x: node.x - 1, y: node.y};
    let right = Pos { x: node.x + 1, y: node.y };

    if get(map, up) - n <= 1 {
        neighbors.push(up);
    }

    if get(map, down) - n <= 1 {
        neighbors.push(down);
    }

    if get(map, left) - n <= 1 {
        neighbors.push(left);
    }

    if get(map, right) - n <= 1 {
        neighbors.push(right);
    }

    return neighbors;
}

/// wow look an algorithm designed specifically for solving this kind of problem
fn a_star(map: &Vec<Vec<i32>>, start: Pos, dest: Pos) -> Vec<Pos> {
    let mut open_set: PriorityQueue<Pos, i32> = PriorityQueue::new();   // discovered nodes set for expansion
    let mut from_map: HashMap<Pos, Pos> = HashMap::new();               // map of parents
    let mut known_score: HashMap<Pos, i32> = HashMap::new();            // cheapest path to node

    let h = heuristic(start, dest);
    let def = &100_000;

    open_set.push(start, h);
    known_score.insert(start, 0);

    // aaaaaaaaaalgorithm
    while open_set.len() != 0 {
        let current_node = *open_set.peek().unwrap().0;

        //println!("{current_node:?}");

        if current_node == dest {
            return construct_path(current_node, from_map);
        }

        open_set.pop();
        for n in neighbors(map, current_node) {
            let tentative = known_score.get(&current_node).unwrap_or(def) + 1;
            if tentative < *known_score.get(&n).unwrap_or(def) {
                // better path found to neighbor
                from_map.insert(n, current_node);
                known_score.insert(n, tentative);

                open_set.push_increase(n, -tentative + heuristic(n, dest));
            }
        }
    }

    return Vec::new(); // failure
}