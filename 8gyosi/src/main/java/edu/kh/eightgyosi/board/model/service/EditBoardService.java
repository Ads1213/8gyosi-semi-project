package edu.kh.eightgyosi.board.model.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import edu.kh.eightgyosi.board.model.dto.Board;
import edu.kh.eightgyosi.board.model.dto.BoardFile;
import edu.kh.eightgyosi.board.model.dto.BoardImage;
import edu.kh.eightgyosi.board.model.dto.BoardType;
import edu.kh.eightgyosi.member.model.dto.Member;

public interface EditBoardService {

    int boardInsert(Board board, List<MultipartFile> images, List<MultipartFile> files) throws Exception;

    int boardUpdate(Board board, List<MultipartFile> images, List<MultipartFile> files,
                    List<Integer> deleteImageList, List<Integer> deleteFileList) throws Exception;

    int boardDelete(int boardId, Member loginMember) throws Exception;

    Board selectBoard(int boardId);

    List<BoardImage> selectBoardImages(int boardId);

    List<BoardFile> selectBoardFiles(int boardId);

    String uploadFile(MultipartFile file) throws Exception;

    boolean isAdminOnlyCategory(int boardTypeNo);

    // 수정: Board → BoardType
    List<BoardType> getCategoryList();
}
