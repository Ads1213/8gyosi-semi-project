package edu.kh.eightgyosi.mypage.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.eightgyosi.member.model.dto.Member;
import edu.kh.eightgyosi.mypage.model.dto.DiaryDTO;

@Mapper
public interface DiaryMapper {

	/** 일기장 내용 저장 SQL
	 * @param inputDiary
	 * @return
	 */
	int insertDiary(DiaryDTO inputDiary);

	/** 일기장 내용 불러오기 SQL
	 * @param inputDiary
	 * @return
	 */
	DiaryDTO selectDiary(DiaryDTO inputDiary);



}
