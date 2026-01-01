package edu.kh.eightgyosi.member.model.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.eightgyosi.member.model.dto.Member;
import edu.kh.eightgyosi.member.model.mapper.MemberMapper;
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
		
		// 2. 만약에 일치하는 이메일이 없어서 조회 결과가 null 인 경우
		if(loginMember == null) return null;
		
		// 3. 입력받은 비밀번호(평문 : member.getMemberPw()) 와
		// 	  암호화된 비밀번호(loginMember.getMemberPw())
		//	  두 비밀번호가 일치하는지 확인
		
		// bcrypt.matches(평문, 암호화) : 평문과 암호화가 내부적으로
		//					일치한다고 판단이 되면 true , 아니면 false
		if(!bcrypt.matches(member.getMemberPw(), 
						loginMember.getMemberPw())) return null;
		
		// 로그인한 회원 정보에서 비밀번호 제거
		loginMember.setMemberPw(null);
		
		return loginMember;
	}
	
	
}
