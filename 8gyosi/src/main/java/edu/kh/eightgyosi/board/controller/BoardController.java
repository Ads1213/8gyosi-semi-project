package edu.kh.eightgyosi.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("board")
public class BoardController {

	@GetMapping("detail")
	public String BoardDetail() {
		return "board/BoardDetail";
	}
	
	@GetMapping("")
	public String BoardList() {
		return "board/boardList";
		
	}
	
	
}

//chulku