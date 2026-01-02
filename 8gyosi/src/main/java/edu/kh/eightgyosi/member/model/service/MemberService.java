package edu.kh.eightgyosi.member.model.service;

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

}
