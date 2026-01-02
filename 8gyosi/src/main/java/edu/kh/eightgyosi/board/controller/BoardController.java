package edu.kh.semiproject.board.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.semiproject.board.model.dto.*;
import edu.kh.semiproject.board.model.service.EditBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/editBoard")
@Slf4j
@RequiredArgsConstructor
public class EditBoardController {

    private final EditBoardService service;

    // 게시글 작성 화면
    @GetMapping("/{boardId}/insert")
    public String insertForm(@PathVariable int boardId, Model model) {
        model.addAttribute("boardId", boardId);
        return "board/boardWrite";
    }

    // 게시글 작성
    @PostMapping("/{boardId}/insert")
    public String insertBoard(@PathVariable int boardId,
                              @ModelAttribute Board board,
                              @RequestParam(value="images", required=false) List<MultipartFile> images,
                              @RequestParam(value="files", required=false) List<MultipartFile> files,
                              @SessionAttribute("loginMember") Member loginMember,
                              RedirectAttributes ra) throws Exception {
        board.setBoardId(boardId);
        board.setMemberNo(loginMember.getMemberNo());

        int boardNo = service.boardInsert(board, images, files);
        ra.addFlashAttribute("message", boardNo>0 ? "게시글 작성 완료" : "작성 실패");
        return boardNo>0 ? "redirect:/board/"+boardId+"/"+boardNo : "redirect:/editBoard/"+boardId+"/insert";
    }

    // 게시글 수정 화면
    @GetMapping("/{boardId}/{boardNo}/update")
    public String updateForm(@PathVariable int boardId,
                             @PathVariable int boardNo,
                             @SessionAttribute("loginMember") Member loginMember,
                             Model model,
                             RedirectAttributes ra) {
        Board board = service.selectBoard(boardNo);
        if(board == null){
            ra.addFlashAttribute("message", "게시글이 존재하지 않습니다.");
            return "redirect:/";
        }
        if(board.getMemberNo() != loginMember.getMemberNo() && !"ADMIN".equals(loginMember.getRole())){
            ra.addFlashAttribute("message", "본인 글 또는 관리자가 아닌 경우 수정 불가");
            return "redirect:/board/"+boardId+"/"+boardNo;
        }
        model.addAttribute("board", board);
        return "board/boardUpdate";
    }

    // 게시글 수정 처리
    @PostMapping("/{boardId}/{boardNo}/update")
    public String updateBoard(@PathVariable int boardId,
                              @PathVariable int boardNo,
                              @ModelAttribute Board board,
                              @RequestParam(value="images", required=false) List<MultipartFile> images,
                              @RequestParam(value="files", required=false) List<MultipartFile> files,
                              @RequestParam(value="deleteImageList", required=false) List<Integer> deleteImageList,
                              @RequestParam(value="deleteFileList", required=false) List<Integer> deleteFileList,
                              @SessionAttribute("loginMember") Member loginMember,
                              RedirectAttributes ra) throws Exception {
        board.setBoardId(boardId);
        board.setMemberNo(loginMember.getMemberNo());

        int result = service.boardUpdate(board, images, files, deleteImageList, deleteFileList);
        ra.addFlashAttribute("message", result>0 ? "게시글 수정 완료" : "수정 실패");
        return result>0 ? "redirect:/board/"+boardId+"/"+boardNo
                        : "redirect:/editBoard/"+boardId+"/"+boardNo+"/update";
    }

    // 게시글 삭제
    @PostMapping("/{boardId}/{boardNo}/delete")
    public String deleteBoard(@PathVariable int boardId,
                              @PathVariable int boardNo,
                              @SessionAttribute("loginMember") Member loginMember,
                              RedirectAttributes ra) throws Exception {
        int result = service.boardDelete(boardNo, loginMember);
        ra.addFlashAttribute("message", result>0 ? "게시글 삭제 완료" : "삭제 실패");
        return result>0 ? "redirect:/board/"+boardId
                        : "redirect:/board/"+boardId+"/"+boardNo;
    }

    // Summernote 이미지/파일 업로드
    @PostMapping("/uploadFile")
    @ResponseBody
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        return service.uploadFile(file);
    }

    // 댓글 작성
    @PostMapping("/comment/insert")
    @ResponseBody
    public int insertComment(@RequestBody BoardComment comment,
                             @SessionAttribute("loginMember") Member loginMember){
        comment.setMemberNo(loginMember.getMemberNo());
        return service.insertComment(comment);
    }

    // 댓글/대댓글 삭제
    @PostMapping("/comment/{commentId}/delete")
    @ResponseBody
    public int deleteComment(@PathVariable int commentId,
                             @SessionAttribute("loginMember") Member loginMember){
        return service.deleteComment(commentId, loginMember);
    }
}

