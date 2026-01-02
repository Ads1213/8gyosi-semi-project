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
    private int boardId; // 게시글 번호(board table의 PK)
    private String boardTitle;
    private String boardContent;
    private int boardViewCount;
    private boolean boardIsDeleted;
    private String boardCreateDate;
    private String boardUpdateDate;
    
    private int boardCommentCount;
    
    private int memberNo;
    
    private int boardTypeNo;
    private String boardTypeName;

    private List<BoardImage> boardImages;
    private List<BoardFile> boardFiles;
    private List<BoardComment> boardComments;
}