package edu.kh.eightgyosi.model.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.eightgyosi.model.dto.Member;
import edu.kh.eightgyosi.model.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {
	
	private final MemberMapper mapper;
	private final BCryptPasswordEncoder bcrypt;
	
	/** 로그인 서비스
	 * @author dasol
	 * 
	 * @param member
	 * @return loginMember
	 */
	@Override
	public Member login(Member member) throws Exception {
		
		// 데이터 가공 ( 암호화 )
		String bcryptPassword = bcrypt.encode(member.getMemberPw());
		log.debug("bcryptPassword : " + bcryptPassword);
		
		// 1. 이메일이 일치하면서 탈퇴하지 않은 회원의 ( + 비밀번호 ) 조회
		Member loginMember = mapper.login(member.getMemberEmail());
		
		return null;
	}
	
	
}
