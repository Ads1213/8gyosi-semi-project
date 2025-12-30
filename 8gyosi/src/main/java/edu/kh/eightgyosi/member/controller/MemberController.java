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
	 * @param ra
	 * @param model
	 * @param saveId
	 * @param resp
	 * @throws Exception
	 */
	@PostMapping("login")
	public String login(Member member, RedirectAttributes ra,
						Model model,
						@RequestParam(value = "saveId", required = false)
						String saveId, HttpServletResponse resp) throws Exception {
		
		// 로그인 서비스 호출
		Member loginMember = service.login(member);
		
		log.debug("loginMember : " + loginMember);
		
		// 로그인 실패 시
		if(loginMember == null) {
			ra.addFlashAttribute("message", "아이디 또는 비밀번호가 일치하지 않습니다");
		
		} else {
			// 로그인 성공 시
			model.addAttribute("loginMember", loginMember);
		}
		
		return "redirect:/";
	}
	
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
