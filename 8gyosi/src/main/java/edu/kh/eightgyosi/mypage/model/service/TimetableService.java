package edu.kh.eightgyosi.mypage.model.service;

import java.util.List;

import edu.kh.eightgyosi.mypage.model.dto.TimetableDTO;

public interface TimetableService {

	List<TimetableDTO> selectTimetable(int memberNo);
	
}
