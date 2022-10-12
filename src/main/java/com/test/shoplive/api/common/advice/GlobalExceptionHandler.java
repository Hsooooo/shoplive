package com.test.shoplive.api.common.advice;

import com.test.shoplive.api.common.exception.BoardException;
import com.test.shoplive.api.common.exception.ErrorCode;
import com.test.shoplive.api.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleException(Exception e) {
        log.error(">>>> Exception",e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .code(ErrorCode.Common.C001)
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<?> handleNoSuchElementException(NoSuchElementException e) {
        log.error(">>>>> NoSuchElementException", e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                            .code(ErrorCode.Common.C001)
                            .message(e.getMessage())
                            .build());
    }

    @ExceptionHandler(BoardException.class)
    protected ResponseEntity<?> handleBoardException(BoardException e) {
        log.error(">>>> BoardException", e);
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ErrorResponse.builder()
                        .code(e.getErrorCode())
                        .message(e.getErrorCode().getMessage())
                        .build());
    }

}
