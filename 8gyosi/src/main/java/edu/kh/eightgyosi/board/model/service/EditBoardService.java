package edu.kh.eightgyosi.board.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.eightgyosi.board.model.dto.Board;
import edu.kh.eightgyosi.board.model.dto.BoardFile;
import edu.kh.eightgyosi.board.model.dto.BoardImage;
import edu.kh.eightgyosi.member.model.dto.Member;

/**
 * EditBoardService
 * - 게시글 CRUD 및 이미지/파일 업로드 처리
 * - Controller와 Mapper 간의 중간 역할
 */
public interface EditBoardService {

    /** 게시글 작성 */
    int boardInsert(Board board, List<MultipartFile> images, List<MultipartFile> files) throws Exception;

    /** 게시글 수정 */
    int boardUpdate(Board board, List<MultipartFile> images, List<MultipartFile> files,
                    List<Integer> deleteImageList, List<Integer> deleteFileList) throws Exception;

    /** 게시글 삭제 */
    int boardDelete(int boardId, Member loginMember) throws Exception;

    /** 단일 게시글 조회 */
    Board selectBoard(int boardId);

    /** 게시글 이미지 리스트 조회 */
    List<BoardImage> selectBoardImages(int boardId);

    /** 게시글 파일 리스트 조회 */
    List<BoardFile> selectBoardFiles(int boardId);


    /** 파일 업로드 처리 */
    String uploadFile(MultipartFile file) throws Exception;

    /** 관리자 전용 카테고리 체크 */
    boolean isAdminOnlyCategory(int boardTypeNo);

    /** 게시판 카테고리 조회 */
    List<Board> getCategoryList();
}
