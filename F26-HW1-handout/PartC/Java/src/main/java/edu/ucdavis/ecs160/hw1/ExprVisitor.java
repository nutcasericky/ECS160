package edu.ucdavis.ecs160.hw1;

public interface ExprVisitor<R> {
    R visit(Num expr);
    R visit(Add expr);
    R visit(Sub expr);
    R visit(Mult expr);
    R visit(Div expr);
}
