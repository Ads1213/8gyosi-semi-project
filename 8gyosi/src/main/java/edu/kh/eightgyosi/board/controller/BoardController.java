package edu.kh.eightgyosi.board.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.eightgyosi.board.model.dto.Board;
import edu.kh.eightgyosi.board.model.dto.BoardImage;
import edu.kh.eightgyosi.board.model.service.BoardService;
import edu.kh.eightgyosi.board.model.service.EditBoardService;
import edu.kh.eightgyosi.member.model.dto.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("board")
public class BoardController {

	@Autowired
	private BoardService service;
	@Autowired
	private EditBoardService editService;
	
	/*
	@GetMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}")
	public String BoardDetail(@PathVariable("boardCode") int boardCode,
							  @RequestParam(value="cp", required = false, defaultValue = "1") int cp,
							  Model model){
		
		
		return "board/BoardDetail";
	}
	*/
	
//	@GetMapping("")
//	public String BoardList() {
//		return "board/boardList";
//		
//	}

	/** 게시글 목록 조회 BOARD CONTROLLER
	 * @return
	 * {boardCodeNo:[0-9]+}
	 */
	// @GetMapping("{boardTypeNo:[0-9]+}")

	/*
	public String selectBoardList(@PathVariable("boardTypeNo") int boardTypeNo,
							@RequestParam(value = "cp", required = false, defaultValue = "1") int cp,
							Model model,
							@RequestParam Map<String, Object> paramMap) {
		
		Map<String, Object> map = null;
		
		// 검색이 아닌 경우 --> 
		if(paramMap.get("key") == null) {
			
			map = service.selectBoardList(boardTypeNo, cp);
			
		} else { // 검색인 경우(검색한 게시글 목록 조회)
			paramMap.put("boardTypeNo", boardTypeNo);
			// -> paramMap은 {key=w, query=짱구, boardCode=1}
			
			// 검색(내가 검색하고 싶은 게시글 목록 조회) 서비스 호출
			map = service.searchList(paramMap, cp);
			
		}
		
		// model에 결과 값 등록
		model.addAttribute("pagination", map.get("pagination"));
		model.addAttribute("boardList", map.get("boardList"));
		
		log.debug("pagination :: {}", map.get("pagination"));
		
		return "board/boardList";	
	}*/
	
	 @GetMapping("{boardTypeNo:[0-9]+}")
	    public String selectBoardList(@PathVariable("boardTypeNo") int boardTypeNo,
	                                  @RequestParam(value = "cp", required = false, defaultValue = "1") int cp,
	                                  Model model,
	                                  @RequestParam Map<String, Object> paramMap) {

	        Map<String, Object> map = null;

	        if (paramMap.get("key") == null) {
	            map = service.selectBoardList(boardTypeNo, cp);
	        } else {
	            paramMap.put("boardTypeNo", boardTypeNo);
	            map = service.searchList(paramMap, cp);
	        }

	        model.addAttribute("pagination", map.get("pagination"));
	        model.addAttribute("boardList", map.get("boardList"));

	        return "board/boardList";
	    }

	    // 게시글 상세
	    @GetMapping("{boardTypeNo:[0-9]+}/{boardId:[0-9]+}")
	    public String BoardDetail(@PathVariable("boardTypeNo") int boardTypeNo,
	                              @PathVariable("boardId") int boardId,
	                              @SessionAttribute(value = "loginMember", required = false) Member loginMember,
	                              Model model,
	                              RedirectAttributes ra,
	                              HttpServletRequest req,
	                              HttpServletResponse resp) {

	        Map<String, Integer> map = new HashMap<>();
	        map.put("boardTypeNo", boardTypeNo);
	        map.put("boardId", boardId);

	        if (loginMember != null) map.put("memberNo", loginMember.getMemberNo());

	        Board board = service.selectOne(map);
	        String path;

	        if (board == null) {
	            path = "redirect:/board/" + boardTypeNo;
	            ra.addFlashAttribute("message", "게시글이 존재하지 않습니다.");
	        } else {
	            // 조회수 처리
	            if (loginMember == null || board.getMemberNo() != loginMember.getMemberNo()) {
	                Cookie[] cookies = req.getCookies();
	                Cookie c = null;

	                if (cookies != null) {
	                    for (Cookie temp : cookies) {
	                        if (temp.getName().equals("readBoardId")) {
	                            c = temp;
	                            break;
	                        }
	                    }
	                }

	                int result = 0;
	                if (c == null) {
	                    c = new Cookie("readBoardId", "[" + boardId + "]");
	                    result = service.updateReadCount(boardId);
	                } else if (c.getValue().indexOf("[" + boardId + "]") == -1) {
	                    c.setValue(c.getValue() + "[" + boardId + "]");
	                    result = service.updateReadCount(boardId);
	                }

	                if (result > 0) {
	                    board.setBoardViewCount(result);
	                    c.setPath("/");
	                    LocalDateTime now = LocalDateTime.now();
	                    LocalDateTime nextDayMidnight = now.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
	                    long secondsUntilNextDay = Duration.between(now, nextDayMidnight).getSeconds();
	                    c.setMaxAge((int) secondsUntilNextDay);
	                    resp.addCookie(c);
	                }
	            }

	            path = "board/boardDetail";

	            // 첨부 파일
	            board.setBoardFiles(editService.selectBoardFiles(boardId));

	            // 모델 등록
	            model.addAttribute("board", board);

	            if (!board.getBoardImages().isEmpty()) {
	                BoardImage thumbnail = null;
	                if (board.getBoardImages().get(0).getImgOrder() == 0) {
	                    thumbnail = board.getBoardImages().get(0);
	                }
	                model.addAttribute("thumbnail", thumbnail);
	                model.addAttribute("start", thumbnail != null ? 1 : 0);
	            }
	        }

	        return path;
	    }
 //ERR_INCOMPLETE_CHUNKED_ENCODING 떠서 null처리 필수 변경함
	    // 좋아요
	    @ResponseBody
	    @PostMapping("like")
	    public int boardLike(@RequestBody Map<String, Integer> map) {
	        return service.boardLike(map);
	    }
	}