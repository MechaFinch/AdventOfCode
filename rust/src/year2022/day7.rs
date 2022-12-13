use std::collections::HashMap;


struct Entry {
    name: String,
    size: u32,
    contents: HashMap<String, Entry>
}

impl Entry {
    fn print(&self, d: u32) {
        let mut t = "".to_owned();

        for _ in 0..d {
            t += "\t";
        }

        let n = &self.name;
        let s = self.size();

        if self.contents.len() != 0 {
            println!("{t} {n} (dir, size={s})");

            for e in &self.contents {
                e.1.print(d + 1);
            }
        } else {
            println!("{t} {n} (file, size={s})");
        }
    }

    /// Adds an entry to a directory's contents
    fn add(&mut self, e: Entry) {
        self.contents.insert(e.name.to_owned(), e);
    }

    /// Gets the size of the entry
    fn size(&self) -> u32 {
        if self.contents.len() == 0 {
            return self.size;
        } else {
            let mut s = 0;

            for e in &self.contents {
                s += e.1.size();
            }

            return s;
        }
    }
}

pub fn main(input: String) {
    let tree = build_tree(input);

    //tree.print(0);

    let s = part2(&tree, tree.size() - (70_000_000 - 30_000_000));

    println!("{s}");
}

/// Returns the size of the smallest eligible directory, or 0 if one isn't found
fn part2(tree: &Entry, min_size: u32) -> u32 {
    let mut min = 70_000_000;

    // search all directories
    for e in &tree.contents {
        // is dir
        if e.1.contents.len() != 0 {
            // try contained stuff first
            let mut s = part2(e.1, min_size);

            // no eligible dirs, check the dir itself
            if s == 0 {
                let s2 = e.1.size();

                if s2 >= min_size {
                    s = s2;
                } else {
                    s = 70_000_000;
                }
            }

            // compare to min
            if s < min {
                min = s;
            }
        }
    }

    // did we find one
    if min == 70_000_000 {
        return 0;
    } else {
        return min;
    }
}

/// Returns the sum of all contained directories with sizes less than 100000's sizes
fn part1(tree: &Entry) -> u32 {
    let mut s = 0;

    for e in &tree.contents {
        if e.1.contents.len() != 0 {
            s += part1(e.1);

            let s2 = e.1.size();

            if s2 <= 100000 {
                s += s2;
            }
        }
    }

    return s;
}

/// Builds the directory tree
fn build_tree(input: String) -> Entry {
    let lines: Vec<&str> = input.lines().collect();

    let mut directory: Vec<&str> = Vec::new();

    let mut tree = Entry {
        name: "/".to_owned(),
        size: 0,
        contents: HashMap::new()
    };

    for ln in lines {
        let chars: Vec<char> = ln.chars().collect();
        let parts: Vec<&str> = ln.split_ascii_whitespace().collect();

        //println!("{ln}");

        // command
        if chars[0] == '$' {
            if parts[1] == "cd" { // change dir
                if parts[2] == ".." { // go back
                    directory.pop();
                } else if parts[2] != "/" { // go in
                    directory.push(parts[2]);
                }
            } // other commands can be ignored
        } else { // contents
            if parts[0] == "dir" { // dir
                let d = Entry {
                    name: parts[1].to_owned(),
                    size: 0,
                    contents: HashMap::new()
                };

                navigate(&mut tree, &directory).add(d);
            } else { // file
                let d = Entry {
                    name: parts[1].to_owned(),
                    size: parts[0].parse::<u32>().unwrap(),
                    contents: HashMap::new()
                };

                navigate(&mut tree, &directory).add(d);
            }
        }
    }

    return tree;
}

/// Get the entry at the given address
fn navigate<'a>(tree: &'a mut Entry, dir: &Vec<&str>) -> &'a mut Entry {
    let mut e = tree;

    for d in dir {
        e = e.contents.get_mut(d.to_owned()).unwrap();
    }

    return e;
}