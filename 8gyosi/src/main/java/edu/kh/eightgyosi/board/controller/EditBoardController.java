package edu.kh.eightgyosi.board.controller;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import edu.kh.eightgyosi.board.model.dto.Board;
import edu.kh.eightgyosi.board.model.dto.BoardFile;
import edu.kh.eightgyosi.board.model.service.EditBoardService;
import edu.kh.eightgyosi.member.model.dto.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/editBoard")
@RequiredArgsConstructor
public class EditBoardController {

    private final EditBoardService service;

    /* ==================== 게시글 작성 화면 ==================== */
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

    /* ==================== 게시글 작성 처리 ==================== */
    @PostMapping("/{boardTypeNo:[1-6]}/insert")
    public String insertBoard(
            @PathVariable("boardTypeNo") int boardTypeNo,
            @ModelAttribute Board board,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            HttpSession session,
            RedirectAttributes ra) throws Exception {

        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            ra.addFlashAttribute("message", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        board.setBoardTypeNo(boardTypeNo);
        board.setMemberNo(loginMember.getMemberNo());

        if (service.isAdminOnlyCategory(boardTypeNo)
                && loginMember.getRole() != Member.Role.ADMIN) {
            ra.addFlashAttribute("message", "공지사항은 관리자만 작성 가능합니다.");
            return "redirect:/board/" + boardTypeNo;
        }

        int boardId = service.boardInsert(board, images, files);

        ra.addFlashAttribute("message",
                boardId > 0 ? "게시글 작성 완료" : "게시글 작성 실패");

        return boardId > 0
                ? "redirect:/board/" + boardTypeNo + "/" + boardId
                : "redirect:/editBoard/" + boardTypeNo + "/insert";
    }

    /* ==================== 게시글 수정 화면 ==================== */
    @GetMapping("/{boardTypeNo:[1-6]}/{boardId:[0-9]+}/update")
    public String updateForm(
            @PathVariable("boardTypeNo") int boardTypeNo,
            @PathVariable("boardId") int boardId,
            @RequestParam(name = "cp", defaultValue = "1") int cp,
            HttpSession session,
            Model model,
            RedirectAttributes ra) {

        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            ra.addFlashAttribute("message", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        Board board = service.selectBoard(boardId);
        if (board == null) {
            ra.addFlashAttribute("message", "게시글이 존재하지 않습니다.");
            return "redirect:/board/" + boardTypeNo + "?cp=" + cp;
        }

        if (!hasPermission(board, loginMember)) {
            ra.addFlashAttribute("message", "수정 권한이 없습니다.");
            return "redirect:/board/" + boardTypeNo + "/" + boardId + "?cp=" + cp;
        }

        // ==================== null-safe 처리 ====================
        // 서비스에서 항상 빈 리스트 반환하도록 개선 → null 체크 제거 가능
        
        
        board.setBoardImages(service.selectBoardImages(boardId));
        board.setBoardFiles(service.selectBoardFiles(boardId));
        // ================================================

        model.addAttribute("board", board);
        model.addAttribute("categoryList", service.getCategoryList());
        model.addAttribute("cp", cp);

        return "board/boardUpdate";
    }

    /* ==================== 게시글 수정 처리 ==================== */
    @PostMapping("/{boardTypeNo:[1-6]}/{boardId:[0-9]+}/update")
    public String updateBoard(
            @PathVariable("boardTypeNo") int boardTypeNo,
            @PathVariable("boardId") int boardId,
            @ModelAttribute Board board,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            @RequestParam(value = "deleteImageIds", required = false) String deleteImageStr,
            @RequestParam(value = "deleteFileIds", required = false) String deleteFileStr,
            @RequestParam(name = "cp", defaultValue = "1") int cp,
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

        // ==================== null-safe parseIds ====================
        // 기존: null 반환 → service에서 null 체크 필요
        // 변경: 항상 빈 리스트 반환 → null-safe
      //게시글에 파일이없거나 이미지가 없으면 null이들어가게되는데 null이 들어가면 500서버에러가 뜸 
        //그래서 빈 리스트로 바꾸면 안전하게 처리됨
        List<Integer> deleteImageList = parseIds(deleteImageStr);
        List<Integer> deleteFileList = parseIds(deleteFileStr);

        int result = service.boardUpdate(
                board, images, files, deleteImageList, deleteFileList);

        ra.addFlashAttribute("message",
                result > 0 ? "게시글 수정 완료" : "게시글 수정 실패");

        return result > 0
                ? "redirect:/board/" + boardTypeNo + "/" + boardId + "?cp=" + cp
                : "redirect:/editBoard/" + boardTypeNo + "/" + boardId + "/update?cp=" + cp;
    }

    /* ==================== 게시글 삭제 ==================== */
    @PostMapping("/{boardTypeNo:[1-6]}/{boardId:[0-9]+}/delete")
    public String deleteBoard(
            @PathVariable("boardTypeNo") int boardTypeNo,
            @PathVariable("boardId") int boardId,
            @RequestParam(name = "cp", defaultValue = "1") int cp,
            HttpSession session,
            RedirectAttributes ra) throws Exception {

        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            ra.addFlashAttribute("message", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        Board board = service.selectBoard(boardId);
        if (board == null || !hasPermission(board, loginMember)) {
            ra.addFlashAttribute("message", "삭제 권한이 없습니다.");
            return "redirect:/board/" + boardTypeNo + "/" + boardId + "?cp=" + cp;
        }

        int result = service.boardDelete(boardId, loginMember);

        ra.addFlashAttribute("message",
                result > 0 ? "게시글 삭제 완료" : "게시글 삭제 실패");

        return "redirect:/board/" + boardTypeNo + "?cp=" + cp;
    }

    /* ==================== Summernote 업로드 ==================== */
    @PostMapping("/uploadFile")
    @ResponseBody
    public Map<String, String> uploadFile(
            @RequestParam("file") MultipartFile file,
            HttpSession session) throws Exception {

        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return Map.of("error", "LOGIN_REQUIRED");
        }

        String url = service.uploadFile(file);
        return Map.of("url", url);
    }

    /* ==================== 게시글 첨부 파일 목록 조회 ==================== */
    @GetMapping("/{boardTypeNo:[1-6]}/{boardId:[0-9]+}/files")
    @ResponseBody
    public List<BoardFile> getBoardFiles(
            @PathVariable("boardTypeNo") int boardTypeNo,
            @PathVariable("boardId") int boardId) {

        return service.selectBoardFiles(boardId);
    }

    /* ==================== 게시글 파일 다운로드 ==================== */
    @GetMapping("/{boardTypeNo:[1-6]}/file/{fileId}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable("boardTypeNo") int boardTypeNo,
            @PathVariable("fileId") int fileId) throws Exception {

        BoardFile file = service.selectBoardFile(fileId);

        if (file == null) {
            return ResponseEntity.notFound().build();
        }

     // Windows 전용 실제 저장 경로 (하드코딩임)
        String serverBasePath = "C:\\uploadFiles\\board";
        Path path = Paths.get(serverBasePath, file.getUploadfileStrg());
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        String encodedFileName = UriUtils.encode(file.getUploadfileOrigin(), StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                .body(resource);
    }

    /* ==================== 권한 체크 ==================== */
    private boolean hasPermission(Board board, Member member) {
        if (service.isAdminOnlyCategory(board.getBoardTypeNo())
                && member.getRole() != Member.Role.ADMIN) {
            return false;
        }
        return board.getMemberNo() == member.getMemberNo()
                || member.getRole() == Member.Role.ADMIN;
    }

    /* ==================== 문자열 → 정수 리스트 (null-safe) ==================== */
    private List<Integer> parseIds(String str) {
        // 변경: null이나 빈 문자열이면 항상 빈 리스트 반환 → null-safe
        if (str == null || str.isBlank()) return List.of();
        return Arrays.stream(str.split(","))
                .map(Integer::parseInt)
                .toList();
    }
}
