package edu.kh.eightgyosi.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.eightgyosi.board.model.dto.Board;
import edu.kh.eightgyosi.board.model.dto.BoardType;
import edu.kh.eightgyosi.board.model.service.BoardService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MainController {

	@Autowired
	private BoardService service;
	
	@RequestMapping("/")
	public String mainPage(Model model){
		
		List<BoardType> boardTypeList = service.selectBoardType();
		List<Board> boardTop5List = service.selectBoardTop5List();
		
		model.addAttribute("boardTypeList", boardTypeList);
		model.addAttribute("boardTop5List", boardTop5List);
		
		return "common/main"; // forward
		

	}
	
	
	
}
