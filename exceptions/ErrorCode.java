package exceptions;

public enum ErrorCode {
    NONE(null),
    UNEXPECTED_TOKEN("Unexpected token"),
    ID_NOT_FOUND("Identifier not found"),
    DUPLICATED_ID("Duplicate id found"),
    INVALID_NUM_ARG("Invalid number of arguments");

    private String errMsg;
    ErrorCode(String errMsg){
        this.errMsg = errMsg;
    }

    public String toString(){
        return "ErrorCode: " + errMsg;
    }
}
