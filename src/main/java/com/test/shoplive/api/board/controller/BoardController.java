package com.test.shoplive.api.board.controller;

import com.test.shoplive.api.board.entity.Board;
import com.test.shoplive.api.board.enums.SearchType;
import com.test.shoplive.api.board.enums.SortType;
import com.test.shoplive.api.board.service.BoardService;
import com.test.shoplive.api.board.vo.BoardAdd;
import com.test.shoplive.api.board.vo.BoardModify;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/board")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createBoard(
            @Valid @RequestPart BoardAdd.Request board,
            @RequestPart(required = false) List<MultipartFile> files) throws Exception {
        Board boardEntity = boardService.addBoard(board, files);

        URI uriLocation = new URI("/board/" + boardEntity.getBoardId());
        return ResponseEntity.created(uriLocation).body("{}");
    }

    @GetMapping
    public ResponseEntity<?> getBoardList(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "CONTENT") SearchType searchType,
            @RequestParam(required = false, defaultValue = "DESC") SortType sortType,
            @PageableDefault(sort = "create_dt", direction = Sort.Direction.DESC) Pageable pageable
    ) throws Exception {
        return ResponseEntity.ok(boardService.getBoardList(keyword, searchType, sortType, pageable));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable Long boardId) throws Exception {
        return ResponseEntity.ok(boardService.getBoard(boardId));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId, @RequestParam String userId) throws Exception {
        boardService.deleteBoard(boardId, userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<?> putBoard(@PathVariable Long boardId,
                                      @RequestPart BoardModify.Request request,
                                      @RequestPart(required = false) List<MultipartFile> files
    ) throws Exception {
        boardService.modifyBoard(boardId, request, files);
        return ResponseEntity.noContent().build();
    }


}
