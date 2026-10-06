package edu.ucdavis.ecs160.hw1;

public final class Evaluator implements ExprVisitor<Integer> {
    @Override
    public Integer visit(Num expr) {
        return expr.getValue();
    }

    @Override
    public Integer visit(Add expr) {
        return expr.getLeft().accept(this) + expr.getRight().accept(this);
    }

    @Override
    public Integer visit(Sub expr) {
        return expr.getLeft().accept(this) - expr.getRight().accept(this);
    }

    @Override
    public Integer visit(Mult expr) {
        return expr.getLeft().accept(this) * expr.getRight().accept(this);
    }

    @Override
    public Integer visit(Div expr) {
        return expr.getLeft().accept(this) / expr.getRight().accept(this);
    }
}
