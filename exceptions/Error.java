package exceptions;

import Lexer.Token;

public class Error extends Exception{
    public ErrorCode errorCode;
    public Token token;
    public String msg;

    public Error(){

    }

    public Error(ErrorCode errorCode, Token token, String msg){
        this.errorCode = errorCode;
        this.token = token;
        this.msg = msg;
    }


}
