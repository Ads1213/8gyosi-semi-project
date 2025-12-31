package edu.kh.eightgyosi.model.service;

import edu.kh.eightgyosi.model.dto.Member;

public interface MemberService {
	
	/** 로그인 서비스
	 * @author dasol
	 * 
	 * @param member
	 * @return loginMember
	 * 
	 */
	Member login(Member member) throws Exception;

}
