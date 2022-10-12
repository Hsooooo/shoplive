package com.test.shoplive.api.board.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class BoardListItem {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        @JsonProperty("title")
        private String title;
        @JsonProperty("user_id")
        private String userId;
        @JsonProperty("board_id")
        private Long boardId;
        @JsonProperty("create_dt")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createDt;
        @JsonProperty("is_image_contains")
        private boolean isImageContains;
    }
}
