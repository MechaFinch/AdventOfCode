
pub fn main(input: String) {
    let rounds: Vec<&str> = input.split("\n").collect();

    part2(rounds);
}

fn part2(rounds: Vec<&str>) {
    let mut total_score = 0;

    for r in rounds {
        let round: Vec<char> = r.chars().collect();

        let them = match round[0] {
            'A' => 1, // rock
            'B' => 2, // paper
            'C' => 3, // scissors
            _   => 0
        };

        let us = match round[2] {
            'X' => match them { // lose
                1   => 3,
                2   => 1,
                3   => 2,
                _   => 0
            },
            'Y' => them,        // draw
            'Z' => match them { // win
                1   => 2,
                2   => 3,
                3   => 1,
                _   => 0
            },
            _   => 0
        };

        if us == them { // draw
            total_score += 3;
        } else if (us == 1 && them == 3) || (us == 2 && them == 1) || (us == 3 && them == 2) {
            total_score += 6;
        }

        // selected score
        total_score += us;
    }

    println!("{total_score}");
}

fn part1(rounds: Vec<&str>) {
    let mut total_score = 0;

    for r in rounds {
        let round: Vec<char> = r.chars().collect();

        let them = match round[0] {
            'A' => 1, // rock
            'B' => 2, // paper
            'C' => 3, // scissors
            _   => 0
        };

        let us = match round[2] {
            'X' => 1, // rock
            'Y' => 2, // paper
            'Z' => 3, // scissors
            _   => 0
        };

        if us == them { // draw
            total_score += 3;
        } else if (us == 1 && them == 3) || (us == 2 && them == 1) || (us == 3 && them == 2) {
            total_score += 6;
        }

        // selected score
        total_score += us;
    }

    println!("{total_score}");
}