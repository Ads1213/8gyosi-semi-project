package edu.kh.eightgyosi.board.model.dto;

import java.time.LocalDateTime;
import lombok.*;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 게시글 첨부파일 DTO
 * - 업로드 파일 정보 저장용
 * - board 테이블과 FK(boardId)로 연결
 */
@Data
@NoArgsConstructor   // MyBatis 기본 생성자
@AllArgsConstructor
public class UploadFile {

    /** 업로드 파일 번호 (PK) */
    private int uploadfileNo;

    /** 업로드 파일 원본명 */
    private String uploadfileOrigin;

    /** 파일 저장 경로 */
    private String uploadfilePath;

    /** 서버에 저장된 파일명 (변경된 이름) */
    private String uploadfileStrg;

    /** 파일 크기 (byte 단위) */
    private long uploadfileSize;

    /** 파일 업로드 날짜 */
    private LocalDateTime uploadfileDate;

    /** 게시글 번호 (FK) */
    private int boardId;
}
