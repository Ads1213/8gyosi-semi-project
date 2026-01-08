package edu.kh.eightgyosi.mypage.model.service;

import java.util.List;
import java.util.Map;

import edu.kh.eightgyosi.mypage.model.dto.TimetableDTO;

public interface TimetableService {

	List<TimetableDTO> selectTimetable(int memberNo, String semester);

	int insertTimetable(Map<String, Object> map, int memberNo);
	
}
