package com.test.shoplive.api.board.entity;

import javax.persistence.Column;
import java.io.Serializable;

public class BoardImageId implements Serializable {
    @Column(name = "image_index")
    private int imageIndex;
    @Column(name = "board_id")
    private Long boardId;
}
