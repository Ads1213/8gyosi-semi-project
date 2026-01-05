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
			@RequestParam(value = "cp", required = false, defaultValue = "1") int cp, Model model,
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
		
		return "board/boardList";
	}
	
	@GetMapping("{boardTypeNo:[0-9]+}/{boardId:[0-9]+}")
	public String BoardDetail(@PathVariable("boardTypeNo") int boardTypeNo,
							@PathVariable("boardId") int boardId,
							@SessionAttribute(value = "loginMember", required = false) Member loginMember,
							Model model,
							RedirectAttributes ra,
							HttpServletRequest req,
							HttpServletResponse resp) {
		
		Map<String, Integer> map =  new HashMap<>();
		map.put("boardTypeNo", boardTypeNo);
		map.put("boardId", boardId);
		
		if(loginMember != null) {
			map.put("memberNo", loginMember.getMemberNo());
		}
		
		Board board = service.selectOne(map);
		
		String path = null;
		
		if(board == null) {
			path = "redirect:/board/" + boardTypeNo;
			ra.addFlashAttribute("message", "게시글이 존재하지 않습니다.");
		} else { //조회 결과가 있는 경우
			
			if(loginMember == null || board.getMemberNo() != loginMember.getMemberNo()) {
				
				// 요청에 담겨있는 모든 쿠키 얻어오기
				Cookie[] cookies = req.getCookies();
				
				Cookie c = null;
				
				for(Cookie temp : cookies) {
					
					// 쿠키 중에 "readBoardNo" 가 존재할 때
					if(temp.getName().equals("readBoardId")) {
						c = temp;
						break;
					}
					
				}
				
				int result = 0; // 조회수 증가 결과를 저장할 변수
				
				// "readBoardNo" 가 쿠키에 없을 때
				if(c == null) {
					
					c = new Cookie("readBoardId", "[" + boardId + "]");
					result = service.updateReadCount(boardId);
					
				} else { // "readBoardNo" 가 쿠키에 있을 때

					// 현재 글을 처음 읽는 경우
					if(c.getValue().indexOf("[" + boardId + "]") == -1) {
						
						// 해당 글번호를 쿠키에 누적 + 서비스 호출
						c.setValue(c.getValue() + "[" + boardId + "]");
						
						result = service.updateReadCount(boardId);
						
					}
					
				}
				
				// 조회 수 증가 성공 / 조회 성공 시
				if(result > 0) {
					
					// 앞서 조회했던 board의 readCount 값을
					// result 값을 다시 세팅
					board.setBoardViewCount(result);
					
					// 쿠키 적용 경로 설정
					c.setPath("/");
					
					// 쿠키 수명 지정
					// 현재 시간을 얻어오기
					LocalDateTime now = LocalDateTime.now();
					
					// 다음날 자정 지정
					LocalDateTime nextDayMidnight = now.plusDays(1).withHour(0)
									.withMinute(0).withSecond(0).withNano(0);
					
					// 다음날 자정까지 남은 시간 계산 (초단위)
					long secondsUntilNextDay = Duration.between(now, nextDayMidnight).getSeconds();
					
					// 쿠키 수명 설정
					c.setMaxAge((int)secondsUntilNextDay);
					
					resp.addCookie(c); // 응답 객체를 이용해서 클라이언트에게 전달
					
				}
				
			}
			
			
			path = "board/boardDetail";
			
			model.addAttribute("board", board);
			
			if( !board.getBoardImages().isEmpty()) {
				
				BoardImage thumbnail = null;
				
				if(board.getBoardImages().get(0).getImgOrder() == 0) {
					thumbnail = board.getBoardImages().get(0);
				}
				
				model.addAttribute("thumbnail", thumbnail);
				
				model.addAttribute("start", thumbnail != null ? 1: 0);
				
				
			}
		}
		
		
		return path;
	}
	
	@ResponseBody
	@PostMapping("like")
	public int boardLike(@RequestBody Map<String, Integer> map) {
		return service.boardLike(map);
	}

	
}
