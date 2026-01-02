<<<<<<< HEAD

=======
>>>>>>> e6955aaaee45b2c428d179a47efd47b585391a64
package edu.kh.eightgyosi.board.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


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

	/** 게시글 목록 조회 BOARD CONTROLLER
	 * @return
	 * {boardCodeNo:[0-9]+}
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
			paramMap.put("boardTypeNo", boardTypeNo);
<<<<<<< HEAD

=======
			// -> paramMap은 {key=w, query=짱구, boardCode=1}
>>>>>>> e6955aaaee45b2c428d179a47efd47b585391a64
			
			// 검색(내가 검색하고 싶은 게시글 목록 조회) 서비스 호출
			map = service.searchList(paramMap, cp);
			
		}
		
		// model에 결과 값 등록
		model.addAttribute("pagination", map.get("pagination"));
		model.addAttribute("boardList", map.get("boardList"));
		
		return "board/boardList";	
	}
	

	

	
<<<<<<< HEAD
}
=======
}
>>>>>>> e6955aaaee45b2c428d179a47efd47b585391a64
