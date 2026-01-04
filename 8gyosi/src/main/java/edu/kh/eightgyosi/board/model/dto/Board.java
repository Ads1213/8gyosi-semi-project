package edu.kh.eightgyosi.board.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 게시글 DTO
 * - board 테이블과 1:1 매핑
 * - 첨부 이미지/파일 리스트 포함
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private String boardCreateDate
    ;

    /** 게시글 수정일 */
    private String boardUpdateDate;

    /** 게시판 타입 번호 */
    private int boardTypeNo;

    /** 작성자 회원 번호 */
    private int memberNo;

    /** 첨부 이미지 리스트 */
    private List<BoardImage> boardImages;

    /** 첨부 파일 리스트 */
    private List<BoardFile> boardFiles;
}
