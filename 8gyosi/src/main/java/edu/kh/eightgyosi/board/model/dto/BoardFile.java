package edu.kh.eightgyosi.board.model.dto;

import java.time.LocalDateTime;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardFile {

    /** 파일 PK */
    private int fileId;

    /** 게시글 FK */
    private int boardId;

    /** 원본 파일명 */
    private String fileOriginName;

    /** 서버 저장 파일명 */
    private String fileStoredName;

    /** 파일 경로 */
    private String filePath;

    /** 파일 크기 */
    private long fileSize;

    /** 업로드 일시 */
    private LocalDateTime uploadDate;
}
