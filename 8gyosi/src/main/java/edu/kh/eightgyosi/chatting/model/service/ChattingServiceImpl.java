package edu.kh.eightgyosi.chatting.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.eightgyosi.chatting.model.dto.ChattingRoom;
import edu.kh.eightgyosi.chatting.model.mapper.ChattingMapper;
import edu.kh.eightgyosi.member.model.dto.Member;

@Service
@Transactional(rollbackFor = Exception.class)
public class ChattingServiceImpl implements ChattingService{
	
	@Autowired
	private ChattingMapper mapper;
	
	// 채팅방 목록 조회
	@Override
	public List<ChattingRoom> selectRoomList(int memberNo) {
		return mapper.selectRoomList(memberNo);
	}
	
	
	// 채팅 상대 검색 서비스
	@Override
	public List<Member> selectTarget(Map<String, Object> map) {
		return mapper.selectTarget(map);
	}
		
	
}
