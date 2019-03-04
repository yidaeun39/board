package cafe.jjdev.springboard.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import cafe.jjdev.springboard.vo.Board;
import cafe.jjdev.springboard.vo.Boardfile;

@Mapper
public interface BoardfileMapper {
	void boardfileInsert(Boardfile boardfile);
	
	// 파일 List를 SELECT하는 쿼리문을 호출하는 메서드 선언
	List<Boardfile> boardfileSelect(Board board);
	
}
