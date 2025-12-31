package edu.kh.eightgyosi.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
	
	

	
}
