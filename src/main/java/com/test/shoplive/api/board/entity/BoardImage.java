package com.test.shoplive.api.board.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Table(name = "board_image")
@Entity
@IdClass(BoardImageId.class)
public class BoardImage implements Serializable {
    @Id
    @Column(name = "board_id")
    private Long boardId;
    @Id
    @Column(name = "image_index")
    private int imageIndex;
    @Column(name = "origin_filename")
    private String originFilename;
    @Column(name = "url")
    private String url;
    @Column(name = "create_dt")
    @CreatedDate
    private LocalDateTime createDt;
    @Column(name = "update_dt")
    private LocalDateTime updateDt;
}
