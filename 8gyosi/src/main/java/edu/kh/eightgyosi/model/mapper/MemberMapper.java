package edu.kh.eightgyosi.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.eightgyosi.model.dto.Member;

@Mapper
public interface MemberMapper {

	/** 로그인 SQL 실행
	 * @param memberEmail
	 * @return loginMember
	 */
	Member login(String memberEmail) throws Exception;
	
	

}
