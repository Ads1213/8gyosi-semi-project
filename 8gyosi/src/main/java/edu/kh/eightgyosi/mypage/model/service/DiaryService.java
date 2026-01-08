package edu.kh.eightgyosi.mypage.model.service;

import edu.kh.eightgyosi.member.model.dto.Member;
import edu.kh.eightgyosi.mypage.model.dto.DiaryDTO;

public interface DiaryService {

	/** 일기장 내용 저장 서비스
	 * @param inputDiary
	 * @return
	 */
	int insertDiary(DiaryDTO inputDiary);

	
	/** 일기장 내용 검색 서비스
	 * @param inputDiary
	 * @return
	 */
	DiaryDTO selectDiary(DiaryDTO inputDiary);

	
	/** 일기장 내용 삭제 서비스
	 * @param inputDiary
	 * @return
	 */
	int deleteDiary(DiaryDTO inputDiary);

	

	/** 일기장 작성일 중복 확인 메서드
	 * @param inputDiary
	 * @return
	 */
	int checkWriteDate(DiaryDTO inputDiary);



}
