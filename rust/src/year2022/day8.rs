
struct Tree {
    height: i32,
    visible: bool
}

pub fn main(input: String) {
    // get grid
    let mut grid: Vec<Vec<Tree>> = Vec::new();

    for ln in input.lines() {
        let mut v: Vec<Tree> = Vec::new();

        for c in ln.chars() {
            let t = Tree {
                height: (c as i32) - ('0' as i32),
                visible: false
            };
            v.push(t);
        }

        grid.push(v);
    }

    part2(&mut grid);
}

fn part2(grid: &mut Vec<Vec<Tree>>) {
    let grid_h = grid.len();
    let grid_w = grid[0].len();

    let mut highest_score = 0;

    // for each tree (edges ignored as they're guaranteed a score of zero)
    for x0 in 1..(grid_w - 1) {
        for y0 in 1..(grid_h - 1) {
            let h = grid[x0][y0].height;

            // determine score
            let mut score_up = 0;
            let mut score_down = 0;
            let mut score_left = 0;
            let mut score_right = 0;

            // look up
            for y1 in (0..y0).rev() {
                score_up += 1;
                if grid[x0][y1].height >= h {
                    break;
                }
            }

            // look down
            for y1 in (y0 + 1)..grid_h {
                score_down += 1;
                if grid[x0][y1].height >= h {
                    break;
                }
            }

            // look left
            for x1 in (0..x0).rev() {
                score_left += 1;
                if grid[x1][y0].height >= h {
                    break;
                }
            }

            // look right
            for x1 in (x0 + 1)..grid_w {
                score_right += 1;
                if grid[x1][y0].height >= h {
                    break;
                }
            }

            let score = score_up * score_down * score_left * score_right;

            //println!("{x0} {y0} {score_up} {score_down} {score_left} {score_right} {score}");

            if score > highest_score {
                highest_score = score;
            }
        }
    }

    println!("{highest_score}");
}

fn part1(grid: &mut Vec<Vec<Tree>>) {
    let grid_h = grid.len();
    let grid_w = grid[0].len();

    // determine visibility
    let mut highest_forward;
    let mut highest_backward;
    let mut num_visible = 0;

    // vertical
    for x in 0..grid_w {
        highest_forward = -1;
        highest_backward = -1;

        for y in 0..grid_h {
            let height_forward = grid[x][y].height;
            let height_backward = grid[x][grid_h - y - 1].height;

            if height_forward > highest_forward {
                highest_forward = height_forward;

                if !grid[x][y].visible {
                    num_visible += 1;
                    grid[x][y].visible = true;
                }
            }

            if height_backward > highest_backward {
                highest_backward = height_backward;

                if !grid[x][grid_h - y - 1].visible {
                    num_visible += 1;
                    grid[x][grid_h - y - 1].visible = true;
                }
            }
        }
    }

    // horizontal
    for y in 0..grid_h {
        highest_forward = -1;
        highest_backward = -1;

        for x in 0..grid_w {
            let height_forward = grid[x][y].height;
            let height_backward = grid[grid_w - x - 1][y].height;

            if height_forward > highest_forward {
                highest_forward = height_forward;

                if !grid[x][y].visible {
                    num_visible += 1;
                    grid[x][y].visible = true;
                }
            }

            if height_backward > highest_backward {
                highest_backward = height_backward;

                if !grid[grid_w - x - 1][y].visible {
                    num_visible += 1;
                    grid[grid_w - x - 1][y].visible = true;
                }
            }
        }
    }

    print_grid(&grid);
    println!("{num_visible}");
}

/// Prints the grid
fn print_grid(g: &Vec<Vec<Tree>>) {
    for r in g {
        for x in r {
            if x.visible {
                let h = x.height;
                print!("{h} ");
            } else {
                print!("  ");
            }
        }

        println!();
    }
}