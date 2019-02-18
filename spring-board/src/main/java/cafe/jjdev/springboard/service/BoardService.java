package cafe.jjdev.springboard.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cafe.jjdev.springboard.mapper.BoardMapper;
import cafe.jjdev.springboard.vo.Board;

// interface로 디커플링 가능
// Transaction -> 예외 발생시 실행 취소
// (DB를 변경할 때만. SELECT는 DB에 영향을 주지 않기 때문에 Transaction 상황이 아님)
@Service
@Transactional
public class BoardService {
	@Autowired
	private BoardMapper boardMapper;
	
	public Board getBoard(int boardNo) {
		return boardMapper.selectBoard(boardNo);
	}
	
	public Map<String, Object> getBoardList(int currentPage) {
		final int ROW_PER_RAGE = 10;
		Map<String, Integer> map = new HashMap<String, Integer>();
		// 1.
		// vo.setCurrentPage(currentPage)
		map.put("currentPage", currentPage);
		map.put("rowPerPage", ROW_PER_RAGE);
		// 2.
		int boardCount = boardMapper.selectBoardCount();
		// Math.ceil -> 올림 함수
		int lastPage = (int)(Math.ceil(boardCount / ROW_PER_RAGE));
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("list", boardMapper.selectBoardList(map));
		returnMap.put("boardCount", boardCount);
		returnMap.put("lastPage", lastPage);
		returnMap.put("currentPage", currentPage);
		
		return returnMap;
	}
	
	public int getBoardCount() {
		return boardMapper.selectBoardCount();
	}
	
	public int addBoard(Board board) {
		return boardMapper.insertBoard(board);
	}

	public int removeBoard(Board board) {
		return boardMapper.deleteBoard(board);
	}
	
	public int modifyBoard(Board board) {
		return boardMapper.updateBoard(board);
	}
}
