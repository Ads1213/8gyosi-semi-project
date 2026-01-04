package edu.kh.eightgyosi.chatting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.eightgyosi.chatting.model.service.ChattingService;
import edu.kh.eightgyosi.chatting.model.service.ChattingServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("chatting")
@Slf4j
public class ChattingController {

	@Autowired
	private ChattingService service;
	
	@GetMapping("")
	public String chatting() {
		return "chatting/chatting";
	}
}
