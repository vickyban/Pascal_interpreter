package Parser;

import Lexer.Token;

import java.util.List;

public class ProcedureCall extends Node {
    public String procName;
    public List<Node> params;

    public ProcedureCall(String procName, List<Node> params, Token token){
        super(token);
        this.procName = procName;
        this.params = params;
    }
}
