package edu.kh.eightgyosi.board.model.dto;

import java.time.LocalDateTime;
import lombok.*;

@Data
@Builder
public class BoardFile {
    private int fileNo;
    private int boardNo;
    private String fileOriginName;
    private String fileStoredName;
    private long fileSize;
    private LocalDateTime uploadDate;
}