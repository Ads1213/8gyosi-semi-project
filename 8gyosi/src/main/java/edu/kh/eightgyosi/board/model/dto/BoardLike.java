package edu.kh.eightgyosi.board.model.dto;

import lombok.*;

@Data
@Builder
public class BoardLike {
    private int likeNo;
    private int boardNo;
    private int memberNo;
    private boolean likeFlg; // true = like, false = hate
}