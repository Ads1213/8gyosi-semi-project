package edu.kh.eightgyosi.board.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.kh.semiproject.board.model.dto.Board;

@Mapper
public interface MainMapper {

    List<Board> selectPopularBoard(
        @Param("boardCode") String boardCode,
        @Param("limit") int limit
    );
}