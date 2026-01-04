package edu.kh.eightgyosi.board.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.eightgyosi.board.model.dto.*;

@Mapper
public interface EditBoardMapper {

    // ===================== 게시글 =====================
    int insertBoard(Board board);
    int updateBoard(Board board);
    int deleteBoard(Map<String, Integer> map);
    Board selectBoardDetail(Map<String, Integer> map);

    // ===================== 이미지 =====================
    int insertBoardImage(BoardImage img);
    BoardImage selectBoardImageById(int imgNo);
    int deleteBoardImage(int imgNo);
    int selectMaxImgOrder(int boardId);
    List<BoardImage> selectBoardImages(int boardId);

    // ===================== 파일 =====================
    int insertBoardFile(BoardFile file);
    BoardFile selectBoardFile(int uploadfileNo);
    int deleteBoardFile(int uploadfileNo);
    List<BoardFile> selectBoardFiles(int boardId);

    // ===================== 카테고리 =====================
    List<Board> selectCategoryList();
}
