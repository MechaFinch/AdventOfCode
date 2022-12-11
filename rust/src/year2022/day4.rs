
pub fn main(input: String) {
    let mut sum = 0;

    for ln in input.lines() {
        let elves: Vec<&str> = ln.split(",").collect();
        let first: Vec<&str> = elves[0].split("-").collect();
        let second: Vec<&str> = elves[1].split("-").collect();

        let (s1, e1) = (first[0].parse::<i32>().unwrap(), first[1].parse::<i32>().unwrap());
        let (s2, e2) = (second[0].parse::<i32>().unwrap(), second[1].parse::<i32>().unwrap());

        if part2(s1, e1, s2, e2) {
            sum += 1;
        }
    }

    println!("{sum}");
}

fn part2(s1: i32, e1: i32, s2: i32, e2: i32) -> bool {
    return (s1 <= e2 && e1 >= s2) || (s2 <= e1 && e2 >= s1);
}

fn part1(s1: i32, e1: i32, s2: i32, e2: i32) -> bool {
    return (s1 <= s2 && e1 >= e2) || (s2 <= s1 && e2 >= e1);
}