package edu.ucdavis.ecs160.hw1;

public class Main {
    private static int testNumber = 0;

    public static void main(String[] args) {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true;
        if (!assertionsEnabled) {
            throw new IllegalStateException("Assertions are disabled; run with java -ea");
        }

        // Level 1: literals
        check(new Num(42), 42);
        check(new Num(-5), -5);

        // Level 2: one operator
        check(new Add(new Num(1), new Num(2)), 3);
        check(new Sub(new Num(10), new Num(4)), 6);
        check(new Mult(new Num(6), new Num(7)), 42);
        check(new Div(new Num(20), new Num(5)), 4);

        // Level 3: negative results and integer division
        check(new Sub(new Num(3), new Num(10)), -7);
        check(new Div(new Num(7), new Num(2)), 3);
        check(new Div(new Num(-7), new Num(2)), -3);

        // Level 4: two operators
        check(new Add(new Num(10), new Mult(new Num(2), new Num(3))), 16);
        check(new Div(new Sub(new Num(20), new Num(4)), new Num(2)), 8);
        check(new Mult(new Add(new Num(1), new Num(2)), new Sub(new Num(10), new Num(4))), 18);

        // Level 5: associativity is fixed by the tree shape
        check(new Sub(new Sub(new Num(10), new Num(3)), new Num(2)), 5);
        check(new Sub(new Num(10), new Sub(new Num(3), new Num(2))), 9);

        // Level 6: deeper nesting
        check(new Add(new Add(new Add(new Add(new Num(1), new Num(2)), new Num(3)), new Num(4)), new Num(5)), 15);
        check(new Div(
                new Mult(new Add(new Num(2), new Num(3)), new Sub(new Num(9), new Num(1))),
                new Add(new Num(1), new Num(1))), 20);
        check(new Mult(
                new Sub(new Num(0), new Num(5)),
                new Div(new Num(-12), new Num(4))), 15);

        // Level 7: all four operators in one tree
        check(new Add(
                new Mult(new Div(new Num(100), new Num(5)), new Sub(new Num(7), new Num(3))),
                new Div(new Sub(new Num(50), new Mult(new Num(4), new Num(5))), new Add(new Num(2), new Num(1)))), 90);

        System.out.println("All assertions passed");
    }

    private static void check(Expr expr, int expected) {
        int actual; // FILL THIS!
        testNumber++;
        System.out.println("Test " + testNumber + " = " + actual);
        assert actual == expected : "Test " + testNumber + ": expected " + expected + ", got " + actual;
    }
}
