/*
 * package edu.kh.eightgyosi.board.model.service;
 * 
 * import java.util.List;
 * 
 * import org.springframework.web.multipart.MultipartFile;
 * 
 * import edu.kh.eightgyosi.board.model.dto.Board; import
 * edu.kh.eightgyosi.board.model.dto.BoardComment; import
 * edu.kh.eightgyosi.model.dto.Member;
 * 
 * public interface EditBoardService {
 * 
 * int boardInsert(Board board, List<MultipartFile> images, List<MultipartFile>
 * files) throws Exception;
 * 
 * int boardUpdate(Board board, List<MultipartFile> images, List<MultipartFile>
 * files, List<Integer> deleteImageList, List<Integer> deleteFileList) throws
 * Exception;
 * 
 * int boardDelete(int boardNo, Member loginMember) throws Exception;
 * 
 * Board selectBoard(int boardNo);
 * 
 * String uploadFile(MultipartFile file) throws Exception;
 * 
 * int insertComment(BoardComment comment);
 * 
 * int deleteComment(int commentId, Member loginMember);
 * 
 * int toggleBoardLike(int boardNo, int memberNo, boolean isLike); }
 */	