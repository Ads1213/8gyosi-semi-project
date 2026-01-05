package edu.kh.eightgyosi.board.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import edu.kh.eightgyosi.board.model.dto.BoardComment;

@Mapper
public interface CommentMapper {

    List<BoardComment> selectCommentList(int boardId);
    int insertComment(BoardComment comment);
    int deleteComment(int commentId);
    BoardComment selectComment(int commentId);
    
	List<BoardComment> select(int boardId);
	int insert(BoardComment comment);
	int delete(int commentNo);
	int update(BoardComment comment);
}