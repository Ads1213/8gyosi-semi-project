package edu.kh.eightgyosi.board.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 게시글 이미지 DTO
 * - board_img 테이블과 1:1 매핑
 * - 게시글 대표 이미지 및 첨부 이미지 관리용
 */
@Data
@NoArgsConstructor   // MyBatis 기본 생성자
@AllArgsConstructor
public class BoardImage {

    /** 이미지 번호 (PK) */
    private int imgNo;

    /** 이미지 저장 경로 */
    private String imgPath;

    /** 이미지 원본 파일명 */
    private String imgOriginalName;

    /** 서버에 저장된 이미지 파일명 (변경된 이름) */
    private String imgRename;

    /** 이미지 순서 (대표 이미지용, 0부터 시작) */
    private int imgOrder;

    /** 게시글 번호 (FK) */
    private int boardId;
}
