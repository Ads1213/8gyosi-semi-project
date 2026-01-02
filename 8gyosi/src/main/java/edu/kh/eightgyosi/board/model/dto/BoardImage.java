package edu.kh.eightgyosi.board.model.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardImage {

    /** 이미지 PK */
    private int imgId;

    /** 게시글 FK */
    private int boardId;

    /** 이미지 순서 */
    private int imgOrder;

    /** 서버 저장 파일명 */
    private String imgStoredName;

    /** 이미지 경로 */
    private String imgPath;
}