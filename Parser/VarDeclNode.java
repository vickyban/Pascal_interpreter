package Parser;

import Lexer.Token;

public class VarDeclNode extends Node {
    public VariableNode var;
    public TypeNode varType;
    public VarDeclNode(VariableNode var, TypeNode varType){
        this.var = var;
        this.varType = varType;
    }
}
