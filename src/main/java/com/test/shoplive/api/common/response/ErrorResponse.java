package com.test.shoplive.api.common.response;

import com.test.shoplive.api.common.exception.Code;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorResponse {
    private Code code;
    private String message;
}
