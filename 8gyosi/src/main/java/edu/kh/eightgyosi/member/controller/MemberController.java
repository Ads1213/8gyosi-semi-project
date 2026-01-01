package edu.kh.eightgyosi.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.eightgyosi.model.dto.Member;
import edu.kh.eightgyosi.model.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SessionAttributes({ "loginMember" })
@Controller
@RequestMapping("8gyosi")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
	
	private final MemberService service;
	
	/** @author dasol
	 * 
	 * @param member
	 */
	@PostMapping("login")
	public String login(Member member) {
		
		try {
			Member loginMember = service.login(member);
			
			
		} catch (Exception e) {
			
			log.info("로그인 중 예외 발생");
			e.printStackTrace();
		}
		
		return "";
	
	}
	
	/** 로그아웃 기능
	 * @param status
	 * @return
	 */
	@GetMapping("logout")
	public String logout(SessionStatus status) {
		
		status.setComplete(); // 세션을 완료
		
		return "redirect:/";
	}
	
	@GetMapping("signup")
	public String signupPage() {
		return "member/signup";
	}
	

}
