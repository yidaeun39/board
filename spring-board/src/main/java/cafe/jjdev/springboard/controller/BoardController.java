package cafe.jjdev.springboard.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cafe.jjdev.springboard.service.BoardService;
import cafe.jjdev.springboard.vo.Board;
import cafe.jjdev.springboard.vo.BoardRequest;
import cafe.jjdev.springboard.vo.Boardfile;

@Controller
public class BoardController {
	@Autowired
	private BoardService boardService;
    
	// 리스트 요청
	//@RequestMapping(value="/boardList", method = RequestMethod.GET)
	@GetMapping(value="/boardList")
	public String boardList(Model model,
    						// required -> 넘겨받지 않을 수도 있음, defaultValue -> 그럴경우 1로
    						@RequestParam(value="currentPage", required=false, defaultValue="0") int currentPage) {
		Map<String, Object> returnMap = boardService.getBoardList(currentPage);
		model.addAttribute("map", returnMap);
        return "boardList";
	}
	
	// 보드 입력 VIEW 요청
	@GetMapping(value="/boardAdd")
		public String boardAdd() {
			return "boardAdd";
	}

   @PostMapping(value="/boardAdd")
   public String boardAdd(BoardRequest boardRequest, HttpSession session) {
	   // Servlet API는 Controller에서만 사용해야 함.
	   // 커맨드 객체 사용을 위해 -> 필드 name = input type의 name이 같아야 setter 호출 가능함.
	   /*
	    * Service에서
	    * 1. Board안에 fileList를 분해하여 DB에 들어갈 수 있는 형태로 작업해야 함
	    * 2. 파일 저장 : 파일의 경로가 필요
	    */
	   System.out.println(boardRequest.getFiles());
	   String path = session.getServletContext().getRealPath("/img");
	   System.out.println("path값이 뭘까? -> "+ path);
	   boardService.addBoard(boardRequest, path);
	   return "redirect:/boardList"; // 글입력후 "/boardList"로 리다이렉트(재요청)
   }
   
  @GetMapping(value="/boardRemove")
  public String boardRemove(@RequestParam(value="boardNo") int boardNo, Model model) {
	  									// 이름이 같으면 value 생략 가능
	  Board resultboard = boardService.getBoard(boardNo);
	  model.addAttribute("board", resultboard);
	  return "boardRemove";
  }
  
  @PostMapping(value="/boardRemove")
  public String boardRemove(Board board) {
	  int result = boardService.removeBoard(board);
	  System.out.println("DELETE 쿼리 실행 여부 -> " + result);
	  return "redirect:/boardList";
  }
  
  @GetMapping(value="/boardModify")
  public String boardSelect(Board board, Model model) {
	  Board resultboard = boardService.getBoard(board.getBoardNo());
	  model.addAttribute("board", resultboard);
	  return "boardModify";
  }
  
  @PostMapping(value="/boardModify")
  public String boardModify(Board board) {
	  int result = boardService.modifyBoard(board);
	  System.out.println("UPDATE 쿼리 실행 여부 -> " + result);
	  return "redirect:/boardList";
  }
  
  @GetMapping(value="/index")
  public String main() {
	  return "index";
  }
  
  @GetMapping(value="/boardDetail")
  public String boardDetail(Board board, Model model) {
	  Board resultboard = boardService.getBoard(board.getBoardNo());
	  List<Boardfile> boardfile = boardService.addFile(board);
	  model.addAttribute("board", resultboard);
	  // 업로드한 이미지 정보가 담겨있는 list를 화면으로 보냄
	  model.addAttribute("boardfile", boardfile);
	  return "boardDetail";
  }
}