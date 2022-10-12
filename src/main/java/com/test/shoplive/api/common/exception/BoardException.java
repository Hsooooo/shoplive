package com.test.shoplive.api.common.exception;

public class BoardException extends RuntimeException {

    private final ErrorCode.Board errorCode;

    public BoardException(Throwable cause, ErrorCode.Board errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public BoardException(ErrorCode.Board errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode.Board getErrorCode() {
        return errorCode;
    }
}
