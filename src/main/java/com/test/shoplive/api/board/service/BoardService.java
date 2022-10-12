package com.test.shoplive.api.board.service;

import com.test.shoplive.api.board.entity.Board;
import com.test.shoplive.api.board.enums.SearchType;
import com.test.shoplive.api.board.enums.SortType;
import com.test.shoplive.api.board.vo.BoardAdd;
import com.test.shoplive.api.board.vo.BoardDetailItem;
import com.test.shoplive.api.board.vo.BoardListItem;
import com.test.shoplive.api.board.vo.BoardModify;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {
    Board addBoard(BoardAdd.Request board, List<MultipartFile> files) throws Exception;

    BoardDetailItem.Response getBoard(Long boardId) throws Exception;

    Page<BoardListItem.Response> getBoardList(String keyword, SearchType searchType, SortType sorttype, Pageable pageable);

    void deleteBoard(Long boardId, String userId) throws Exception;

    void modifyBoard(Long boardId, BoardModify.Request request, List<MultipartFile> files) throws Exception;
}
