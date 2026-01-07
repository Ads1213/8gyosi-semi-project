package edu.kh.eightgyosi.mypage.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.eightgyosi.member.model.dto.Member;
import edu.kh.eightgyosi.mypage.model.dto.CalenderDTO;
import edu.kh.eightgyosi.mypage.model.dto.DiaryDTO;
import edu.kh.eightgyosi.mypage.model.dto.TimetableDTO;
import edu.kh.eightgyosi.mypage.model.dto.WrongNoteDTO;
import edu.kh.eightgyosi.mypage.model.service.CalenderService;
import edu.kh.eightgyosi.mypage.model.service.DiaryService;
import edu.kh.eightgyosi.mypage.model.service.MyPageService;
import edu.kh.eightgyosi.mypage.model.service.TimetableService;
import edu.kh.eightgyosi.mypage.model.service.WrongNoteService;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@SessionAttributes({ "calender", "loginMember" })
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

	@Autowired
	private MyPageService myPageService; // 내 정보 변경

	
	@Autowired
	private TimetableService timetableService;
	
	// @Autowired 
	// private ServletContext application; // 테스트용
	
	/**
	 * @param loginMember : 로그인된 멤버의 멤버 객체(session 에 담김)
	 * @return
	 */
	@GetMapping("")
	public String mainPage(@SessionAttribute("loginMember") Member loginMember, Model model) {

		// 1. 회원의 캘린더 정보 뿌려주기

		int memberNo = loginMember.getMemberNo();

		// 회원의 캘린더 정보 list로 얻어오기
		List<CalenderDTO> calender = calService.selectCalender(memberNo);

		// Model 객체 통해 조회된 결과 담고, 클래스 상단 @SessionAttributes 통해 Session 에 담기
		model.addAttribute("calender", calender);

		// test: log.debug("boardTypeList : " + application.getAttribute("boardTypeList"));
		// test: log.debug("결과 : " + calender);

		// 2. 오답노트 정보 뿌려주기
		List<WrongNoteDTO> wrongNoteDTOLists = wroService.selectWrongNote(memberNo);
		model.addAttribute("wrongNoteDTOLists", wrongNoteDTOLists);

		return "myPage/myPage-main"; // forward
		
		// 3. 시간표 정보 뿌려주기
		List<TimetableDTO> timetableDTOLists = timetableService.selectTimetable(memberNo);
		
		
		
		// row : day
		// col : cls
		String[][] tt = new String[6][7];
		for(int i = 0; i < 6; i++) {
			Arrays.fill(tt[i], "미설정"); // 모든 행(i, day, 요일) 에 미설정 채워넣기 
		}
		
		for(TimetableDTO temp : timetableDTOLists) { // 가져온 DTO 객체 하나씩 돈다
			int row = temp.getDay() - 1; // 인덱스로 반환 (0~5)
			int col = temp.getCls() - 1; // 인덱스로 반환 (0~6)
			
			if(row >= 0 && row < 6 && col >= 0 && col < 7) {
				tt[row][col] = temp.getSubject(); // 이중 배열 특정 칸에 가져온 과목 넣기
			}
		}
		
		// test: log.debug(Arrays.deepToString(tt));
		
		model.addAttribute("fullTimetable", tt);
		
		return "myPage/myPage-main"; // forward	
	}

	/**
	 * 오답노트 조회하여 오답노트 페이지로 이동하는 get 방식 요청 처리 메서드
	 * 
	 * @param loginMember
	 * @param model
	 * @return
	 */
	@GetMapping("myPage-wrongNote/{wrongNoteNo:[0-9]+}")
	public String wrongNote(@SessionAttribute("loginMember") Member loginMember, Model model,
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

	/*
	 * 0* 오답노트 등록 서비스
	 * 
	 * @return
	 */
	@PostMapping("myPage-wrongNote/{wrongNoteNo:[0-9]+}")
	public String insertWrongNote(@SessionAttribute("loginMember") Member loginMember, Model model,
			@ModelAttribute WrongNoteDTO wrongNote, RedirectAttributes ra) {

		// 로그인 멤버 NO wrongNote 객체에 담기
		wrongNote.setMemberNo(loginMember.getMemberNo());

		// 서비스 호출
		int result = wroService.insertWrongNote(wrongNote);

		String message = null;
		String path = null;

		// 성공 시
		if (result > 0) {

			message = "오답노트 정보 등록 완료";
			ra.addFlashAttribute("message", message);
			path = "redirect:/myPage/myPage-wrongNote/" + wrongNote.getWrongNoteNo();

		} else {

			message = "오답노트 정보 등록 실패";
			ra.addFlashAttribute("message", message);
			path = "redirect:/myPage/myPage-wrongNote/" + wrongNote.getWrongNoteNo();

		}

		return path;
	}

	/**
	 * 일기장 내용 저장
	 * 
	 * @param loginMember
	 * @param model
	 * @return
	 */
	@PostMapping("diary/insertDiary")
	public String insertDiary(@SessionAttribute("loginMember") Member loginMember, Model model,
			@ModelAttribute DiaryDTO inputDiary, // 제목과 내용이 여기에 담김
			RedirectAttributes ra) {

		int memberNo = loginMember.getMemberNo();
		inputDiary.setMemberNo(memberNo);

		// 2. 서비스 호출 (inputDiary 객체 자체를 넘기는 것이 좋습니다)
		int result = diaryService.insertDiary(inputDiary);
		log.info("inputDiary", inputDiary);
		log.debug("inputDiary :: {}", "inputDiary");
		String message = null;

		if (result > 0) {
			message = "일기가 성공적으로 저장되었습니다.";
		} else {
			message = "일기 저장에 실패했습니다.";
		}

		ra.addFlashAttribute("message", message);

		// 3. 저장 후 다시 마이페이지 메인으로 리다이렉트
		return "redirect:/myPage";
	}

	@PostMapping("diary/selectDiary")
	public String selectDiary(@SessionAttribute("loginMember") Member loginMember, @ModelAttribute DiaryDTO inputDiary,
			Model model, RedirectAttributes ra) {

		inputDiary.setMemberNo(loginMember.getMemberNo());

		// 1. 서비스 호출 (결과를 DTO 객체로 받음)
		DiaryDTO diary = diaryService.selectDiary(inputDiary);

		if (diary != null) {

			model.addAttribute("diary", diary);
			return "redirect:/myPage";
		} else {
			ra.addFlashAttribute("message", "해당 날짜에 작성된 일기가 없습니다.");
			return "redirect:/myPage";
		}
	}

// seongjong

	/**
	 * 마이페이지 프로필 수정 화면으로 이동
	 * 
	 * @author dasol
	 * @param loginMember
	 * @return
	 */
	@GetMapping("info") // /myPage/info
	public String info(@SessionAttribute("loginMember") Member loginMember, Model model) {

		// 현재 로그인한 회원의 주소를 꺼내옴
		// 현재 로그인한 회원 정보 -> session scope 에 등록된 상태(loginMember)
		// loginMember(memberAddress도 포함)
		// -> 만약 회원가입 당시 주소를 입력했다면 주소값 문자열(^^^ 구분자로 만들어진 문자열)
		// -> 회원가입 당시 주소를 입력하지 않았다면 null
		String memberAddress = loginMember.getMemberAddress();
		// 03189^^^서울 종로구 우정국로2길 21^^^3층, 302클래스 (대왕빌딩)
		// or null

		if (memberAddress != null) { // 주소가 있을 경우에만 동작
			// 구분자 "^^^" 를 기준으로
			// memberAddress 값을 쪼개어 String[] 로 반환
			String[] arr = memberAddress.split("\\^\\^\\^");
			// ["03189", "서울 종로구 우정국로2길 21", "3층, 302호 클래스 (대왕빌딩)"]

			model.addAttribute("postcode", arr[0]); // 우편주소
			model.addAttribute("address", arr[1]); // 도로명/지번주소
			model.addAttribute("detailAddress", arr[2]); // 상세주소
		}

		return "myPage/myPage-info";
	}

	/**
	 * 회원 정보 수정
	 * 
	 * @param member
	 * @param memberAddress : 주소만 따로 배열 형태로 얻어옴
	 * @param loginMember   : 현재 로그인한 회원의 회원번호(PK) 사용
	 * @return
	 */
	@PostMapping("info") // /myPage/info POST 방식 요청 매핑
	public String updateInfo(@ModelAttribute Member member, @RequestParam("memberAddress") String[] memberAddress,
			@SessionAttribute("loginMember") Member loginMember, RedirectAttributes ra) {

		// Member 에 현재 로그인한 회원 번호 추가
		member.setMemberNo(loginMember.getMemberNo());
		// member : 수정된 회원의 닉네임, 수정된 회원의 전화번호, [주소], 회원번호

		// 회원 정보 수정 서비스 호출
		int result = myPageService.updateInfo(member, memberAddress);

		String message = null;

		if (result > 0) {
			message = "회원 정보 수정 성공!!!";

			// member에 DB상 업데이트된 내용으로 세팅
			// -> member는 세션에 저장된 로그인한 회원 정보가
			// 저장되어 있다 (로그인 할 당시의 기존 데이터)
			// -> member를 수정하면 세션에 저장된 로그인한 회원의
			// 정보가 업데이트 된다
			// == Session에 있는 회원 정보와 DB 데이터를 동기화

			loginMember.setMemberNickname(member.getMemberNickname());
			loginMember.setMemberTel(member.getMemberTel());
			loginMember.setMemberAddress(member.getMemberAddress());

		} else {
			message = "회원 정보 수정 실패...";

		}

		ra.addFlashAttribute("message", message);

		return "redirect:info"; // 재요청 경로 : /myPage/info GET 요청
	}

	// 비밀번호 변경 화면 이동
	@GetMapping("changePw")
	public String changePw() {

		return "myPage/myPage-changePw";
	}

	// 비밀번호 변경
	@PostMapping("changePw")
	public String changePw(@RequestParam Map<String, Object> paramMap,
			@SessionAttribute("loginMember") Member loginMember, RedirectAttributes ra) {

		// 로그인한 회원 번호
		int memberNo = loginMember.getMemberNo();

		// 서비스 호출
		int result = myPageService.changePw(paramMap, memberNo);

		String message = null;
		String path = null;

		if (result > 0) {

			message = "비밀번호가 변경되었습니다";
			path = "/myPage/info";

		} else {
			message = "현재 비밀번호가 일치하지 않습니다";
			path = "/myPage/changePw";
		}

		ra.addFlashAttribute("message", message);
		

		return "redirect:" + path;
	}

	/**
	 * 회원 프로필
	 * 
	 * @return
	 */
	@GetMapping("profile")
	public String profile() {

		return "myPage/myPage-profile";
	}

	// 회원 탈퇴 화면 이동
	@GetMapping("secession")
	public String secession() {
		return "myPage/myPage-secession";
	}

	/** 회원 탈퇴
	 * @param memberPw
	 * @param loginMember
	 * @param status : @SessionAttributes({""}) 와 반드시 같이 사용
	 * @param ra
	 * @return
	 */
	@PostMapping("secession") // /myPage/secession POST 요청 매핑
	public String secession(@RequestParam("memberPw") String memberPw,
			@SessionAttribute("loginMember") Member loginMember,
			SessionStatus status,
			RedirectAttributes ra) {

		// 로그인한 회원의 회원번호 꺼내오기
		int memberNo = loginMember.getMemberNo();

		// 서비스 호출 (입력받은 비밀번호, 로그인한 회원번호)
		int result = myPageService.secession(memberPw, memberNo);
		
		String message = null;
		String path = null;
		
		if(result > 0) {
			message = "탈퇴되었습니다.";
			path = "/";
			
			status.setComplete(); // 세션 비우기(로그아웃 상태 변경)
			
		} else {
			
			message = "비밀번호 일치하지 않습니다";
			path = "secession";
		}
		
		ra.addFlashAttribute("message", message);

		return "redirect:" + path;
	}

	// 파일 테스트 화면으로 이동
	@GetMapping("fileTest")
	public String fileTest() {
		return "myPage/myPage-fileTest";
	}
	
	/*
	 * Spring에서 파일을 처리하는 방법
	 * 
	 * - enctype="multipart/form-data"로 클라이언트의 요청을 받으면
	 * 	 (문자, 숫자, 파일 등이 섞여있는 요청)
	 * 		
	 * 	 이를 MultipartResolver(FileConfig에 정의)를 이용해서
	 * 	 섞여있는 파라미터를 분리 작업을 함
	 * 		
	 * 	 문자열, 숫자 -> String
	 * 	 파일		  -> MultipartFile
	 *   	 
	 * 
	 * */
	@PostMapping("file/test1") // /myPage/file/test1
	public String fileUpload1(@RequestParam("uploadFile") MultipartFile uploadFile,
								RedirectAttributes ra) {
		
		try {
			String path = myPageService.fileUpload1(uploadFile);
				// /myPage/file/파일명.jpg
			
			// 파일이 실제로 서버 컴퓨터에 저장이 되어
			// 웹에서 접근할 수 있는 경로가 반환되었을 때
			if(path != null) {
				ra.addFlashAttribute("path", path);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("파일 업로드 예제1 중 예외 발생");
		}
		
		return "redirect:/myPage/fileTest";
	}

	// 파일 목록 조회 화면 이동
	@GetMapping("fileList")
	public String fileList() {
		return "/myPage/fileList";
	}
	
	

}