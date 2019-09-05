package Parser;

import Lexer.Token;

public class UnaryNode extends Node {
    public Token op;
    public Node expr;
    public UnaryNode(Token op, Node expr){
        super(op);
        this.op = op;
        this.expr = expr;
    }
}
