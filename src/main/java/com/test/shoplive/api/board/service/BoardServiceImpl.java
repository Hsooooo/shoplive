package com.test.shoplive.api.board.service;

import com.amazonaws.util.StringUtils;
import com.test.shoplive.api.board.entity.Board;
import com.test.shoplive.api.board.entity.BoardImage;
import com.test.shoplive.api.board.enums.SearchType;
import com.test.shoplive.api.board.enums.SortType;
import com.test.shoplive.api.board.repository.BoardCustomRepository;
import com.test.shoplive.api.board.repository.BoardRepository;
import com.test.shoplive.api.board.vo.*;
import com.test.shoplive.api.boardimage.repository.BoardImageCustomRepository;
import com.test.shoplive.api.boardimage.repository.BoardImageRepository;
import com.test.shoplive.api.common.exception.BoardException;
import com.test.shoplive.api.common.exception.ErrorCode;
import com.test.shoplive.aws.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardCustomRepository boardCustomRepository;
    private final BoardImageRepository boardImageRepository;
    private final BoardImageCustomRepository boardImageCustomRepository;
    private final ModelMapper modelMapper;
    private final CacheManager cacheManager;

    private final S3Service s3Service;
    @CacheEvict(cacheNames = "boardList", allEntries = true)
    @Override
    @Transactional
    @CachePut(cacheNames = "userBoard", key = "#boardVO.userId")
    public Board addBoard(BoardAdd.Request boardVO, List<MultipartFile> files) throws Exception {
        if (files != null && files.size() > 2) {
            throw new BoardException(ErrorCode.Board.B003);
        }
        if (isAbuseWriteBoard(boardVO.getUserId())) {
            throw new BoardException(ErrorCode.Board.B001);
        }
        Board boardEntity = modelMapper.map(boardVO, Board.class);
        boardEntity.setCreateDt(LocalDateTime.now());
        Board board = boardRepository.save(boardEntity);
        if (files != null && !StringUtils.isNullOrEmpty(files.get(0).getOriginalFilename())) {
            saveBoardImages(board.getBoardId(), files);
        }
        return board;
    }
    @Override
    @Cacheable(cacheNames = "boardDetail", key = "#boardId")
    public BoardDetailItem.Response getBoard(Long boardId) throws Exception {
        Board board = boardRepository.findById(boardId).orElseThrow(NoSuchElementException::new);
        List<BoardImage> imageList = boardImageCustomRepository.listImageByBoardId(boardId);
        List<BoardImageItem> imageItemList = new ArrayList<>();
        for (BoardImage image : imageList) {
            imageItemList.add(new BoardImageItem(image.getOriginFilename(), image.getImageIndex(), image.getUrl()));
        }
        return new BoardDetailItem.Response(board.getTitle(), board.getContent(), board.getUserId(), board.getCreateDt(), imageItemList);
    }

    @Override
    @Cacheable(cacheNames = "boardList", key = "#keyword + #searchType + #sortType + #pageable.pageNumber")
    public Page<BoardListItem.Response> getBoardList(String keyword, SearchType searchType, SortType sortType, Pageable pageable) {
        return boardCustomRepository.findBoardBySearch(keyword, sortType, searchType, pageable);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"boardList", "boardDetail"}, allEntries = true, key = "#boardId")
    public void deleteBoard(Long boardId, String userId) throws Exception{
        Board board = boardRepository.findById(boardId).orElseThrow(NoSuchElementException::new);
        if (!board.getUserId().equals(userId)) {
            throw new BoardException(ErrorCode.Board.B002);
        }
        boardRepository.deleteById(boardId);
        List<BoardImage> boardImageList = boardImageCustomRepository.findBoardImageByBoardId(boardId);
        List<String> deleteImageUrlList = boardImageList.stream().map(BoardImage::getUrl).collect(Collectors.toList());
        this.deleteImageS3ByUrlList(deleteImageUrlList);
        boardImageCustomRepository.deleteBoardImageByBoardId(boardId);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"boardList", "boardDetail"}, allEntries = true, key = "#boardId")
    public void modifyBoard(Long boardId, BoardModify.Request request, List<MultipartFile> files) throws Exception {
        BoardDetailItem.Response boardDetail = this.getBoard(boardId);

        if (!boardDetail.getUserId().equals(request.getUserId())) {
            throw new BoardException(ErrorCode.Board.B002);
        }

        this.modifyBoardImageData(boardId, request, files, boardDetail);

        Board board = new Board();
        board.setBoardId(boardId);
        board.setContent(request.getContent());
        board.setTitle(request.getTitle());
        board.setUpdateDt(LocalDateTime.now());
        boardCustomRepository.update(board);
    }

    private void modifyBoardImageData(Long boardId, BoardModify.Request request, List<MultipartFile> files, BoardDetailItem.Response boardDetail) throws Exception {
        if (files != null && request.getBoardImageItemList().size() + files.size() > 2) {
            throw new BoardException(ErrorCode.Board.B003);
        } else if (request.getBoardImageItemList().size() != boardDetail.getBoardImageItemList().size()) {
            List<String> remainImageUrlList = request.getBoardImageItemList()
                    .stream()
                    .map(BoardImageItem::getUrl)
                    .collect(Collectors.toList());
            List<String> deleteImageUrlList = boardImageRepository.findDeleteTargetImageUrlList(remainImageUrlList);

            this.deleteImageS3ByUrlList(deleteImageUrlList);
            boardImageCustomRepository.deleteBoardImageByUrlList(deleteImageUrlList);
        } else if (request.getBoardImageItemList().size() == 0) {
            boardImageCustomRepository.deleteBoardImageByBoardId(boardId);
            List<String> deleteImageUrlList = boardDetail.getBoardImageItemList()
                    .stream()
                    .map(BoardImageItem::getUrl)
                    .collect(Collectors.toList());
            this.deleteImageS3ByUrlList(deleteImageUrlList);
        }
        if (files != null && !StringUtils.isNullOrEmpty(files.get(0).getOriginalFilename())) {
            this.saveBoardImages(boardId, files);
        }
    }

    private void deleteImageS3ByUrlList(List<String> deleteImageUrlList) throws IOException {
        for (String url : deleteImageUrlList) {
            String[] splitUrl = url.split("/");
            String objectName = splitUrl[splitUrl.length - 1];
            s3Service.delete(objectName);
        }
    }

    private void saveBoardImages(Long boardId, List<MultipartFile> files) throws Exception {
        filesLoop:
        for (MultipartFile file : files) {

            String contentType = file.getContentType();
            String originalExt;
            if (ObjectUtils.isEmpty(contentType)) {
                break;
            } else {
                switch (contentType) {
                    case "image/jpeg":
                        originalExt = ".jpg";
                        break;
                    case "image/png":
                        originalExt = ".png";
                        break;
                    case "image/gif":
                        originalExt = ".gif";
                        break;
                    default:
                        break filesLoop;
                }
                String newFileName = System.nanoTime() + originalExt;
                String url = s3Service.upload(file, newFileName);

                BoardImage image = new BoardImage();
                image.setBoardId(boardId);
                image.setOriginFilename(file.getOriginalFilename());
                image.setUrl(url);
                image.setCreateDt(LocalDateTime.now());

                boardImageRepository.saveImage(image);
            }

        }
    }
    private boolean isAbuseWriteBoard(String userId) {
        Cache boardCache = cacheManager.getCache("userBoard");
        if (boardCache == null){
            return false;
        } else {
            Board board = boardCache.get(userId, Board.class);
            if (board == null) {
                return false;
            } else {
                return board.getUserId().equals(userId);
            }
        }
    }
}
