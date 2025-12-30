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
	 * @throws Exception 
	 * @return loginMember
	 */
	@Override
	public Member login(Member member) throws Exception {
		
		String bcryptPassword = bcrypt.encode(member.getMemberPw());
		log.debug("bcryptPassword : " + bcryptPassword);
		
		
		return null;
	}
}
