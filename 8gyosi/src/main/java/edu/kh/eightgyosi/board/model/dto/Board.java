package edu.kh.eightgyosi.board.model.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    /** 게시글 PK */
    private int boardId;

    /** 게시판 종류 번호 */
    private int boardTypeNo;

    /** 게시판 종류 이름 */
    private String boardTypeName;

    /** 작성자 회원 번호 */
    private int memberNo;

    /** 작성자 닉네임 */
    private String memberNickname;

    /** 제목 */
    private String boardTitle;

    /** 내용 */
    private String boardContent;

    /** 작성일 */
    private LocalDateTime createDate;

    /** 삭제 여부 (Y/N) */
    private String boardDelFl;

    /** 조회수 */
    private int boardViewCount;

    /** 댓글 수 */
    private int boardCommentCount;

    /** 좋아요 수 */
    private int boardLikeCount;

    /** 이미지 목록 */
    private List<BoardImage> boardImages;

    /** 파일 목록 */
    private List<BoardFile> boardFiles;

    /** 댓글 목록 */
    private List<BoardComment> boardComments;
}