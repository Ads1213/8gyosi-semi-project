package edu.kh.eightgyosi.chatting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
	private int messageNo;			// 메시지 번호
	private String messageContent;  // 메시지 내용
	private String readFl; 			// 읽음 여부
	private int senderNo; 			// 보낸 회원
	private int chattingRoomNo; 	// 채팅방 번호
	private int targetNo;			// 웹소켓을 이용한 메시지 값 세팅시 필요

	private String sendDate;	   // 마지막 메세지 보낸 날짜(01월 04일)
								   // - 체팅 목록 옆에 붙여주기
	private String sendYearDate;   // 마지막 메시지 년도 포함된 보낸 날짜(2026년 01월 04일) 
								   // - 체팅이 다음날 시작되면 메세지창에 뿌려주기
	private String sendTime;	   // 마지막 메시지 보낸 시간(20:03)
								   // - 채팅 내용 옆에 붙여주기
	
	
	
}
