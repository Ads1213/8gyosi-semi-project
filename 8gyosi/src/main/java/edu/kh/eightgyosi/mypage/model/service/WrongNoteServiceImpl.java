package edu.kh.eightgyosi.mypage.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.kh.eightgyosi.mypage.model.dto.WrongNoteDTO;
import edu.kh.eightgyosi.mypage.model.mapper.MyPageMapper;

@Service
public class WrongNoteServiceImpl implements WrongNoteService{

	@Autowired
	private MyPageMapper wroMapper; // 오답노트 매퍼 선언
	
	/**
	 * 오답노트 정보 조회 서비스
	 */
	@Override
	public List<WrongNoteDTO> selectWrongNote(int memberNo) {
		
		return wroMapper.selectWrongNote(memberNo);
	}
	
}
