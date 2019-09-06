package Parser;

import Lexer.Token;

public class TypeNode extends Node{
    public String value;
    public TypeNode(Token token){
        super(token);
        this.value = token.value;
    }
}
