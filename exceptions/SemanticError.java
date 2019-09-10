package exceptions;

import Lexer.Token;

public class SemanticError extends Error {
    public SemanticError(){}

    public  SemanticError(ErrorCode errorCode, Token token, String msg){
        super(errorCode,token,msg);
    }
}
