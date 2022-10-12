package com.test.shoplive.api.boardimage.repository;

import com.test.shoplive.api.board.entity.BoardImage;
import com.test.shoplive.api.board.entity.BoardImageId;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BoardImageRepository extends JpaRepository<BoardImage, BoardImageId> {
    @Modifying
    @Query(value = "INSERT INTO board_image (" +
                            "board_id," +
                            "origin_filename," +
                            "image_index," +
                            "url," +
                            "create_dt)" +
                        "VALUES (" +
                            ":boardId," +
                            ":originFilename," +
                            "SELECT IFNULL(MAX(image_index), 0) + 1 FROM board_image WHERE board_id = :boardId," +
                            ":url," +
                            ":createDt" +
                        ")", nativeQuery = true)
    void saveImageByParameters(@Param("boardId")Long boardId, @Param("originFilename")String originFilename, @Param("url")String url, @Param("createDt")LocalDateTime createDt);

    default void saveImage(BoardImage boardImage) {
        saveImageByParameters(boardImage.getBoardId(), boardImage.getOriginFilename(), boardImage.getUrl(), boardImage.getCreateDt());
    }

    @Query(value = "SELECT url FROM board_image WHERE url NOT IN (:remainImageUrlList)", nativeQuery = true)
    List<String> findDeleteTargetImageUrlList(@Param("remainImageUrlList")List<String> remainImageUrlList);

}
