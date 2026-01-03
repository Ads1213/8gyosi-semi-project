package edu.kh.eightgyosi.member.model.service;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.eightgyosi.member.model.dto.Member;

public interface MemberService {
	
	/** 로그인 서비스
	 * 
	 * @param member
	 * @return loginMember
	 * 
	 */
	Member login(Member member) throws Exception;

	/** 이메일 중복검사 서비스
	 * 
	 * @param memberEmail
	 * @return
	 */
	int checkEmail(String memberEmail);

	/** 닉네임 중복 검사
	 * @param nickname
	 * @return
	 */
	String checkNickname(String nickname);

	/** 프로필 이미지 변경 서비스
	 * @param profileImg
	 * @return 
	 */
	int profile(MultipartFile profileImg) throws Exception;

}
