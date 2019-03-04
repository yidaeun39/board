package cafe.jjdev.springboard.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import cafe.jjdev.springboard.mapper.BoardMapper;
import cafe.jjdev.springboard.mapper.BoardfileMapper;
import cafe.jjdev.springboard.vo.Board;
import cafe.jjdev.springboard.vo.BoardRequest;
import cafe.jjdev.springboard.vo.Boardfile;

// interface로 디커플링 가능
// Transaction -> 예외 발생시 실행 취소(롤백)
// (DB를 변경할 때만! SELECT는 DB에 영향을 주지 않기 때문에 Transaction 상황이 아님)
@Service
@Transactional
public class BoardService {
	@Autowired
	private BoardMapper boardMapper;
	@Autowired
	private BoardfileMapper boardfileMapper;
	
	public Board getBoard(int boardNo) {
		return boardMapper.selectBoard(boardNo);
	}
	
	public Map<String, Object> getBoardList(int currentPage) {
		final int ROW_PER_RAGE = 10;
		Map<String, Integer> map = new HashMap<String, Integer>();
		// 1.
		// vo.setCurrentPage(currentPage)
		map.put("currentPage", currentPage * 10);
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
	
	// Board 추가
	public void addBoard(BoardRequest boardRequest, String path) {
		/*
		 * 1. BoardRequest를 분리 : board, file, file정보
		 * 2. board -> boardVo
		 * 3. file정보 -> boardFileVO
		 * 4. file -> path를 이용해 물리적 장치에 저장
		 */
		// board 객체에 화면에서 입력받은 값을 저장
		Board board = new Board();
		board.setBoardTitle(boardRequest.getBoardTitle());
		board.setBoardUser(boardRequest.getBoardUser());
		board.setBoardPw(boardRequest.getBoardPw());
		board.setBoardContent(boardRequest.getBoardContent());
		board.setBoardDate(boardRequest.getBoardDate());
		boardMapper.insertBoard(board); 	// 쿼리를 통해 boardNo 값 구할 수 있음
		System.out.println("selectKey 제대로 되나????? -> " + board.getBoardNo());
		// 화면에서 입력받은 files를 list에 저장
		List<MultipartFile> files = boardRequest.getFiles();
		// files만큼 for문 반복
		for(MultipartFile f : files) {
			Boardfile boardfile = new Boardfile();
			// f -> boardfile
			// boardfile 객체에 반복되는 값 저장
			boardfile.setBoardNo(board.getBoardNo());
			boardfile.setFileType(f.getContentType());
			boardfile.setFileSize(f.getSize());
			
			// 화면에서 입력한 파일의 이름을 문자열에 저장하고
			String originalFilename = f.getOriginalFilename();
			// . 이후의 글자수를 변수 i에 저장
			int i = originalFilename.lastIndexOf(".");	// find(".")
			// 다시 그 값에 1을 더해 . 이후의 문자열을 ext 변수에 저장
			String ext = originalFilename.substring(i+1);
			// 확장자를 추출해 객체에 저장
			boardfile.setFileExt(ext);
			
			// 파일명을 랜덤으로 생성
			String fileName = UUID.randomUUID().toString();
			// 파일의 정보가 저장된 객체를 매개변수로 보내 INSERT문에 넣고 실행
			boardfile.setFileName(fileName);
			// 파일의 정보가 저장된 객체를 보내 INSERT문 실행
			boardfileMapper.boardfileInsert(boardfile);
			// 3 파일 저장
			try {
				// new 생성자 메서드를 이용해 File을 생성하고 지정한 경로에 이름, 확장자를 넣고 저장
                f.transferTo(new File(path+"/"+fileName+"."+ext));
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
		}	
	}

	public int removeBoard(Board board) {
		return boardMapper.deleteBoard(board);
	}
	
	public int modifyBoard(Board board) {
		return boardMapper.updateBoard(board);
	}
}
