package edu.kh.eightgyosi.chatting.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import edu.kh.eightgyosi.chatting.model.dto.ChattingRoom;
import edu.kh.eightgyosi.member.model.dto.Member;

@Mapper
public interface ChattingMapper {

	/** 채팅방 검색 SQL
	 * @param memberNo
	 * @return
	 */
	List<ChattingRoom> selectRoomList(int memberNo);

	/** 채팅상대 검색 SQL 
	 * @param map
	 * @return
	 */
	List<Member> selectTarget(Map<String, Object> map);
	
	

}
