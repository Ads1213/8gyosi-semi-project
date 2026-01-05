package edu.kh.eightgyosi.chatting.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import edu.kh.eightgyosi.chatting.model.dto.ChattingRoom;
import edu.kh.eightgyosi.chatting.model.service.ChattingService;
import edu.kh.eightgyosi.member.model.dto.Member;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("chatting")
@Slf4j
public class ChattingController {

	@Autowired
	private ChattingService service;
	
	@GetMapping("")
	public String chat() {
		return "chatting/chatting";
	}
	
/*	
	@GetMapping("list")
	public String chatting(@SessionAttribute("loginMember") Member loginMember,
						Model model
			) {

		List<ChattingRoom> roomList = service.selectRoomList(loginMember.getMemberNo());
		
		model.addAttribute("roomList", roomList);
		
		return "chatting/chatting";
	}
	// 채팅 상대 검색 - 비동기 
	@GetMapping("selectTarget")
	@ResponseBody // 비동기 요청이니깐
	public List<Member> selectTarget(@RequestParam("query") String query, 
									// queryString 형태로 받아왔다면 @RequestParam으로 갖고와야 한다
									@SessionAttribute("loginMember") Member loginMember
									) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("memberNo", loginMember.getMemberNo());
		map.put("query", query);
		
		return service.selectTarget(map);
		
	}
 */
}