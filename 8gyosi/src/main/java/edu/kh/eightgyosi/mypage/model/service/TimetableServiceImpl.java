package edu.kh.eightgyosi.mypage.model.service;

import java.time.LocalDate;
import java.util.ArrayList;
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
	public List<TimetableDTO> selectTimetable(int memberNo, String semester) {
		
		
		
		Map<String, Object> map = new HashMap<>();
		
		if(semester == null || semester.isEmpty()) {
			// 3-1. 현재 연도와 월 구해서 전달
			// 		학기 : 9~12/1~2 : 1학기, 3~8 : 2학기
			LocalDate time = LocalDate.now();
			int year = time.getYear();
			int month = time.getMonthValue();
			
			if (month <= 2 || month >= 9) {
				year -= 1;
				semester = Integer.toString(year) + "-2"; // ex > 연도 + 2 : 2026-2 형태를 String 으로 저장
			}else {
				semester = Integer.toString(year) + "-1";
			}
			
			map.put("semester", semester);
			log.debug("test: "+ semester);
		}
		
		if(!semester.isEmpty()) {
			
			map.put("semester", semester);
			log.debug("test: "+ semester);
		}
		
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
	
	@Override
	public int insertTimetable(Map<String, Object> map, int memberNo) {
		
		log.debug(map.toString());
		// 가져온 map 가공
		String year = (String)map.get("tyear");
		String period = (String)map.get("tperiod");
		String semester = year + "-" + period;
		
		// mapper 로 보낼 map 생성
		Map<String, Object> allMap = new HashMap<String, Object>();
		
		// map 에 담을 임시 list 생성
		List<String> list = new ArrayList<>();
		// map.entrySet() : Map 객체에 저장된 모든 k-v 쌍을 꺼내온다
		// .stream() : 데이터를 순차적으로 검사하는 통로를 여는 함수
		// .filter(): stream() 과 함께 사용되어, 해당 통로의 요소들을 조건에 맞게 걸러내는 함수
		// stream 에서 흐르는 모든 요소들을 temp -> 통하여 temp라는 변수를 설정
		// 해당 temp 에서 정규식(\\d{2}, 2자리 숫자)을 만족하는 모든 key 값을 꺼내옴
		map.entrySet().stream().filter(temp -> temp.getKey().matches("\\d{2}"))
			.forEach(temp -> {
				String day = temp.getKey().substring(0,1);
				String cls = temp.getKey().substring(1,2);
				String subject = (String) temp.getValue();
				String dayClassSubject = day+cls+subject;
				list.add(dayClassSubject);
			});
		
		// 학기 정보 map 에 등록
		allMap.put("semester", semester);
		
		// memberNo 등록
		allMap.put("memberNo", memberNo);
		
		// 요일-교시-과목 등록
		for(int i =0; i < list.size(); i ++) {
			map.put("dayClassSubject"+(i+1), list.get(i));
		}
		
		int result = mapper.insertTimetable(allMap);
	
		return result;
	}
	
}
