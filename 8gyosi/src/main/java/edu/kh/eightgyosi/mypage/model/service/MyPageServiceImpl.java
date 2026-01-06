package edu.kh.eightgyosi.mypage.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.eightgyosi.member.model.dto.Member;
import edu.kh.eightgyosi.mypage.model.mapper.MyPageMapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class MyPageServiceImpl implements MyPageService {
	
	@Autowired
	private MyPageMapper myPageMapper;

	/** 회원 정보 수정 서비스
	 *
	 */
	@Override
	public int updateInfo(Member member, String[] memberAddress) {
		
		// 입력된 주소가 있을 경우
		// A^^^B^^^C 형태로 가공
		
		// 주소가 입력되었을 때
		if(!member.getMemberAddress().equals(",,")) {
			String address = String.join("^^^", memberAddress);
			member.setMemberAddress(address);
			
			
		} else {
			// 주소가 입력되지 않았을 때
			member.setMemberAddress(null);
		}
		
		return myPageMapper.updateInfo(member);
	}
	
	
	
}
