package Parser;

import Lexer.Token;

public class VariableNode extends Node {
    public String name;
    public VariableNode(Token token){
        super(token);
        name = token.value;
    }
}
