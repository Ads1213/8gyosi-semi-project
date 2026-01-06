package edu.kh.eightgyosi.mypage.model.service;

import java.util.Map;

import edu.kh.eightgyosi.member.model.dto.Member;

public interface MyPageService {

	/** 회원 정보 수정 서비스
	 * @param member
	 * @param memberAddress
	 * @return
	 */
	int updateInfo(Member member, String[] memberAddress);
	
	/** 회원 비밀번호 변경 서비스
	 * @param paramMap
	 * @param memberNo
	 * @return
	 */
	int changePw(Map<String, Object> paramMap, int memberNo);

	/** 회원 탈퇴 서비스
	 * @param memberPw
	 * @param memberNo
	 * @return
	 */
	int secession(String memberPw, int memberNo);


	

}
