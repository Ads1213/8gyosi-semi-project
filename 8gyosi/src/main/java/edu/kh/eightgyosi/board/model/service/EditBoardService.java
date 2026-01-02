package edu.kh.eightgyosi.board.model.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import edu.kh.eightgyosi.board.model.dto.Board;

public interface EditBoardService {

    /** 게시글 작성 */
    int boardInsert(Board board, List<MultipartFile> images) throws Exception;

    /** 게시글 수정 */
    int boardUpdate(Board board, List<MultipartFile> images, List<Integer> deleteImageList) throws Exception;

    /** 게시글 삭제 */
    int boardDelete(int boardId, int memberNo) throws Exception;

    /** 게시글 상세조회 */
    Board selectBoard(int boardId);

    /** 좋아요 토글 및 최신 좋아요 수 반환 */
    LikeResponse toggleBoardLikeWithCount(int boardId, int memberNo);

    /** 조회수 1 증가 후 최신 조회수 반환 */
    int updateReadCount(int boardId);

    /** 카테고리 리스트 조회 */
    List<String> getCategoryList();

    /** 관리자 전용 게시판 여부 확인 */
    boolean isAdminOnlyCategory(int boardTypeNo);

    /** 좋아요 응답 DTO */
    record LikeResponse(boolean liked, int likeCount, int boardTypeNo) {}
}
