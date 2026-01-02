package edu.kh.eightgyosi.board.model.service;

import java.util.List;
import edu.kh.eightgyosi.board.model.dto.BoardComment;
import edu.kh.eightgyosi.board.model.dto.Member;

public interface CommentService {

    /** 댓글/대댓글 목록 조회 (게시글별) */
    List<BoardComment> selectCommentList(int boardId);

    /** 댓글/대댓글 작성 */
    int insertComment(BoardComment comment);

    /** 댓글/대댓글 삭제 */
    int deleteComment(int commentId, Member loginMember);
}
