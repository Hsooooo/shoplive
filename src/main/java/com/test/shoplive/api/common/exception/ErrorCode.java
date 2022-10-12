package com.test.shoplive.api.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class ErrorCode {

    @Getter
    @RequiredArgsConstructor
    public enum Board implements Code {
        B001("1분 뒤 다시 시도하세요.", HttpStatus.TOO_MANY_REQUESTS),
        B002("권한이 없는 사용자입니다.", HttpStatus.FORBIDDEN),
        B003("이미지의 개수는 2개를 초과해서는 안됩니다.", HttpStatus.BAD_REQUEST);

        private final String message;
        private final HttpStatus httpStatus;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Common implements Code {
        C001
    }
}
