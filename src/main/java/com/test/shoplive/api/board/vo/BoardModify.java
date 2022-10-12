package com.test.shoplive.api.board.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class BoardModify {
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Request {
        @JsonProperty("title")
        private String title;
        @JsonProperty("content")
        private String content;
        @JsonProperty("user_id")
        private String userId;
        @JsonProperty("create_dt")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createDt;
        @JsonProperty("board_image_item_list")
        private List<BoardImageItem> boardImageItemList;
    }
}
