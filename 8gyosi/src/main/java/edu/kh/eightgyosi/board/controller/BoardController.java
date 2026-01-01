package edu.kh.eightgyosi.board.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ch.qos.logback.core.model.Model;
import edu.kh.eightgyosi.board.model.service.BoardService;

@Controller
@RequestMapping("board")
public class BoardController {

	private BoardService service;
	
	@GetMapping("detail")
	public String BoardDetail() {
		return "board/BoardDetail";
	}
	
	@GetMapping("")
	public String BoardList() {
		return "board/boardList";
		
	}
	
	/** 게시글 목록 조회
	 * @return
	 */
	@GetMapping("{boardTypeNo:[0-9]+}")
	public String selectBoardList(@PathVariable("boardTypeNo") int boardTypeNo,
							@RequestParam(value = "cp", required = false, defaultValue = "1") int cp,
							Model model,
							@RequestParam Map<String, Object> paramMap) {
		
		Map<String, Object> map = null;
		
		// 검색이 아닌 경우 --> 
		if(paramMap.get("key") == null) {
			
			map = service.selectBoardList(boardTypeNo, cp);
			
		} else { // 검색인 경우(검색한 게시글 목록 조회)
			
			
		}
		
		
		
		
		return "board/boardList";	
	}
	
	

	
}
