package edu.kh.eightgyosi.board.model.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.eightgyosi.board.model.dto.Board;
import edu.kh.eightgyosi.board.model.dto.pagination;
import edu.kh.eightgyosi.board.model.mapper.BoardMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class BoardSerivceImpl implements BoardService{
	
	@Autowired
	private BoardMapper mapper;
	
	
	/** 게시판 종류 조회 서비스
	 *
	 */
	@Override
	public List<Map<String, Object>> selectBoardTypeList() {
			return mapper.selectBoardTypeList();
	}
	
	// 게시판별 게시판 목록 조회
	@Override
	public Map<String, Object> selectBoardList(int boardTypeNo, int cp) {

		int listCount = mapper.getListCount(boardTypeNo);
		
		pagination pagination = new pagination(cp, listCount);
		
		
		int limit = pagination.getLimit(); // 10개
		int offset = (cp -1 ) * limit;
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		List<Board> boardList = mapper.selectBoardList(boardTypeNo, rowBounds);
		
		
		return null;
	}
}
