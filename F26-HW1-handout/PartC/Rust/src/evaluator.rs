use crate::ast::Expr;

impl Expr {
    pub fn eval(&self) -> i64 {
        match self {
            Expr::Num(value) => *value,
            Expr::Add(left, right) => left.eval() + right.eval(),
            Expr::Sub(left, right) => left.eval() - right.eval(),
            Expr::Mult(left, right) => left.eval() * right.eval(),
            Expr::Div(left, right) => left.eval() / right.eval(),
        }
    }
}
