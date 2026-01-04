//package edu.kh.eightgyosi.board.controller;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.stream.Collectors;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.SessionAttribute;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import edu.kh.eightgyosi.board.model.dto.Board;
//import edu.kh.eightgyosi.board.model.service.EditBoardService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//
///**
// * EditBoardController
// * - 게시글 작성/수정/삭제/상세조회/좋아요 기능
// * - 조회수는 쿠키를 활용하여 하루 1회만 증가
// */
//
//@Controller
//@RequestMapping("/editBoard")
//@RequiredArgsConstructor
//public class EditBoardController {
//
//	@GetMapping("detail")
//	public String detail() {
//		return "board/boardDetail";
//	}
//	
//	@GetMapping("update")
//	public String update() {
//		return "board/boardUpdate";
//	}
//	
//	@GetMapping("write")
//	public String write() {
//		return "board/boardWrite";
//	}
//}
//
//    private final EditBoardService service;                // Service 주입
//    private static final String REDIRECT_BOARD = "redirect:/editBoard/";
//
//    // ===================== 게시글 작성 화면 =====================
//    @GetMapping("/{boardTypeNo:[1-6]}/insert")
//    public String insertForm(@PathVariable int boardTypeNo, Model model){
//        model.addAttribute("boardTypeNo", boardTypeNo);
//        model.addAttribute("categoryList", service.getCategoryList());
//        return "board/boardWrite";
//    }
//
//    // ===================== 게시글 작성 처리 =====================
//    @PostMapping("/{boardTypeNo:[1-6]}/insert")
//    public String insertBoard(@PathVariable int boardTypeNo,
//                              @ModelAttribute Board board,
//                              @SessionAttribute("loginMember") Member loginMember,
//                              @RequestParam(value="images", required=false) List<MultipartFile> images,
//                              RedirectAttributes ra) throws IOException {
//
//        board.setBoardTypeNo(boardTypeNo);
//        board.setMemberNo(loginMember.getMemberNo());
//
//        // 관리자 전용 카테고리 체크
//        if(service.isAdminOnlyCategory(boardTypeNo) && !"ADMIN".equals(loginMember.getRole())){
//            ra.addFlashAttribute("message", "공지사항은 관리자만 작성 가능합니다.");
//            return REDIRECT_BOARD + boardTypeNo;
//        }
//
//        try {
//            int boardId = service.boardInsert(board, images);
//            ra.addFlashAttribute("message", boardId > 0 ? "게시글 작성 완료" : "작성 실패");
//            return REDIRECT_BOARD + (boardId > 0 ? boardTypeNo + "/" + boardId : boardTypeNo + "/insert");
//        } catch(Exception e){
//            ra.addFlashAttribute("message", "게시글 작성 중 오류: " + e.getMessage());
//            return REDIRECT_BOARD + boardTypeNo + "/insert";
//        }
//    }
//
//    // ===================== 게시글 수정 화면 =====================
//    @GetMapping("/{boardTypeNo:[1-6]}/{boardId:[0-9]+}/update")
//    public String updateForm(@PathVariable int boardTypeNo,
//                             @PathVariable int boardId,
//                             @SessionAttribute("loginMember") Member loginMember,
//                             Model model,
//                             RedirectAttributes ra) {
//
//        Board board = service.selectBoard(boardId);
//        if(board == null){
//            ra.addFlashAttribute("message", "게시글이 존재하지 않습니다");
//            return REDIRECT_BOARD + boardTypeNo;
//        }
//        if(!hasPermission(board, loginMember)){
//            ra.addFlashAttribute("message", "본인 글 또는 관리자만 수정 가능");
//            return REDIRECT_BOARD + boardTypeNo + "/" + boardId;
//        }
//
//        model.addAttribute("board", board);
//        model.addAttribute("categoryList", service.getCategoryList());
//        return "board/boardUpdate";
//    }
//
//    // ===================== 게시글 수정 처리 =====================
//    @PostMapping("/{boardTypeNo:[1-6]}/{boardId:[0-9]+}/update")
//    public String updateBoard(@PathVariable int boardTypeNo,
//                              @PathVariable int boardId,
//                              @ModelAttribute Board board,
//                              @RequestParam(value="images", required=false) List<MultipartFile> images,
//                              @RequestParam(value="deleteImageList", required=false) String deleteImageListStr,
//                              @SessionAttribute("loginMember") Member loginMember,
//                              RedirectAttributes ra) throws Exception {
//
//        board.setBoardId(boardId);
//        board.setBoardTypeNo(boardTypeNo);
//        board.setMemberNo(loginMember.getMemberNo());
//
//        List<Integer> deleteImageList = parseDeleteList(deleteImageListStr);
//
//        try {
//            int result = service.boardUpdate(board, images, deleteImageList);
//            ra.addFlashAttribute("message", result > 0 ? "게시글 수정 완료" : "수정 실패");
//            return REDIRECT_BOARD + (result > 0 ? boardTypeNo + "/" + boardId : boardTypeNo + "/" + boardId + "/update");
//        } catch(Exception e){
//            ra.addFlashAttribute("message", "게시글 수정 중 오류: " + e.getMessage());
//            return REDIRECT_BOARD + boardTypeNo + "/" + boardId + "/update";
//        }
//    }
//
//    // ===================== 게시글 삭제 =====================
//    @PostMapping("/{boardTypeNo:[1-6]}/{boardId:[0-9]+}/delete")
//    public String deleteBoard(@PathVariable int boardTypeNo,
//                              @PathVariable int boardId,
//                              @SessionAttribute("loginMember") Member loginMember,
//                              RedirectAttributes ra) {
//
//        Board board = service.selectBoard(boardId);
//        if(board == null || !hasPermission(board, loginMember)){
//            ra.addFlashAttribute("message", "삭제 권한이 없습니다.");
//            return REDIRECT_BOARD + boardTypeNo + "/" + boardId;
//        }
//
//        try {
//            int result = service.boardDelete(boardId, loginMember.getMemberNo());
//            ra.addFlashAttribute("message", result > 0 ? "게시글 삭제 완료" : "삭제 실패");
//            return REDIRECT_BOARD + (result > 0 ? boardTypeNo : boardTypeNo + "/" + boardId);
//        } catch(Exception e){
//            ra.addFlashAttribute("message", "게시글 삭제 중 오류: " + e.getMessage());
//            return REDIRECT_BOARD + boardTypeNo + "/" + boardId;
//        }
//    }
//
//    // ===================== 게시글 상세조회 =====================
//    @GetMapping("/{boardTypeNo:[1-6]}/{boardId:[0-9]+}")
//    public String boardDetail(@PathVariable int boardTypeNo,
//                              @PathVariable int boardId,
//                              @SessionAttribute(value="loginMember", required=false) Member loginMember,
//                              Model model,
//                              RedirectAttributes ra,
//                              HttpServletRequest req,
//                              HttpServletResponse resp) {
//
//        Board board = service.selectBoard(boardId);
//        if(board == null){
//            ra.addFlashAttribute("message", "게시글이 존재하지 않습니다");
//            return REDIRECT_BOARD + boardTypeNo;
//        }
//
//        updateReadCount(board, loginMember, req, resp);
//
//        model.addAttribute("board", board);
//        return "board/boardDetail";
//    }
//
//    // ===================== 게시글 좋아요 =====================
//    @PostMapping("/{boardTypeNo:[1-6]}/{boardId:[0-9]+}/like")
//    @ResponseBody
//    public ResponseEntity<EditBoardService.LikeResponse> toggleLike(@PathVariable int boardTypeNo,
//                                                   @PathVariable int boardId,
//                                                   @SessionAttribute("loginMember") Member loginMember) {
//
//        EditBoardService.LikeResponse response = service.toggleBoardLikeWithCount(boardId, loginMember.getMemberNo());
//        return ResponseEntity.ok(response);
//    }
//
//    // ===================== 유틸 메서드 =====================
//    private boolean hasPermission(Board board, Member member){
//        return board.getMemberNo() == member.getMemberNo() || "ADMIN".equals(member.getRole());
//    }
//
//    private List<Integer> parseDeleteList(String deleteImageListStr){
//        if(deleteImageListStr == null || deleteImageListStr.isEmpty()) return null;
//        return Arrays.stream(deleteImageListStr.split(","))
//                .map(Integer::parseInt)
//                .collect(Collectors.toList());
//    }
//
//    private void updateReadCount(Board board, Member loginMember, HttpServletRequest req, HttpServletResponse resp){
//        if(loginMember != null && board.getMemberNo() == loginMember.getMemberNo()) return;
//
//        Cookie cookie = getCookie(req, "readBoard");
//        boolean increment = false;
//
//        if(cookie == null){
//            cookie = new Cookie("readBoard", "[" + board.getBoardId() + "]");
//            increment = true;
//        } else if(!cookie.getValue().contains("[" + board.getBoardId() + "]")){
//            cookie.setValue(cookie.getValue() + "[" + board.getBoardId() + "]");
//            increment = true;
//        }
//
//        if(increment){
//            int newCount = service.updateReadCount(board.getBoardId());
//            board.setBoardViewCount(newCount);
//
//            cookie.setPath("/");
//            long secondsUntilMidnight = Duration.between(LocalDateTime.now(),
//                    LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0)).getSeconds();
//            cookie.setMaxAge((int)secondsUntilMidnight);
//            resp.addCookie(cookie);
//        }
//    }
//
//    private Cookie getCookie(HttpServletRequest req, String name){
//        if(req.getCookies() == null) return null;
//        for(Cookie c : req.getCookies()){
//            if(name.equals(c.getName())) return c;
//        }
//        return null;
//    }
//
//
