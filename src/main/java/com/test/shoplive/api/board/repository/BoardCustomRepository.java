package com.test.shoplive.api.board.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.test.shoplive.api.board.entity.Board;
import com.test.shoplive.api.board.entity.QBoard;
import com.test.shoplive.api.board.entity.QBoardImage;
import com.test.shoplive.api.board.enums.SearchType;
import com.test.shoplive.api.board.enums.SortType;
import com.test.shoplive.api.board.vo.BoardListItem;
import com.test.shoplive.api.board.vo.BoardModify;
import com.test.shoplive.api.common.pageable.RestPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Board findBoardByUserIdRecently(String userId) {
        JPAQuery<?> query = new JPAQuery<>(entityManager);

        QBoard qBoardEntity = QBoard.board;

        return query.select(qBoardEntity)
                .from(qBoardEntity)
                .orderBy(qBoardEntity.createDt.desc())
                .where(qBoardEntity.userId.eq(userId))
                .limit(1)
                .fetchFirst();
    }

    public Page<BoardListItem.Response> findBoardBySearch(String keyword, SortType sortType, SearchType searchType, Pageable pageable) {
        JPAQuery<?> query = new JPAQuery<>(entityManager);
        QBoard qBoard = QBoard.board;
        QBoardImage qBoardImage = QBoardImage.boardImage;
        String queryKeyword = "%" + keyword + "%";
        List<BoardListItem.Response> list = query
                .select(Projections.fields(BoardListItem.Response.class,
                                qBoard.title,
                                qBoard.userId,
                                qBoard.boardId,
                                qBoard.createDt,
                                Expressions.asBoolean(qBoardImage.count().intValue().gt(0)).as("isImageContains")
                        )
                )
                .from(qBoard)
                .where(searchType.equals(SearchType.TITLE) ? qBoard.title.like(queryKeyword) : qBoard.content.like(queryKeyword))
                .groupBy(qBoard.boardId)
                .orderBy(sortType.equals(SortType.ASC) ? qBoard.createDt.asc() : qBoard.createDt.desc())
                .leftJoin(qBoardImage).on(qBoard.boardId.eq(qBoardImage.boardId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new RestPage<>(new PageImpl<>(list, pageable, query.fetch().size()));
    }

    public void update(Board board) {
        QBoard qBoard = QBoard.board;
        new JPAUpdateClause(entityManager, qBoard)
                .set(qBoard.title, board.getTitle())
                .set(qBoard.content, board.getContent())
                .set(qBoard.updateDt, board.getUpdateDt())
                .where(qBoard.boardId.eq(board.getBoardId()))
                .execute();
    }
}
