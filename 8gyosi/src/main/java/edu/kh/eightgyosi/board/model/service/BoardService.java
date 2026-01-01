package edu.kh.eightgyosi.board.model.service;

import java.util.List;
import java.util.Map;

public interface BoardService {

	/** 게시판 종류 조회 서비스
	 * @return
	 */
	List<Map<String, Object>> selectBoardTypeList();

	/** 게시판별 게시판 목록 조회 
	 * @param boardTypeNo(게시판 종류 번호)
	 * @param cp(현재 페이지)
	 * @return
	 */
	Map<String, Object> selectBoardList(int boardTypeNo, int cp);

}
