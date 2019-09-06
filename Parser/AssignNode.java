package Parser;

import Lexer.Token;

public class AssignNode extends Node {
    public Token op;
    public Node left;
    public Node right;
    public AssignNode(Node left, Token op, Node right){
        this.token = op;
        this.left = left;
        this.right = right;
    }
}
