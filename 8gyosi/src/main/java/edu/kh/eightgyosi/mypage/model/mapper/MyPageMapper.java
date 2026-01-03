package edu.kh.eightgyosi.mypage.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.eightgyosi.member.model.dto.Member;
import edu.kh.eightgyosi.mypage.model.dto.CalenderDTO;
import edu.kh.eightgyosi.mypage.model.dto.WrongNoteDTO;

@Mapper
public interface MyPageMapper {

	/** memberNo로 캘린더 조회 SQL
	 * @param memberNo
	 * @return
	 */
	List<CalenderDTO> selectCalender(int memberNo);

	/** memberNo로 오답노트 조회 SQL(제일 최신 거만)
	 * @param memberNo
	 * @return
	 */
	List<WrongNoteDTO> selectWrongNote(int memberNo);

	
	
}
