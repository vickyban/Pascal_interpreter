package exceptions;

import Lexer.Token;

public class LexerError extends Error {
    public LexerError(){}

    public  LexerError(ErrorCode errorCode, Token token, String msg){
        super(errorCode,token,msg);
    }
}
