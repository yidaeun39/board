package cafe.jjdev.springboard.mapper;

import org.apache.ibatis.annotations.Mapper;

import cafe.jjdev.springboard.vo.Boardfile;

@Mapper
public interface BoardfileMapper {
	void boardfileInsert(Boardfile boardfile);
}
