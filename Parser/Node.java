package Parser;

import Lexer.Token;

/**
 * base of AST
 */
public abstract class Node {
    public Token token;
    public Node(Token token){
        this.token = token;
    }
    public Node(){}
}
