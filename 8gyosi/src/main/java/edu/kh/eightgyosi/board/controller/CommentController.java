package edu.kh.eightgyosi.board.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import edu.kh.eightgyosi.board.model.dto.BoardComment;
import edu.kh.eightgyosi.board.model.dto.Member;
import edu.kh.eightgyosi.board.model.service.CommentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/editBoard/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService service;

    /** ===================== 댓글/대댓글 목록 조회 ===================== */
    @GetMapping("/{boardId}")
    public ResponseEntity<List<BoardComment>> getCommentList(@PathVariable int boardId) {
        List<BoardComment> commentList = service.selectCommentList(boardId);
        return ResponseEntity.ok(commentList);
    }

    /** ===================== 댓글/대댓글 작성 ===================== */
    @PostMapping("/{boardId}")
    public ResponseEntity<Map<String,Object>> insertComment(@PathVariable int boardId,
                                                            @RequestBody BoardComment comment,
                                                            @SessionAttribute("loginMember") Member loginMember) {
        // 로그인한 회원 정보 세팅
        comment.setMemberNo(loginMember.getMemberNo());
        comment.setBoardId(boardId);

        int result = service.insertComment(comment);
        return ResponseEntity.ok(
            Map.of("success", result > 0,
                   "message", result > 0 ? "댓글 작성 완료" : "작성 실패")
        );
    }

    /** ===================== 댓글/대댓글 삭제 ===================== */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Map<String,Object>> deleteComment(@PathVariable int commentId,
                                                            @RequestParam int boardId,
                                                            @SessionAttribute("loginMember") Member loginMember) {

        int result = service.deleteComment(commentId, loginMember);
        List<BoardComment> commentList = service.selectCommentList(boardId); // 삭제 후 댓글 목록 반환

        return ResponseEntity.status(result > 0 ? HttpStatus.OK : HttpStatus.FORBIDDEN)
                .body(Map.of(
                        "success", result > 0,
                        "message", result > 0 ? "댓글 삭제 완료" : "권한 없음 또는 댓글이 존재하지 않습니다.",
                        "commentList", commentList
                ));
    }
}
