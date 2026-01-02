package edu.kh.eightgyosi.mypage.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.eightgyosi.member.model.dto.Member;
import edu.kh.eightgyosi.mypage.model.dto.CalenderDTO;

@Mapper
public interface CalenderMapper {

	List<CalenderDTO> selectCalender(int memberNo);

	
	
}
