package Parser;

import Lexer.Token;

public class BinOpNode extends Node {
    public Token op;
    public Node left;
    public Node right;

    public BinOpNode(Token token, Node left, Node right){
        super(token);
        op = token;
        this.left = left;
        this.right = right;
    }
}
