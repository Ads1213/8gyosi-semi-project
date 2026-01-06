package edu.kh.eightgyosi.mypage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.eightgyosi.member.model.dto.Member;
import edu.kh.eightgyosi.mypage.model.dto.CalenderDTO;
import edu.kh.eightgyosi.mypage.model.dto.DiaryDTO;
import edu.kh.eightgyosi.mypage.model.dto.WrongNoteDTO;
import edu.kh.eightgyosi.mypage.model.service.CalenderService;
import edu.kh.eightgyosi.mypage.model.service.DiaryService;
import edu.kh.eightgyosi.mypage.model.service.WrongNoteService;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@SessionAttributes({"calender"})
@Controller
@RequestMapping("myPage")
@Slf4j
public class MyPageController {
	
	@Autowired
	private CalenderService calService; // 캘린더 서비스 필드 선언

	@Autowired
	private WrongNoteService wroService; // 오답노트 서비스 필드 선언
	
	@Autowired
	private DiaryService diaryService; // 다이어리 서비스 필드 선언
	
	
	/**
	 * @param loginMember : 로그인된 멤버의 멤버 객체(session 에 담김)
	 * @return
	 */
	@GetMapping("")
	public String mainPage(@SessionAttribute("loginMember") Member loginMember,
						   Model model) {
		
		// 1. 회원의 캘린더 정보 뿌려주기
		
		int memberNo = loginMember.getMemberNo();
		
		// 회원의 캘린더 정보 list로 얻어오기
		List<CalenderDTO> calender = calService.selectCalender(memberNo);
		
		// Model 객체 통해 조회된 결과 담고, 클래스 상단 @SessionAttributes 통해 Session 에 담기
		model.addAttribute("calender", calender);
		
		// test: log.debug("결과 : " + calender);
		
		// 2. 오답노트 정보 뿌려주기
		List<WrongNoteDTO> wrongNoteDTOLists = wroService.selectWrongNote(memberNo); 
		model.addAttribute("wrongNoteDTOLists", wrongNoteDTOLists);
		
		
		return "myPage/myPage-main"; // forward	
	}
	
	/** 오답노트 조회하여 오답노트 페이지로 이동하는 get 방식 요청 처리 메서드
	 * @param loginMember
	 * @param model
	 * @return
	 */
	@GetMapping("myPage-wrongNote/{wrongNoteNo:[0-9]+}")
	public String wrongNote(@SessionAttribute("loginMember") Member loginMember,
							Model model,
							@PathVariable("wrongNoteNo") int wrongNoteNo) {
		
		// 1. 오답노트 테이블 조회하기 위해 service 불러오기
		int memberNo = loginMember.getMemberNo();
		List<WrongNoteDTO> wrongNoteDTOLists = wroService.selectWrongNote(memberNo); 
		model.addAttribute("wrongNoteDTOLists", wrongNoteDTOLists);
		
		// 2. 주소창에서 얻어온 wrongNoteNo 도 따로 model 로 전달
		model.addAttribute("wrongNoteNo", wrongNoteNo);
		
		// test: log.debug(wrongNote.get(0).getWrongNoteExplain());
		
		return "myPage/myPage-wrongNote";
	}
	
	
	/*0* 오답노트 등록 서비스
	 * @return
	 */
	@PostMapping("myPage-wrongNote/{wrongNoteNo:[0-9]+}")
	public String insertWrongNote(@SessionAttribute("loginMember") Member loginMember,
								  Model model,
								  @ModelAttribute WrongNoteDTO wrongNote,
								  RedirectAttributes ra) {
		
		// 로그인 멤버 NO wrongNote 객체에 담기
		wrongNote.setMemberNo(loginMember.getMemberNo());
		
		// 서비스 호출
		int result = wroService.insertWrongNote(wrongNote);
		
		String message = null;
		String path = null;
		
		// 성공 시 
		if(result > 0) {
			
			message = "오답노트 정보 등록 완료";
			ra.addFlashAttribute("message", message);
			path = "redirect:/myPage/myPage-wrongNote/" + wrongNote.getWrongNoteNo();
			
		}else {
			
			message = "오답노트 정보 등록 실패";
			ra.addFlashAttribute("message", message);
			path = "redirect:/myPage/myPage-wrongNote/" + wrongNote.getWrongNoteNo();
			
		}
		
		return path;
	}
	
	
// ------------------------------------------------------------------------------	
	
	/** 일기장 작성일 형식 및 중복 확인 메서드 (***** 조립해서 쓸것 ******)
	 * @param loginMember
	 * @param model
	 * @param inputDiary
	 * @param ra
	 * @return
	 */
/*	public String checkWhiteDate(@SessionAttribute("loginMember") Member loginMember,
			Model model,
			@ModelAttribute DiaryDTO inputDiary, 
	        RedirectAttributes ra) { 
		int memberNo = loginMember.getMemberNo();
		inputDiary.setMemberNo(memberNo);
		int result = 0;
		String message = null;
		// *추가 - 숫자가 아닌 문자가 들어온 경우

		// *추가 - 2000이상의 값으로 시작하지 않는 경우
		
		
		// 8자리보다 크거나 작은 숫자를 입력한 경우
		if(inputDiary.getDiaryDate().length() != 8) {
			
		
			message = "YYYYMMDD형식의 8자리 작성일을 입력해주세요";
			ra.addFlashAttribute("message", message);
			return "redirect:/myPage";
		
		// 작성한 날짜에 이미 작성한 일기가 존재하는 경우
		} else if(result == 1) {
		

			
			result = diaryService.checkWhiteDate(inputDiary);
			

			message = "이미 회원님이 일기를 작성한 날이에요";
			ra.addFlashAttribute("message", message);
			return "redirect:/myPage";
		} else {
			

		
		}
	}
*/	
	
	
	/** 일기장 내용 저장
	 * @param loginMember
	 * @param model
	 * @return
	 */
	@PostMapping("diary/insertDiary")
	public String insertDiary(@SessionAttribute("loginMember") Member loginMember,
			Model model,
			@ModelAttribute DiaryDTO inputDiary, // 제목과 내용이 여기에 담김
	        RedirectAttributes ra) { 
		
		String message = null;

		
		int memberNo = loginMember.getMemberNo();
		inputDiary.setMemberNo(memberNo);
	    
	    // 2. 서비스 호출 (inputDiary 객체 자체를 넘기는 것이 좋습니다)
	    int result = diaryService.insertDiary(inputDiary);
	    log.debug("result : " + result);
	    
	    
	    if(result > 0) {
	        message = "일기가 성공적으로 저장되었습니다.";
	    } else {
	        message = "일기 저장에 실패했습니다. ";
	    }
	    
	    ra.addFlashAttribute("message", message);

	    // 3. 저장 후 다시 마이페이지 메인으로 리다이렉트
	    return "redirect:/myPage"; 
	    
		}
	
	
	/** 일기 내용 조회
	 * @param loginMember
	 * @param model
	 * @param inputDiary
	 * @param ra
	 * @return
	 */
	@ResponseBody
	@PostMapping("diary/selectDiary")
	public DiaryDTO selectDiary(@SessionAttribute("loginMember") Member loginMember,
	                            @RequestBody DiaryDTO inputDiary) { 
	    
	    inputDiary.setMemberNo(loginMember.getMemberNo());
	    
	    
	    log.debug("result : " + result);
	    // DB에서 일기 정보를 가져와서 객체 그대로 반환 (JSON 변환됨)
	    return diaryService.selectDiary(inputDiary); 
	}

	
	
	/** 일기 내용 삭제
	 * @param loginMember
	 * @param model
	 * @param inputDiary
	 * @param ra
	 * @return
	 */
	@PostMapping("diary/deleteDiary")
	public String deleteDiary(@SessionAttribute("loginMember") Member loginMember,
			Model model, 
	        @ModelAttribute DiaryDTO inputDiary,
	        RedirectAttributes ra) { 
		
		int memberNo = loginMember.getMemberNo();
		
		inputDiary.setMemberNo(memberNo);
		
		int result = diaryService.deleteDiary(inputDiary);

	    String message = null;
	    
	    if(result > 0) {
	        message = "일기가 성공적으로 삭제되었습니다.";
	    } else {
	        message = "일기 삭제가 실패했습니다.";
	    }
	    
	    ra.addFlashAttribute("message", message);

	    // 3. 저장 후 다시 마이페이지 메인으로 리다이렉트
	    return "redirect:/myPage";
	}
 		

// seongjong
	
	/** 마이페이지 프로필 수정 화면으로 이동
	 * @author dasol
	 * @return
	 */
	@GetMapping("info")
	public String myPageInfo() {
		
		return "myPage/myPage-info";
	}
	
	// 비밀번호 변경 화면 이동
	@GetMapping("changePw") 
	public String changePw() {
		return "myPage/myPage-changePw";
	}
	
	// 회원 탈퇴 화면 이동
	@GetMapping("secession")
	public String secession() {
		return "myPage/myPage-secession";
	}
	
	// 파일 테스트 화면으로 이동
	@GetMapping("fileTest")
	public String fileTest() {
		return "myPage/myPage-fileTest";
	}
	
	// 파일 목록 조회 화면 이동
	@GetMapping("fileList")
	public String fileList() {
		return "/myPage/fileList";
	}
	

	
	
	
}