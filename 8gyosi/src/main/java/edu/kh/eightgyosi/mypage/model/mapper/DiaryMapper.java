package edu.kh.eightgyosi.mypage.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.eightgyosi.member.model.dto.Member;
import edu.kh.eightgyosi.mypage.model.dto.DiaryDTO;

@Mapper
public interface DiaryMapper {

	/** 일기장 내용 저장 서비스
	 * @param inputDiary
	 * @return
	 */
	int insertDiary(DiaryDTO inputDiary);



}
