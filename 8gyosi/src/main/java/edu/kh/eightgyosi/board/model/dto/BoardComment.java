package edu.kh.eightgyosi.board.model.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Data
@Builder
public class BoardComment {
    private int commentId;
    private int boardNo;
    private int memberNo;
    private String memberName;
    private String profileImage;
    private String commentContent;
    private LocalDateTime commentWriteDate;
    private boolean commentDelFlg;
    private Integer parentCommentId;
    private List<BoardComment> replies;
}