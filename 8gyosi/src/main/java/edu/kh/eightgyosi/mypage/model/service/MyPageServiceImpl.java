package edu.kh.eightgyosi.mypage.model.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.eightgyosi.member.model.dto.Member;
import edu.kh.eightgyosi.mypage.model.mapper.MyPageMapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class MyPageServiceImpl implements MyPageService {
	
	@Autowired
	private MyPageMapper myPageMapper;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;

	// 회원 정보 서비스
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
	
	// 비밀번호 변경 서비스
	@Override
	public int changePw(Map<String, Object> paramMap, int memberNo) {

		// 현재 비밀번호가 일치하는지 확인하기
		String originPw = myPageMapper.selectPw(memberNo);

		// 입력받은 현재 비밀번호와(평문)
		// DB에서 조회한 비밀번호(암호화)를 비교
		if (!bcrypt.matches((String) paramMap.get("currentPw"), originPw)) {
				return 0;
		}

		// 같을경우
		String encPw = bcrypt.encode((String) paramMap.get("newPw"));

		// 진행후 DB에 업데이트
		paramMap.put("encPw", encPw);
		paramMap.put("memberNo", memberNo);

		return myPageMapper.changePw(paramMap);
	}

	/** 회원 탈퇴 서비스
	 *
	 */
	@Override
	public int secession(String memberPw, int memberNo) {
		
		// 1. 현재 로그인한 회원의 암호화된 비밀번호를 DB에서 조회
		
		
		return 0;
	}
	
	
	
}
