package com.test.shoplive.api.board.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BoardImageItem {
    @JsonProperty("origin_file_name")
    private String originFileName;
    @JsonProperty("image_index")
    private int imageIndex;
    @JsonProperty("url")
    private String url;
}
