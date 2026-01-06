package edu.kh.eightgyosi.mypage.model.service;

import edu.kh.eightgyosi.member.model.dto.Member;

public interface MyPageService {

	/** 회원 정보 수정 서비스
	 * @param member
	 * @param memberAddress
	 * @return
	 */
	int updateInfo(Member member, String[] memberAddress);

}
