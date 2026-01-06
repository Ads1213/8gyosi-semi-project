package edu.kh.eightgyosi.board.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.eightgyosi.board.model.dto.Board;
import edu.kh.eightgyosi.board.model.dto.BoardFile;
import edu.kh.eightgyosi.board.model.dto.BoardImage;
import edu.kh.eightgyosi.board.model.service.EditBoardService;
import edu.kh.eightgyosi.member.model.dto.Member;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/editBoard")
@RequiredArgsConstructor
public class EditBoardController {

    private final EditBoardService service;
    private static final String REDIRECT_BOARD = "redirect:/editBoard/";

    /** ==================== 게시글 작성 화면 ==================== */
    @GetMapping("/{boardTypeNo:[1-6]}/insert")
    public String insertForm(@PathVariable("boardTypeNo") int boardTypeNo, Model model) {
        model.addAttribute("boardTypeNo", boardTypeNo);
        model.addAttribute("categoryList", service.getCategoryList());
        return "board/boardWrite";
    }

    /** ==================== 게시글 작성 처리 ==================== */
    @PostMapping("/{boardTypeNo:[1-6]}/insert")
    public String insertBoard(@PathVariable("boardTypeNo") int boardTypeNo,
                              @ModelAttribute Board board,
                              @SessionAttribute("loginMember") Member loginMember,
                              @RequestParam(value="images", required=false) List<org.springframework.web.multipart.MultipartFile> images,
                              @RequestParam(value="files", required=false) List<org.springframework.web.multipart.MultipartFile> files,
                              RedirectAttributes ra) throws Exception {

        // 작성자 번호, 게시판 타입 세팅
        board.setBoardTypeNo(boardTypeNo);
        board.setMemberNo(loginMember.getMemberNo());

        // 관리자 전용 게시판이면 일반회원 접근 차단
        if(service.isAdminOnlyCategory(boardTypeNo) && loginMember.getRole() != Member.Role.ADMIN){
            ra.addFlashAttribute("message", "공지사항은 관리자만 작성 가능합니다.");
            return REDIRECT_BOARD + boardTypeNo;
        }

        int boardId = service.boardInsert(board, images, files);
        ra.addFlashAttribute("message", boardId > 0 ? "게시글 작성 완료" : "작성 실패");
        return REDIRECT_BOARD + (boardId > 0 ? boardTypeNo + "/" + boardId : boardTypeNo + "/insert");
    }

    /** ==================== 게시글 수정 화면 ==================== */
    @GetMapping("/{boardTypeNo:[1-6]}/{boardId:[0-9]+}/update")
    public String updateForm(@PathVariable int boardTypeNo,
                             @PathVariable int boardId,
                             @SessionAttribute("loginMember") Member loginMember,
                             Model model,
                             RedirectAttributes ra) {

        Board board = service.selectBoard(boardId);
        if(board == null){
            ra.addFlashAttribute("message", "게시글이 존재하지 않습니다");
            return REDIRECT_BOARD + boardTypeNo;
        }

        // 작성자 또는 ADMIN 권한 체크
        if(!hasPermission(board, loginMember)){
            ra.addFlashAttribute("message", "수정 권한이 없습니다.");
            return REDIRECT_BOARD + boardTypeNo + "/" + boardId;
        }

        board.setBoardImages(service.selectBoardImages(boardId));
        board.setBoardFiles(service.selectBoardFiles(boardId));

        model.addAttribute("board", board);
        model.addAttribute("categoryList", service.getCategoryList());
        return "board/boardUpdate";
    }

    /** ==================== 게시글 수정 처리 ==================== */
    @PostMapping("/{boardTypeNo:[1-6]}/{boardId:[0-9]+}/update")
    public String updateBoard(@PathVariable int boardTypeNo,
                              @PathVariable int boardId,
                              @ModelAttribute Board board,
                              @RequestParam(value="images", required=false) List<org.springframework.web.multipart.MultipartFile> images,
                              @RequestParam(value="files", required=false) List<org.springframework.web.multipart.MultipartFile> files,
                              @RequestParam(value="deleteImageIds", required=false) String deleteImageStr,
                              @RequestParam(value="deleteFileIds", required=false) String deleteFileStr,
                              @SessionAttribute("loginMember") Member loginMember,
                              RedirectAttributes ra) throws Exception {

        // 게시글 ID, 작성자, 게시판 타입 세팅
        board.setBoardId(boardId);
        board.setBoardTypeNo(boardTypeNo);
        board.setMemberNo(loginMember.getMemberNo());

        // 삭제할 이미지/파일 리스트 변환
        List<Integer> deleteImageList = parseIds(deleteImageStr);
        List<Integer> deleteFileList = parseIds(deleteFileStr);

        int result = service.boardUpdate(board, images, files, deleteImageList, deleteFileList);
        ra.addFlashAttribute("message", result > 0 ? "게시글 수정 완료" : "수정 실패");
        return REDIRECT_BOARD + (result > 0 ? boardTypeNo + "/" + boardId : boardTypeNo + "/" + boardId + "/update");
    }

    /** ==================== 게시글 삭제 ==================== */
    @PostMapping("/{boardTypeNo:[1-6]}/{boardId:[0-9]+}/delete")
    public String deleteBoard(@PathVariable int boardTypeNo,
                              @PathVariable int boardId,
                              @SessionAttribute("loginMember") Member loginMember,
                              RedirectAttributes ra) throws Exception {

        Board board = service.selectBoard(boardId);

        // 권한 없는 경우
        if(board == null || !hasPermission(board, loginMember)){
            ra.addFlashAttribute("message", "삭제 권한이 없습니다.");
            return REDIRECT_BOARD + boardTypeNo + "/" + boardId;
        }

        int result = service.boardDelete(boardId, loginMember);
        ra.addFlashAttribute("message", result > 0 ? "게시글 삭제 완료" : "삭제 실패");
        return REDIRECT_BOARD + (result > 0 ? boardTypeNo : boardTypeNo + "/" + boardId);
    }

    /** ==================== Summernote 파일 업로드 ==================== */
    @PostMapping("/uploadFile")
    @ResponseBody
    public String uploadFile(@RequestParam("file") org.springframework.web.multipart.MultipartFile file) throws Exception {
        return service.uploadFile(file);
    }

    /** ==================== 권한 체크 유틸 ==================== */
    private boolean hasPermission(Board board, Member member){
        // 관리자 전용 게시판이면 일반회원 접근 차단
        if(service.isAdminOnlyCategory(board.getBoardTypeNo()) && member.getRole() != Member.Role.ADMIN){
            return false;
        }
        // 작성자 또는 ADMIN만 권한 있음
        return board.getMemberNo() == member.getMemberNo() || member.getRole() == Member.Role.ADMIN;
    }

    /** ==================== 문자열 -> 정수 리스트 변환 ==================== */
    private List<Integer> parseIds(String str){
        if(str == null || str.isEmpty()) return null;
        return Arrays.stream(str.split(",")).map(Integer::parseInt).toList();
    }
}

