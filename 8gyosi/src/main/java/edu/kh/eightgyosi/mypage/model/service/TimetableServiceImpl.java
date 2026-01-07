package edu.kh.eightgyosi.mypage.model.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.kh.eightgyosi.mypage.model.dto.TimetableDTO;
import edu.kh.eightgyosi.mypage.model.mapper.MyPageMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TimetableServiceImpl implements TimetableService{
	
	@Autowired
	private MyPageMapper mapper;
	
	/**
	 * 시간표 조회 서비스
	 */
	@Override
	public List<TimetableDTO> selectTimetable(int memberNo) {
		
		// 3-1. 현재 연도와 월 구해서 전달
		// 		학기 : 9~12/1~2 : 1학기, 3~8 : 2학기
		LocalDate time = LocalDate.now();
		String semester = null;
		
		int year = time.getYear();
		int month = time.getMonthValue();
		
		if (month <= 2 || month >= 9) {
			year -= 1;
			semester = Integer.toString(year) + "-2"; // ex > 연도 + 2 : 2026-2 형태를 String 으로 저장
		}else {
			semester = Integer.toString(year) + "-1";
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("semester", semester);
		map.put("memberNo", memberNo);
		
		// log.debug(semester);
		// log.debug("testttt : " + mapper.selectTimetable(map));
		
		List<TimetableDTO> result = mapper.selectTimetable(map);
		
		// result 결과는 DTO 객체로 이루어져 있고, 이때 DTO DAY_CLASS_SUBJECT 는 '1-2-과목' 형태로 되어 있음
		// 아래 코드는 이를 분리 하여 '1(day,요일), 2(class,교시), 3(subject,과목) 으로 분리하고 이를 다시 결과에 담아주는 작업
		int day = 0;
		int cls = 0;
		String subject = null;
		
		for(int i = 0; i < result.size(); i++) {
			String dcs = result.get(i).getDayClassSubject();
			String[] dcsPart = dcs.split("-");
			day = Integer.parseInt(dcsPart[0]);
			cls = Integer.parseInt(dcsPart[1]);
			subject = dcsPart[2];
			result.get(i).setDay(day);
			result.get(i).setCls(cls);
			result.get(i).setSubject(subject);
		}
		
		// test: log.debug(result.toString());
		
		return result;
	}
	
	
}
