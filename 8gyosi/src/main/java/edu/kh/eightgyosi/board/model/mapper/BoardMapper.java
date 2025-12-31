package edu.kh.eightgyosi.board.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {

	/** 게시판 종류 조회 SQL
	 * @return
	 */
	List<Map<String, Object>> selectBoardTypeList();

}
