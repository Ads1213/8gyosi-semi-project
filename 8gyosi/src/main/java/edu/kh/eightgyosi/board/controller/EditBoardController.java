package edu.kh.eightgyosi.board.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.eightgyosi.board.model.dto.Board;
import edu.kh.eightgyosi.board.model.service.EditBoardService;
import edu.kh.eightgyosi.member.model.dto.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/editBoard")
@RequiredArgsConstructor
public class EditBoardController {

    private final EditBoardService service;

    private static final String REDIRECT_BOARD = "redirect:/editBoard/";

    /** ==================== 게시글 작성 화면 ==================== */
    @GetMapping("/{boardTypeNo:[1-6]}/insert")
    public String insertForm(
            @PathVariable("boardTypeNo") int boardTypeNo,
            Model model,
            HttpSession session,
            RedirectAttributes ra) {

        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            ra.addFlashAttribute("message", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        model.addAttribute("boardTypeNo", boardTypeNo);
        model.addAttribute("categoryList", service.getCategoryList());
        return "board/boardWrite";
    }

    /** ==================== 게시글 작성 처리 ==================== */
    @PostMapping("/{boardTypeNo:[1-6]}/insert")
    public String insertBoard(
            @PathVariable("boardTypeNo") int boardTypeNo,
            @ModelAttribute Board board,
            @RequestParam(value="images", required=false) List<org.springframework.web.multipart.MultipartFile> images,
            @RequestParam(value="files", required=false) List<org.springframework.web.multipart.MultipartFile> files,
            HttpSession session,
            RedirectAttributes ra) throws Exception {

        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            ra.addFlashAttribute("message", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        board.setBoardTypeNo(boardTypeNo);
        board.setMemberNo(loginMember.getMemberNo());

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
    public String updateForm(
            @PathVariable(name="boardTypeNo") int boardTypeNo,
            @PathVariable(name="boardId") int boardId,
            @RequestParam(name="cp", required=false, defaultValue="1") int cp,
            HttpSession session,
            Model model,
            RedirectAttributes ra) {

        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            ra.addFlashAttribute("message", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        Board board = service.selectBoard(boardId);
        if (board == null){
            ra.addFlashAttribute("message", "게시글이 존재하지 않습니다");
            return REDIRECT_BOARD + boardTypeNo + "?cp=" + cp;
        }

        if(!hasPermission(board, loginMember)){
            ra.addFlashAttribute("message", "수정 권한이 없습니다.");
            return REDIRECT_BOARD + boardTypeNo + "/" + boardId + "?cp=" + cp;
        }

        board.setBoardImages(service.selectBoardImages(boardId));
        board.setBoardFiles(service.selectBoardFiles(boardId));

        model.addAttribute("board", board);
        model.addAttribute("categoryList", service.getCategoryList());
        model.addAttribute("cp", cp);

        return "board/boardUpdate";
    }

    /** ==================== 게시글 수정 처리 ==================== */
    @PostMapping("/{boardTypeNo:[1-6]}/{boardId:[0-9]+}/update")
    public String updateBoard(
            @PathVariable(name="boardTypeNo") int boardTypeNo,
            @PathVariable(name="boardId") int boardId,
            @ModelAttribute Board board,
            @RequestParam(value="images", required=false) List<org.springframework.web.multipart.MultipartFile> images,
            @RequestParam(value="files", required=false) List<org.springframework.web.multipart.MultipartFile> files,
            @RequestParam(value="deleteImageIds", required=false) String deleteImageStr,
            @RequestParam(value="deleteFileIds", required=false) String deleteFileStr,
            @RequestParam(name="cp", required=false, defaultValue="1") int cp,
            HttpSession session,
            RedirectAttributes ra) throws Exception {

        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            ra.addFlashAttribute("message", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        board.setBoardId(boardId);
        board.setBoardTypeNo(boardTypeNo);
        board.setMemberNo(loginMember.getMemberNo());

        List<Integer> deleteImageList = parseIds(deleteImageStr);
        List<Integer> deleteFileList = parseIds(deleteFileStr);

        int result = service.boardUpdate(board, images, files, deleteImageList, deleteFileList);
        ra.addFlashAttribute("message", result > 0 ? "게시글 수정 완료" : "수정 실패");

        return REDIRECT_BOARD + (result > 0 ? boardTypeNo + "/" + boardId + "?cp=" + cp
                                           : boardTypeNo + "/" + boardId + "/update?cp=" + cp);
    }

    /** ==================== 게시글 삭제 ==================== */
    @PostMapping("/{boardTypeNo:[1-6]}/{boardId:[0-9]+}/delete")
    public String deleteBoard(
            @PathVariable(name="boardTypeNo") int boardTypeNo,
            @PathVariable(name="boardId") int boardId,
            @RequestParam(name="cp", required=false, defaultValue="1") int cp,
            HttpSession session,
            RedirectAttributes ra) throws Exception {

        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            ra.addFlashAttribute("message", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        Board board = service.selectBoard(boardId);
        if(board == null || !hasPermission(board, loginMember)){
            ra.addFlashAttribute("message", "삭제 권한이 없습니다.");
            return REDIRECT_BOARD + boardTypeNo + "/" + boardId + "?cp=" + cp;
        }

        int result = service.boardDelete(boardId, loginMember);
        ra.addFlashAttribute("message", result > 0 ? "게시글 삭제 완료" : "삭제 실패");

        return REDIRECT_BOARD + (result > 0 ? boardTypeNo + "?cp=" + cp
                                           : boardTypeNo + "/" + boardId + "?cp=" + cp);
    }

    /** ==================== Summernote 파일 업로드 ==================== */
    @PostMapping("/uploadFile")
    @ResponseBody
    public String uploadFile(
            @RequestParam(name="file") org.springframework.web.multipart.MultipartFile file,
            HttpSession session) throws Exception {

        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) return "로그인이 필요합니다.";
        return service.uploadFile(file);
    }

    /** ==================== 권한 체크 유틸 ==================== */
    private boolean hasPermission(Board board, Member member){
        if(service.isAdminOnlyCategory(board.getBoardTypeNo()) && member.getRole() != Member.Role.ADMIN){
            return false;
        }
        return board.getMemberNo() == member.getMemberNo() || member.getRole() == Member.Role.ADMIN;
    }

    /** ==================== 문자열 -> 정수 리스트 변환 ==================== */
    private List<Integer> parseIds(String str){
        if(str == null || str.isEmpty()) return null;
        return Arrays.stream(str.split(",")).map(Integer::parseInt).toList();
    }
}