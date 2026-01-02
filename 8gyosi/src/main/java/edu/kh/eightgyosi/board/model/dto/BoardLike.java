package edu.kh.eightgyosi.board.model.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardLike {

    /** 좋아요 PK */
    private int likeId;

    /** 게시글 FK */
    private int boardId;

    /** 회원 번호 */
    private int memberNo;
}