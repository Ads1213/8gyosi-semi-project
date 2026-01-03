package edu.kh.eightgyosi.board.model.dto;

import java.time.LocalDateTime;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 게시판 게시글 DTO
 * - board 테이블과 1:1 매핑
 * - MyBatis에서 결과 매핑 및 파라미터 전달용 객체
 */
@Data
@NoArgsConstructor   // MyBatis, Jackson에서 기본 생성자 필요
@AllArgsConstructor  // 테스트 및 객체 생성 편의
public class Board {

    /** 게시글 번호 (PK) */
    private int boardId;

    /** 게시글 제목 */
    private String boardTitle;

    /** 게시글 내용 */
    private String boardContent;

    /** 게시글 조회수 */
    private int boardViewCount;

    /** 게시글 삭제 여부 (Y/N) */
    private String boardIsDeleted;

    /** 게시글 작성일 */
    private LocalDateTime boardCreate;

    /** 게시글 수정일 */
    private LocalDateTime boardUpdated;

    /** 게시판 타입 번호 */
    private int boardTypeNo;

    /** 작성자 회원 번호 (FK) */
    private int memberNo;
}
