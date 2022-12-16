
#[derive(Clone)]
struct Monkey {
    items: Vec<i64>,
    op: Operation,
    opval: i64,
    test: i64,
    true_target: usize,
    false_target: usize,
    inspect_count: i64
}

#[derive(Clone)]
enum Operation {
    ADD,
    MULTIPLY,
    SQUARE
}

pub fn main(input: String) {
    let lines = input.lines().collect::<Vec<&str>>();
    let mut monkeys: Vec<Monkey> = Vec::new();

    let mut i = 0;
    while i < lines.len() {
        // parse items
        let mut items: Vec<i64> = Vec::new();
        let itemlist = lines[i + 1].split(": ").collect::<Vec<&str>>()[1].split(", ");

        for item in itemlist {
            items.push(item.parse::<i64>().unwrap());
        }

        // parse operation
        let op;
        let opval;
        let opsplit: Vec<&str> = lines[i + 2].split(" ").collect();
        
        if opsplit[opsplit.len() - 1] == "old" {
            op = Operation::SQUARE;
            opval = 0;
        } else {
            op = match opsplit[opsplit.len() - 2] {
                "+" => Operation::ADD,
                "*" => Operation::MULTIPLY,
                _   => panic!()
            };

            opval = opsplit[opsplit.len() - 1].parse::<i64>().unwrap();
        }

        // parse test
        let test = lines[i + 3].split(" ").collect::<Vec<&str>>().pop().unwrap().parse::<i64>().unwrap(); // lmao

        // parse targets
        let true_target = lines[i + 4].split(" ").collect::<Vec<&str>>().pop().unwrap().parse::<usize>().unwrap(); // lmao
        let false_target = lines[i + 5].split(" ").collect::<Vec<&str>>().pop().unwrap().parse::<usize>().unwrap(); // lmao

        monkeys.push(Monkey {
            items: items,
            op: op,
            opval: opval,
            test: test,
            true_target: true_target,
            false_target: false_target,
            inspect_count: 0
        });

        i += 7;
    }

    part2(monkeys);
}

fn part2(mut monkeys: Vec<Monkey>) {
    // modular arithmetic go brrrrr
    // divisibility is preserved as long as the modulo is a multiple of the divisor, because reasons
    let mut modulo = 1;

    for m in &monkeys {
        modulo *= m.test;
    }

    // rounds
    for _ in 0..10_000 {
        // turns
        for m in 0..monkeys.len() {
            let monkey = monkeys[m].clone();

            // for each item
            for item in &monkey.items {
                // apply op
                let i = match monkey.op {
                    Operation::ADD      => item + monkey.opval,
                    Operation::MULTIPLY => item * monkey.opval,
                    Operation::SQUARE   => item * item
                } % modulo;

                // test & throw
                if (i % monkey.test) == 0 {
                    monkeys[monkey.true_target].items.push(i);
                } else {
                    monkeys[monkey.false_target].items.push(i);
                }
            }

            monkeys[m].inspect_count += monkeys[m].items.len() as i64;
            monkeys[m].items.clear();
        }
    }

    // monkey business
    let mut first = 0;
    let mut second = 0;

    for m in monkeys {
        if m.inspect_count > first {
            second = first;
            first = m.inspect_count;
        } else if m.inspect_count > second {
            second = m.inspect_count;
        }
    }

    let val = first * second;
    println!("{val}");
}

fn part1(mut monkeys: Vec<Monkey>) {
    // rounds
    for _ in 0..20 {
        // turns
        for m in 0..monkeys.len() {
            let monkey = monkeys[m].clone();

            // for each item
            for item in &monkey.items {
                // apply op
                let i = match monkey.op {
                    Operation::ADD      => item + monkey.opval,
                    Operation::MULTIPLY => item * monkey.opval,
                    Operation::SQUARE   => item * item
                } / 3;

                // test & throw
                if (i % monkey.test) == 0 {
                    monkeys[monkey.true_target].items.push(i);
                } else {
                    monkeys[monkey.false_target].items.push(i);
                }
            }

            monkeys[m].inspect_count += monkeys[m].items.len() as i64;
            monkeys[m].items.clear();
        }
    }

    // monkey business
    let mut first = 0;
    let mut second = 0;

    for m in monkeys {
        if m.inspect_count > first {
            second = first;
            first = m.inspect_count;
        } else if m.inspect_count > second {
            second = m.inspect_count;
        }
    }

    let val = first * second;
    println!("{val}");
}