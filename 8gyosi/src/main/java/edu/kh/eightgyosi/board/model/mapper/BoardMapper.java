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

	/** 검색 조건에 맞는 게시글 수 조회 SQL
	 * @param paramMap
	 * @return
	 */
	int getSearchCount(Map<String, Object> paramMap);

	/** 검색 결과 목록 조회 SQL
	 * @param paramMap
	 * @param rowBounds
	 * @return
	 */
	List<Board> selectSearchList(Map<String, Object> paramMap, RowBounds rowBounds);

	/** 게시글 상세 조회 SQL 수행 (BOARD/BOARD_IMG/COMMENT)
	 * @param map
	 * @return
	 */
	Board selectOne(Map<String, Integer> map);

	/** 조회수 1증가 SQL 수행
	 * @param boardId
	 * @return
	 */
	int updateReadCount(int boardId);

	/** 조회수 조회 SQL 수행
	 * @param boardId
	 * @return
	 */
	int selectReadCount(int boardId);

	/** 좋아요 해체
	 * @param map
	 * @return
	 */
	int deleteBoardLike(Map<String, Integer> map);

	/** 좋아요 체크
	 * @param map
	 * @return
	 */
	int insertBoardLike(Map<String, Integer> map);

	/** 게시글 좋아요 갯수 조회
	 * @param integer
	 * @return
	 */
	int selectLikeCount(int boardId);
}