use std::collections::{HashMap, HashSet};

use crate::util::Pos;

pub fn main(input: String) {
    let mut sensors_map: HashMap<Pos, i32> = HashMap::new(); // maps sensors to their manhattan distance
    let mut beacons: HashSet<Pos> = HashSet::new();

    for ln in input.lines() {
        let sx_start = ln.find("x=").unwrap() + 2;
        let sx_end = ln.find("y=").unwrap() - 2;
        let sy_start = sx_end + 4;
        let sy_end = ln.find(":").unwrap();

        let bx_start = ln.rfind("x=").unwrap() + 2;
        let bx_end = ln.rfind("y=").unwrap() - 2;
        let by_start = bx_end + 4;
        let by_end = ln.len();

        let sx: i32 = ln[sx_start..sx_end].parse().unwrap();
        let sy: i32 = ln[sy_start..sy_end].parse().unwrap();
        let bx: i32 = ln[bx_start..bx_end].parse().unwrap();
        let by: i32 = ln[by_start..by_end].parse().unwrap();

        let sensor = Pos { x: sx, y: sy };
        let beacon = Pos { x: bx, y: by };

        sensors_map.insert(sensor, (sx - bx).abs() + (sy - by).abs());
        beacons.insert(beacon);
    }

    part2(sensors_map, beacons);
}

fn part2(sensors_map: HashMap<Pos, i32>, beacons: HashSet<Pos>) {
    let range = 4_000_000;

    let mut x = 0;
    let mut y = 0;
    'out:
    while x <= range {
        y = 0;

        'search:
        while y <= range {
            // check if occupied
            let p = Pos { x: x, y: y };

            // if this space has a sensor, jump to the end of its range
            if sensors_map.contains_key(&p) {
                y += sensors_map.get(&p).unwrap();
                continue 'search;
            } else if beacons.contains(&p) {
                // if this space has a beacon, just skip it
                y += 1;
                continue 'search;
            }

            // find in-range sensors
            for (sensor, range) in &sensors_map {
                // if the space is in the range of a sensor, jump to the end of its range
                let distance = (x - sensor.x).abs() + (y - sensor.y).abs();
                if distance <= *range {
                    y += (range - distance) + 1;
                    continue 'search;
                }
            }

            // if we reach this point, we aren't in range of any sensors
            break 'out;
        }

        x += 1;
    }

    if x <= range {
        let freq = (x as i64 * 4_000_000) + y as i64;
        println!("{freq}");
    } else {
        println!("not found");
    }
}

fn part1(sensors_map: HashMap<Pos, i32>, beacons: HashSet<Pos>) {
    let target = 2_000_000;

    // find number of beacons on the target line
    let mut y2_beacon_count = 0;

    for b in beacons {
        if b.y == target {
            y2_beacon_count += 1;
        }
    }

    // count covered positions on target line
    let mut covered: HashSet<i32> = HashSet::new();

    for (sensor, range) in sensors_map {
        // range - (ranged used getting to target line)
        let y2_range = range - (sensor.y - target).abs() + 1;

        if y2_range >= 0 {
            for dx in 0..y2_range {
                covered.insert(sensor.x + dx);
                covered.insert(sensor.x - dx);
            }
        }
    }

    let covered_count = covered.len() - y2_beacon_count;

    println!("{covered_count}");
}