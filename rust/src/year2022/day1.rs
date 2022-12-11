
pub fn main(input: String) {
    let split: Vec<&str> = input.split("\n").collect();

    let (mut first, mut second, mut third) = (0, 0, 0);

    let mut current = 0;

    for i in 0..split.len() {
        let s = split[i].trim();

        if s == "" {
            if current > first {
                third = second;
                second = first;
                first = current;
            } else if current > second {
                third = second;
                second = current;
            } else if current > third {
                third = current;
            }

            current = 0;
        } else {
            current += s.parse::<i32>().unwrap();
        }
    }

    let sum = first + second + third;

    println!("{sum}");
}