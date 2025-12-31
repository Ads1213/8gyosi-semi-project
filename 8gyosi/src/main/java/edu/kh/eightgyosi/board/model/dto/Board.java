package edu.kh.eightgyosi.board.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {
    private int boardNo;
    private int boardId; // 게시판 종류
    private int memberNo;
    private String boardTitle;
    private String boardContent;
    private LocalDateTime createDate;
    private boolean boardIsDeleted;
    private int boardViewCount;
    private int boardCommentCount;

    private int boardTypeNo;
    private String boardTypeName;

    private List<BoardImage> boardImages;
    private List<BoardFile> boardFiles;
    private List<BoardComment> boardComments;
}