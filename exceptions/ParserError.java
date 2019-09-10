package exceptions;

import Lexer.Token;

public class ParserError extends Error {
    public ParserError(){}

    public  ParserError(ErrorCode errorCode, Token token, String msg){
        super(errorCode,token,msg);
    }
}
