mod ast;
mod evaluator;

use ast::{add, div, mult, num, sub, Expr};

fn main() {
    let tests: Vec<(Expr, i64)> = vec![
        // Level 1: literals
        (num(42), 42),
        (num(-5), -5),
        // Level 2: one operator
        (add(num(1), num(2)), 3),
        (sub(num(10), num(4)), 6),
        (mult(num(6), num(7)), 42),
        (div(num(20), num(5)), 4),
        // Level 3: negative results and integer division
        (sub(num(3), num(10)), -7),
        (div(num(7), num(2)), 3),
        (div(num(-7), num(2)), -3),
        // Level 4: two operators
        (add(num(10), mult(num(2), num(3))), 16),
        (div(sub(num(20), num(4)), num(2)), 8),
        (mult(add(num(1), num(2)), sub(num(10), num(4))), 18),
        // Level 5: associativity is fixed by the tree shape
        (sub(sub(num(10), num(3)), num(2)), 5),
        (sub(num(10), sub(num(3), num(2))), 9),
        // Level 6: deeper nesting
        (add(add(add(add(num(1), num(2)), num(3)), num(4)), num(5)), 15),
        (div(mult(add(num(2), num(3)), sub(num(9), num(1))), add(num(1), num(1))), 20),
        (mult(sub(num(0), num(5)), div(num(-12), num(4))), 15),
        // Level 7: all four operators in one tree
        (
            add(
                mult(div(num(100), num(5)), sub(num(7), num(3))),
                div(sub(num(50), mult(num(4), num(5))), add(num(2), num(1))),
            ),
            90,
        ),
    ];

    for (i, (expr, expected)) in tests.iter().enumerate() {
        let actual = expr.eval();
        println!("Test {} = {}", i + 1, actual);
        assert_eq!(actual, *expected, "Test {}", i + 1);
    }

    println!("All assertions passed");
}
