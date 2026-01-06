package edu.kh.eightgyosi.board.model.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 게시글 이미지 DTO
 * - board_image 테이블과 매핑
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardImage {

    /** 이미지 번호 (PK) */
    private int imgNo;

    /** 이미지 경로 */
    private String imgPath;

    /** 원본 이미지 파일명 */
    private String imgOriginalName;

    /** 서버 저장명 */
    private String imgRename;

    /** 이미지 순서 */
    private int imgOrder;

    /** 게시글 번호 (FK) */
    private int boardId;

    /** 이미지 파일 크기 */
    private long imgSize;
    
    /** 게시글 이미지 삽입/수정 할 때 사용 */
    private MultipartFile uploadFile;
    
    /** 서버 저장 파일명 */
    private String imgStoredName;
    
    
}
