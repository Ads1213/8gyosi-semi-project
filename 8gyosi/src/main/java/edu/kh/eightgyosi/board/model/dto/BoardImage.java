package edu.kh.eightgyosi.board.model.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardImage {
    private int imgNo;
    private int boardNo;
    private String imgOriginName;
    private String imgStoredName;
    private int imgOrder;
    private LocalDateTime uploadDate;
}