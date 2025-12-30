package edu.kh.eightgyosi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
	
	private int memberNo;
	private String memberEmail;
	private String memberPw;
	private String memberNickName;
	private String memberTel;
	private String memberSchool;
	private String memberBirth;
	private String memberAddress;
	private String profileImg;
	private String enrollDate;
	private String memberDelFl;
	private int authority;
	private String memberBg;

}
