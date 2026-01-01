package edu.kh.eightgyosi.board.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import edu.kh.eightgyosi.board.model.dto.Board;

@Mapper
public interface BoardMapper {

	/** 게시판 종류 조회 SQL
	 * @return
	 */
	List<Map<String, Object>> selectBoardTypeList();

	/** 게시글 수 조회 SQL
	 * @param boardTypeNo
	 * @return
	 */
	int getListCount(int boardTypeNo);

	/** 게시판별 페이지 목록 조회 SQL 수행
	 * @param boardTypeNo
	 * @param rowBounds
	 * @return
	 */
	List<Board> selectBoardList(int boardTypeNo, RowBounds rowBounds);

	
}
