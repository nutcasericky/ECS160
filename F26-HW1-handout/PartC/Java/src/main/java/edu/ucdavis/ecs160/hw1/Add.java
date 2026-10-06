package edu.ucdavis.ecs160.hw1;

public final class Add extends BinaryExpr {
    public Add(Expr left, Expr right) {
        super(left, right);
    }
    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
