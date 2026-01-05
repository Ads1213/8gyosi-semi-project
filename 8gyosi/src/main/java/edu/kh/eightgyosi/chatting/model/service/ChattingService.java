package edu.kh.eightgyosi.chatting.model.service;

import java.util.List;
import java.util.Map;

import edu.kh.eightgyosi.chatting.model.dto.ChattingRoom;
import edu.kh.eightgyosi.member.model.dto.Member;

public interface ChattingService {
	/** 채팅방 목록 조회
	 * @param memberNo
	 * @return
	 */
	List<ChattingRoom> selectRoomList(int memberNo);

	/** 채팅 상대 검색
	 * @param map
	 * @return
	 */
	List<Member> selectTarget(Map<String, Object> map);
}
