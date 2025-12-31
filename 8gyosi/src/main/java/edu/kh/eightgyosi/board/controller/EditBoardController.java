/*
 * package edu.kh.eightgyosi.board.controller;
 * 
 * import java.util.Arrays; import java.util.List; import java.util.Map; import
 * java.util.stream.Collectors;
 * 
 * import org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.ModelAttribute; import
 * org.springframework.web.bind.annotation.PathVariable; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.RequestBody; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.RequestParam; import
 * org.springframework.web.bind.annotation.ResponseBody; import
 * org.springframework.web.bind.annotation.SessionAttribute; import
 * org.springframework.web.multipart.MultipartFile; import
 * org.springframework.web.servlet.mvc.support.RedirectAttributes;
 * 
 * import edu.kh.eightgyosi.board.model.dto.Board; import
 * edu.kh.eightgyosi.board.model.dto.BoardComment; import
 * edu.kh.eightgyosi.board.model.service.EditBoardService; import
 * edu.kh.eightgyosi.model.dto.Member; import lombok.RequiredArgsConstructor;
 * 
 * @Controller
 * 
 * @RequestMapping("/editBoard")
 * 
 * @RequiredArgsConstructor
 * 
 * public class EditBoardController {
 * 
 * private final EditBoardService service;
 * 
 *//** 게시글 작성 화면 */
/*
 * @GetMapping("/{boardId}/insert") public String insertForm(@PathVariable int
 * boardId, Model model){ model.addAttribute("boardId", boardId);
 * model.addAttribute("categoryList", service.getCategoryList()); return
 * "board/boardWrite"; }
 * 
 *//** 게시글 작성 */
/*
 * @PostMapping("/{boardId}/insert") public String insertBoard(@PathVariable int
 * boardId,
 * 
 * @ModelAttribute Board board,
 * 
 * @RequestParam(value="images", required=false) List<MultipartFile> images,
 * 
 * @RequestParam(value="files", required=false) List<MultipartFile> files,
 * 
 * @SessionAttribute("loginMember") Member loginMember, RedirectAttributes ra)
 * throws Exception {
 * 
 * board.setBoardId(boardId); board.setMemberNo(loginMember.getMemberNo());
 * 
 * // 서버에서 카테고리 확인 후 관리자 여부 체크 if(service.isAdminOnlyCategory(boardId) &&
 * !"ADMIN".equals(loginMember.getRole())){ ra.addFlashAttribute("message",
 * "공지사항은 관리자만 작성 가능합니다."); return "redirect:/board/"+boardId; }
 * 
 * int boardNo = service.boardInsert(board, images, files);
 * ra.addFlashAttribute("message", boardNo > 0 ? "게시글 작성 완료" : "작성 실패");
 * 
 * return boardNo > 0 ? "redirect:/board/"+boardId+"/"+boardNo :
 * "redirect:/editBoard/"+boardId+"/insert"; }
 * 
 *//** 게시글 수정 화면 */
/*
 * @GetMapping("/{boardId}/{boardNo}/update") public String
 * updateForm(@PathVariable int boardId,
 * 
 * @PathVariable int boardNo,
 * 
 * @SessionAttribute("loginMember") Member loginMember, Model model,
 * RedirectAttributes ra){
 * 
 * Board board = service.selectBoard(boardNo); if(board == null){
 * ra.addFlashAttribute("message","게시글이 존재하지 않습니다."); return "redirect:/"; }
 * if(board.getMemberNo() != loginMember.getMemberNo() &&
 * !"ADMIN".equals(loginMember.getRole())){
 * ra.addFlashAttribute("message","본인 글 또는 관리자가 아닌 경우 수정 불가"); return
 * "redirect:/board/"+boardId+"/"+boardNo; }
 * 
 * model.addAttribute("board", board); model.addAttribute("categoryList",
 * service.getCategoryList()); return "board/boardUpdate"; }
 * 
 *//** 게시글 수정 처리 */
/*
 * @PostMapping("/{boardId}/{boardNo}/update") public String
 * updateBoard(@PathVariable int boardId,
 * 
 * @PathVariable int boardNo,
 * 
 * @ModelAttribute Board board,
 * 
 * @RequestParam(value="images", required=false) List<MultipartFile> images,
 * 
 * @RequestParam(value="files", required=false) List<MultipartFile> files,
 * 
 * @RequestParam(value="deleteImageList", required=false) String
 * deleteImageListStr,
 * 
 * @RequestParam(value="deleteFileList", required=false) String
 * deleteFileListStr,
 * 
 * @SessionAttribute("loginMember") Member loginMember, RedirectAttributes ra)
 * throws Exception {
 * 
 * board.setBoardId(boardId); board.setMemberNo(loginMember.getMemberNo());
 * 
 * // 문자열로 넘어온 삭제 목록을 List<Integer>로 변환 List<Integer> deleteImageList =
 * (deleteImageListStr != null && !deleteImageListStr.isEmpty()) ?
 * Arrays.stream(deleteImageListStr.split(",")).map(Integer::parseInt).collect(
 * Collectors.toList()) : null; List<Integer> deleteFileList =
 * (deleteFileListStr != null && !deleteFileListStr.isEmpty()) ?
 * Arrays.stream(deleteFileListStr.split(",")).map(Integer::parseInt).collect(
 * Collectors.toList()) : null;
 * 
 * int result = service.boardUpdate(board, images, files, deleteImageList,
 * deleteFileList); ra.addFlashAttribute("message", result > 0 ? "게시글 수정 완료" :
 * "수정 실패");
 * 
 * return result > 0 ? "redirect:/board/"+boardId+"/"+boardNo :
 * "redirect:/editBoard/"+boardId+"/"+boardNo+"/update"; }
 * 
 *//** 게시글 삭제 */
/*
 * @PostMapping("/{boardId}/{boardNo}/delete") public String
 * deleteBoard(@PathVariable int boardId,
 * 
 * @PathVariable int boardNo,
 * 
 * @SessionAttribute("loginMember") Member loginMember, RedirectAttributes ra)
 * throws Exception {
 * 
 * Board board = service.selectBoard(boardNo); if(board.getMemberNo() !=
 * loginMember.getMemberNo() && !"ADMIN".equals(loginMember.getRole())){
 * ra.addFlashAttribute("message","삭제 권한이 없습니다."); return
 * "redirect:/board/"+boardId+"/"+boardNo; }
 * 
 * int result = service.boardDelete(boardNo, loginMember);
 * ra.addFlashAttribute("message", result > 0 ? "게시글 삭제 완료" : "삭제 실패"); return
 * result > 0 ? "redirect:/board/"+boardId :
 * "redirect:/board/"+boardId+"/"+boardNo; }
 * 
 *//** Summernote 이미지/파일 업로드 */
/*
 * @PostMapping("/uploadFile")
 * 
 * @ResponseBody public Map<String,String> uploadFile(@RequestParam("file")
 * MultipartFile file) throws Exception { String url = service.uploadFile(file);
 * return Map.of("url", url); }
 * 
 *//** 댓글 작성 */
/*
 * @PostMapping("/{boardNo}/comment")
 * 
 * @ResponseBody public int insertComment(@PathVariable int boardNo,
 * 
 * @RequestBody BoardComment comment,
 * 
 * @SessionAttribute("loginMember") Member loginMember){
 * 
 * comment.setMemberNo(loginMember.getMemberNo()); comment.setBoardNo(boardNo);
 * return service.insertComment(comment); }
 * 
 *//** 댓글 삭제 */
/*
 * @PostMapping("/comment/{commentId}/delete")
 * 
 * @ResponseBody public int deleteComment(@PathVariable int commentId,
 * 
 * @SessionAttribute("loginMember") Member loginMember){ return
 * service.deleteComment(commentId, loginMember); }
 * 
 *//** 좋아요/싫어요 토글 *//*
					 * @PostMapping("/{boardNo}/like")
					 * 
					 * @ResponseBody public int toggleLike(@PathVariable int boardNo,
					 * 
					 * @RequestParam boolean isLike,
					 * 
					 * @SessionAttribute("loginMember") Member loginMember){ return
					 * service.toggleBoardLike(boardNo, loginMember.getMemberNo(), isLike); } }
					 * //chulku
					 */