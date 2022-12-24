use std::{collections::{HashMap, HashSet}, cmp::{self, Reverse}};

use priority_queue::PriorityQueue;

#[derive(Debug, Clone)]
struct Node {
    name: String,
    rate: i32,
    neighbors: Vec<String>,
    fastest: HashMap<String, String>,
    fastest_distance: HashMap<String, i32>
}

struct FastNode {
    name: String,
    rate: i32,
    neighbors: HashMap<String, i32> // name, distance
}

pub fn main(input: String) {
    let mut graph: HashMap<String, Node> = HashMap::new();

    // parse input
    for line in input.lines() {
        let chars: Vec<char> = line.chars().collect();

        let name_start = 6;
        let name_end = 8;
        let rate_start = 23;
        let rate_end = line.find(";").unwrap();
        let mut valves_start = rate_end + 24;
        let valves_end = line.len();

        if line.contains("valves") {
            valves_start += 1;
        }

        let name: String = chars[name_start..name_end].into_iter().collect();
        let rate: i32 = chars[rate_start..rate_end].into_iter().collect::<String>().parse().unwrap();
        let valves: Vec<String> = chars[valves_start..valves_end].into_iter().collect::<String>().split(", ").map(|s| s.to_string()).collect();

        //print!("{name} {rate} ");
        //for v in &valves {
        //    print!("{v} ");
        //}
        println!();

        let node = Node {
            name: name.clone(),
            rate: rate,
            neighbors: valves,
            fastest: HashMap::new(),
            fastest_distance: HashMap::new()
        };

        graph.insert(name, node);
    }

    // please give me a garbage collector so I can stop dealing with this
    let mut graph_snapshot: HashMap<String, Node> = HashMap::new();

    for (s, n) in &graph {
        let a = &n.name;
        let b = n.rate;
        let c = &n.neighbors;
        print!("{s}: {a} {b} ");
        for d in c { print!("{d} "); }
        println!();

        graph_snapshot.insert(s.clone(), n.clone());
    }

    let keys: Vec<&String> = graph_snapshot.keys().collect();

    // determine fastest paths
    for name in &keys {
        let node = graph.get_mut(*name).unwrap();

        for k in &keys {
            if k == name {
                node.fastest_distance.insert((*k).clone(), 0);
            } else if node.neighbors.contains(k) {
                node.fastest.insert((*k).clone(), (*k).clone());
                node.fastest_distance.insert((*k).clone(), 1);
            } else {
                let n = *k;
                let (fastest_neighbor, fastest_distance) = a_star(&graph_snapshot, &node.name, n);

                node.fastest.insert(n.clone(), fastest_neighbor);
                node.fastest_distance.insert(n.clone(), fastest_distance);
            }
        }
    }

    let fgraph = convert(graph);

    part2(fgraph);
}

fn convert(graph: HashMap<String, Node>) -> HashMap<String, FastNode> {
    // convert to nonzero-only nodes
    let mut new_graph: HashMap<String, FastNode> = HashMap::new();
    let mut nonzero_rate: Vec<String> = Vec::new();

    for (name, node) in &graph {
        if node.rate != 0 {
            nonzero_rate.push(name.clone());
        }
    }

    for (name, node) in &graph {
        if node.rate != 0 {
            let mut fnode = FastNode {
                name: name.clone(),
                rate: node.rate,
                neighbors: HashMap::new()
            };

            for (n, d) in &node.fastest_distance {
                if nonzero_rate.contains(n) && n != name {
                    fnode.neighbors.insert(n.clone(), *d);
                }
            }

            new_graph.insert(name.clone(), fnode);
        }
    }
    
    // add AA to the graph to start from
    let a_name = "AA".to_string();
    let mut a_node = FastNode {
        name: "AA".to_string(),
        rate: 0,
        neighbors: HashMap::new()
    };

    let a_onode = graph.get("AA").unwrap();
    for (n, d) in &a_onode.fastest_distance {
        if nonzero_rate.contains(n) {
            a_node.neighbors.insert(n.clone(), *d);
        }
    }

    new_graph.insert(a_name, a_node);

    // print
    for (name, node) in &new_graph {
        let r = node.rate;
        print!("{name} {r}");
        for (n, d) in &node.neighbors {
            print!("{{{n}: {d}}}");
        }
        println!();
    }

    return new_graph;
}

fn part2(graph: HashMap<String, FastNode>) {
    let mut open_set: HashSet<String> = HashSet::new();
    let mut path_a: Vec<String> = Vec::new();
    let mut path_b: Vec<String> = Vec::new();

    let result = dual_max_search(&graph, graph.get("AA").unwrap(), graph.get("AA").unwrap(), &mut open_set, &mut path_a, &mut path_b, 0, 0, 27, 0, -1);
    println!("{result}");
}

fn part1(graph: HashMap<String, FastNode>) {
    // serach
    let mut open_set: HashSet<String> = HashSet::new();
    let result = max_search(&graph, graph.get("AA").unwrap(), &mut open_set, 0, 31, 0);
    println!("{result}");
}

fn concat(set_a: &Vec<String>, set_b: &Vec<String>) -> String {
    let mut str = "".to_owned();

    for a in set_a {
        str.push_str(a);
    }

    for b in set_b {
        str.push_str(b);
    }

    return str;
}

/// search for the maximum path with 2 agents
/// node_a is the target node of agent A
/// node_b is the target ndoe of agent B
/// dist_a is the distance from agent A to its target
/// dist_b is the distance from agent B to its target
/// path history records sum paths (a + b) to prevent duplicate work
fn dual_max_search(graph: &HashMap<String, FastNode>, node_a: &FastNode, node_b: &FastNode, open: &mut HashSet<String>, path_a: &mut Vec<String>, path_b: &mut Vec<String>, dist_a: i32, dist_b: i32, time_left: i32, score: i32, global_best: i32) -> i32 {
    // neither agent can reach a node
    if time_left < dist_a && time_left < dist_b {
        return score;
    }
    
    // if it's not possible to exceed the maximum by simultaneously activating remaining nodes, stop trying
    let mut impossible_case = score;
    let min = cmp::min(dist_a, dist_b);
    for (name, node) in graph {
        if !open.contains(name) {
            impossible_case += node.rate * time_left - min - 1;
        }
    }

    if impossible_case < global_best {
        return 0;
    }

    let mut gbest = global_best;

    // both agents reach a node at the same time
    if dist_a == dist_b {
        // both agents open valves
        let new_time_left = time_left - dist_a - 1;
        let new_score = score + (new_time_left * (node_a.rate + node_b.rate));

        open.insert(node_a.name.clone());
        open.insert(node_b.name.clone());
        path_a.push(node_a.name.clone());
        path_b.push(node_b.name.clone());

        let mut best = new_score;
        let mut terminal = true;

        // recurse
        for (next_node_a, next_dist_a) in &node_a.neighbors {
            if !open.contains(next_node_a) {
                for (next_node_b, next_dist_b) in &node_b.neighbors {
                    if next_node_a != next_node_b && !open.contains(next_node_b) {
                        terminal = false;
                        let nna = graph.get(next_node_a).unwrap();
                        let nnb = graph.get(next_node_b).unwrap();

                        best = cmp::max(best, dual_max_search(graph, nna, nnb, open, path_a, path_b, *next_dist_a, *next_dist_b, new_time_left, new_score, gbest));
                        gbest = cmp::max(best, gbest);
                    }
                }
            }
        }

        if terminal {
            /*
            println!("{best}");
            print!("A: ");
            for p in path_a.iter() {
                print!("{p} ");
            }
            print!("\nB: ");
            for p in path_b.iter() {
                print!("{p} ");
            }
            println!("\n");
            */
        }

        // close
        open.remove(&node_a.name);
        open.remove(&node_b.name);
        path_a.pop();
        path_b.pop();
        return best;
    } else if dist_a < dist_b { // agent A reaches its node before agent B
        let new_time_left = time_left - dist_a - 1;
        let new_dist_b = dist_b - dist_a - 1;
        let new_score = score + (new_time_left * node_a.rate);

        open.insert(node_a.name.clone());
        path_a.push(node_a.name.clone());

        let mut best = new_score;
        let mut terminal = true;

        // recurse
        for (next_node_a, next_dist_a) in &node_a.neighbors {
            if next_node_a != &node_b.name && !open.contains(next_node_a) {
                terminal = false;
                let nna = graph.get(next_node_a).unwrap();

                best = cmp::max(best, dual_max_search(graph, nna, node_b, open, path_a, path_b, *next_dist_a, new_dist_b, new_time_left, new_score, gbest));
                gbest = cmp::max(best, gbest);
            }
        }

        if terminal {
            // there are no more valves for agent A to open, but agent B might have one left
            if new_dist_b < new_time_left {
                let new_time_left = new_time_left - new_dist_b - 1;
                best = new_score + (new_time_left * node_b.rate);
            }

            /*
            println!("{best}");
            print!("A: ");
            for p in path_a.iter() {
                print!("{p} ");
            }
            print!("\nB: ");
            for p in path_b.iter() {
                print!("{p} ");
            }
            println!("\n");
            */

            open.remove(&node_a.name);
            path_a.pop();

            return best;
        }

        // close
        open.remove(&node_a.name);
        path_a.pop();
        return best;
    } else { // agent B reaches its node before agent A
        let new_time_left = time_left - dist_b - 1;
        let new_dist_a = dist_a - dist_b - 1;
        let new_score = score + (new_time_left * node_b.rate);

        open.insert(node_b.name.clone());
        path_b.push(node_b.name.clone());

        let mut best = new_score;
        let mut terminal = true;

        // recurse
        for (next_node_b, next_dist_b) in &node_b.neighbors {
            if next_node_b != &node_a.name && !open.contains(next_node_b) {
                terminal = false;
                let nnb = graph.get(next_node_b).unwrap();

                best = cmp::max(best, dual_max_search(graph, node_a, nnb, open, path_a, path_b, new_dist_a, *next_dist_b, new_time_left, new_score, gbest));
                gbest = cmp::max(best, gbest);
            }
        }

        if terminal {
            // there are no more valves for agent B to open, but agent A might have one left
            if new_dist_a < new_time_left {
                let new_time_left = new_time_left - new_dist_a - 1;
                best = new_score + (new_time_left * node_a.rate);
            }

            /*
            println!("{best}");
            print!("A: ");
            for p in path_a.iter() {
                print!("{p} ");
            }
            print!("\nB: ");
            for p in path_b.iter() {
                print!("{p} ");
            }
            println!("\n");
            */

            open.remove(&node_b.name);
            path_b.pop();

            return best;
        }

        // close
        open.remove(&node_b.name);
        path_b.pop();
        return best;
    }
}

/// search for the maximum path
fn max_search(graph: &HashMap<String, FastNode>, node: &FastNode, open: &mut HashSet<String>, dist: i32, time_left: i32, score: i32) -> i32 {
    // node cannot be reached
    if time_left < dist {
        return score;
    } else {
        // node reached, update
        let tl = time_left - dist - 1;
        let ns = score + (tl * node.rate);

        open.insert(node.name.clone());
        let mut best = ns;

        // try to open more
        for (n, d) in &node.neighbors {
            if !open.contains(n) {
                best = cmp::max(best, max_search(graph, graph.get(n).unwrap(), open, *d, tl, ns));
            }
        }

        // undo persistent
        open.remove(&node.name);
        return best;
    }
}

fn construct_path(node: &String, from_map: &HashMap<String, String>) -> (String, i32) {
    let mut previous = node;
    let mut current = node;
    let mut count = 0;

    let keys: Vec<&String> = from_map.keys().collect();

    while keys.contains(&current) {
        previous = current;
        current = from_map.get(current).unwrap();
        count += 1;
    }

    return (previous.clone(), count);
}

fn heuristic(node: &Node, target: &String) -> i32 {
    if node.neighbors.contains(&target) {
        return 1;
    } else {
        return 0;
    }
}

/// performs a* and returns the first step (as that's all we use)
fn a_star(graph: &HashMap<String, Node>, start: &String, dest: &String) -> (String, i32) {
    let mut open_set: PriorityQueue<String, Reverse<i32>> = PriorityQueue::new();
    let mut from_map: HashMap<String, String> = HashMap::new();
    let mut known_score: HashMap<String, i32> = HashMap::new();

    let def = &100_000;
    let h = heuristic(graph.get(start).unwrap(), &dest);

    open_set.push(start.clone(), std::cmp::Reverse(h));
    known_score.insert(start.clone(), 0);

    //println!("pathing {start} to {dest}");
    
    while open_set.len() != 0 {
        let current_node = &open_set.peek().unwrap().0.clone();

        //println!("trying {current_node}");

        if current_node == dest {
            //println!("found dest");
            return construct_path(current_node, &from_map);
        }

        open_set.pop();
        for n in &graph.get(current_node).unwrap().neighbors {
            let tentative = known_score.get(current_node).unwrap_or(def) + 1;

            if tentative < *known_score.get(n).unwrap_or(def) {
                from_map.insert(n.clone(), current_node.clone());
                known_score.insert(n.clone(), tentative);

                //println!("{n}");

                open_set.push_increase(n.clone(), std::cmp::Reverse(tentative + heuristic(&graph.get(n).unwrap(), &dest)));
            }
        }
    }

    return ("failed".to_string(), 9999);
}