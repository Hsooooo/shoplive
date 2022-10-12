package com.test.shoplive.api.board.controller;

import com.test.shoplive.api.board.service.BoardServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BoardController.class)
@AutoConfigureRestDocs
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BoardControllerTest {

//    private static final String BASE_URL = "/board";
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private BoardServiceImpl boardService;
//
//
//    @Test
//    @DisplayName("게시판 목록 조회")
//    void getBoardList() throws Exception {
//        final String boardListResponseJson = getJsonFrom(BASE_URL + "/" + "get-board-list-response.json");
//    }
//
//    private String getJsonFrom(String path) throws Exception {
//        return jsonConverter.fromJson(MOCK_DATA_RESOURCE_PATH + path);
//    }

}
