package com.test.shoplive.api.board.service;

import com.test.shoplive.api.board.entity.Board;
import com.test.shoplive.api.board.repository.BoardRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback(false)
public class BoardServiceTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void saveBoardTest() {
        Board board = new Board();
        board.setTitle("테스트입니다.");
        board.setContent("dsafjkadslfjklasdfjklads;jfkl;asdjfkl");
        board.setUserId("rkrk6469");

        boardRepository.save(board);

        Board retrivedBoard = boardRepository.findById(board.getBoardId()).get();

        Assert.assertEquals(retrivedBoard.getTitle(), "테스트입니다.");
        Assert.assertEquals(retrivedBoard.getContent(), "dsafjkadslfjklasdfjklads;jfkl;asdjfkl");
        Assert.assertEquals(retrivedBoard.getUserId(), "rkrk6469");
    }
}
