package edu.kh.eightgyosi.board.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.eightgyosi.board.model.dto.Board;
import edu.kh.eightgyosi.board.model.dto.BoardComment;
import edu.kh.eightgyosi.board.model.dto.BoardFile;
import edu.kh.eightgyosi.board.model.dto.BoardImage;
import edu.kh.eightgyosi.board.model.dto.BoardLike;

@Mapper
public interface EditBoardMapper {
    int insertBoard(Board board);
    int updateBoard(Board board);
    int deleteBoard(Map<String,Integer> map);
    Board selectOneBoard(int boardNo);
    String selectBoardContent(int boardNo);

    // 이미지
    int insertBoardImage(BoardImage img);
    BoardImage selectImageByOrder(Map<String,Integer> map);
    int deleteBoardImage(int imgNo);
    int selectMaxImgOrder(int boardNo);
    List<BoardImage> selectBoardImages(int boardNo);

    // 파일
    int insertBoardFile(BoardFile file);
    BoardFile selectBoardFile(int fileNo);
    int deleteBoardFile(int fileNo);
    List<BoardFile> selectBoardFiles(int boardNo);

    // 댓글
    int insertBoardComment(BoardComment comment);
    BoardComment selectBoardComment(int commentId);
    int deleteBoardComment(int commentId);
    int deleteBoardComments(int boardNo);

    // 좋아요/싫어요
    BoardLike selectBoardLike(int boardNo, int memberNo);
    int insertBoardLike(BoardLike bl);
    int updateBoardLike(int likeNo, boolean likeFlg);
    int deleteBoardLike(int likeNo);
}