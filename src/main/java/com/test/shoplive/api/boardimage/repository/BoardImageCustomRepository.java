package com.test.shoplive.api.boardimage.repository;

import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import com.test.shoplive.api.board.entity.BoardImage;
import com.test.shoplive.api.board.entity.QBoardImage;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class BoardImageCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<BoardImage> listImageByBoardId(Long boardId) {
        JPAQuery<?> query = new JPAQuery<>(entityManager);
        QBoardImage qBoardImage = QBoardImage.boardImage;

        return query.select(qBoardImage)
                .from(qBoardImage)
                .where(qBoardImage.boardId.eq(boardId))
                .fetch();
    }

    public void deleteBoardImageByBoardId(Long boardId) {
        QBoardImage qBoardImage = QBoardImage.boardImage;
        new JPADeleteClause(entityManager, qBoardImage)
                .where(qBoardImage.boardId.eq(boardId))
                .execute();
    }

    public List<BoardImage> findBoardImageByBoardId(Long boardId) {
        JPAQuery<?> query = new JPAQuery<>(entityManager);
        QBoardImage qBoardImage = QBoardImage.boardImage;

        return query
                .select(qBoardImage)
                .from(qBoardImage)
                .where(qBoardImage.boardId.eq(boardId))
                .fetch();
    }

    public void deleteBoardImageByUrlList(List<String> deleteImageUrlList) {
        QBoardImage boardImage = QBoardImage.boardImage;
        new JPADeleteClause(entityManager, boardImage)
                .where(boardImage.url.in(deleteImageUrlList))
                .execute();
    }
}
