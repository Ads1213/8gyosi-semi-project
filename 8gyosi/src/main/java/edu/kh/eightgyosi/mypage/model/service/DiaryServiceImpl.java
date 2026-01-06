package edu.kh.eightgyosi.mypage.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.eightgyosi.mypage.model.dto.DiaryDTO;
import edu.kh.eightgyosi.mypage.model.mapper.DiaryMapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class DiaryServiceImpl implements DiaryService{
	
	@Autowired
	private DiaryMapper diaryMapper; // 다이어리 메퍼 선언

	// 일기장 내용 저장 서비스
	@Override
	public int insertDiary(DiaryDTO inputDiary) {
		
		return diaryMapper.insertDiary(inputDiary);
	}
	
	// 일기장 내용 불러오기 서비스
	@Override
	public DiaryDTO selectDiary(DiaryDTO inputDiary) {
		return diaryMapper.selectDiary(inputDiary);
	}


}
