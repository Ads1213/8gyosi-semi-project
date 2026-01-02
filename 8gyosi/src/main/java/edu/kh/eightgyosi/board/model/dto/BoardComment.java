package edu.kh.eightgyosi.board.model.dto;

import java.time.LocalDateTime;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardComment {

    /** 댓글 PK */
    private int commentId;

    /** 게시글 FK */
    private int boardId;

    /** 작성자 회원 번호 */
    private int memberNo;

    /** 작성자 닉네임 */
    private String memberNickname;

    /** 댓글 내용 */
    private String commentContent;

    /** 작성일 */
    private LocalDateTime createDate;

    /** 삭제 여부 (Y/N) */
    private String commentDelFl;
    
    private int parentCommentNo;
}
