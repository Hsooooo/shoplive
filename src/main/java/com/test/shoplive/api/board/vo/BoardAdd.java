package com.test.shoplive.api.board.vo;

import lombok.Getter;

public class BoardAdd {
    @Getter
    public static class Request {
        private String title;
        private String content;
        private String userId;
    }
}




