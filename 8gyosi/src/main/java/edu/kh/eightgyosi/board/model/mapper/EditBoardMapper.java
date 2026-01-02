package edu.kh.eightgyosi.board.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import edu.kh.eightgyosi.board.model.dto.*;

@Mapper
public interface EditBoardMapper {

    // 게시글
    int insertBoard(Board board);
    int updateBoard(Board board);
    int deleteBoard(Map<String,Integer> map); // Map: boardId, memberNo
    Board selectBoardDetail(Map<String,Integer> map); // Service에서 사용
    String selectBoardContent(int boardId);

    // 이미지
    int insertBoardImage(BoardImage img);
    BoardImage selectImageByOrder(Map<String,Integer> map); // boardId, imgOrder
    int deleteBoardImage(int imgId);
    int selectMaxImgOrder(int boardId);
    List<BoardImage> selectBoardImages(int boardId);

    // 파일
    int insertBoardFile(BoardFile file);
    BoardFile selectBoardFile(int fileId);
    int deleteBoardFile(int fileId);
    List<BoardFile> selectBoardFiles(int boardId);

    // 좋아요
    BoardLike selectBoardLike(Map<String,Integer> map); // boardId, memberNo
    int insertBoardLike(BoardLike bl);
    int deleteBoardLike(int likeId);
}