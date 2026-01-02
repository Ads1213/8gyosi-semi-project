package edu.kh.eightgyosi.mypage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import edu.kh.eightgyosi.member.model.dto.Member;
import edu.kh.eightgyosi.mypage.model.dto.CalenderDTO;
import edu.kh.eightgyosi.mypage.model.service.CalenderService;
import lombok.extern.slf4j.Slf4j;

@SessionAttributes({"calender"})
@Controller
@RequestMapping("myPage")
@Slf4j
public class MyPageController {
	
	@Autowired
	private CalenderService calService; // 캘린더 서비스 필드 선언
	
	/**
	 * @param member : 로그인된 멤버의 멤버 객체(session 에 담김)
	 * @return
	 */
	@GetMapping("")
	public String mainPage(@SessionAttribute("loginMember") Member member,
						   Model model) {
		
		// 1. 회원의 캘린더 정보 뿌려주기
		
		int memberNo = member.getMemberNo();
		
		// 회원의 캘린더 정보 list로 얻어오기
		List<CalenderDTO> calender = calService.selectCalender(memberNo);
		
		// Model 객체 통해 조회된 결과 담고, 클래스 상단 @SessionAttributes 통해 Session 에 담기
		model.addAttribute("calender", calender);
		
		log.debug("결과 : " + calender);
		
		return "myPage/myPage-main"; // forward
	}
}

// seongjong