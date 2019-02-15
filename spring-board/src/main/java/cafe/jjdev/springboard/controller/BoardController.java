package cafe.jjdev.springboard.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cafe.jjdev.springboard.service.BoardService;
import cafe.jjdev.springboard.vo.Board;

@Controller
public class BoardController {
	@Autowired
	private BoardService boardService;
    
	// 리스트 요청
	//@RequestMapping(value="/boardList", method = RequestMethod.GET)
	@GetMapping(value="/boardList")
	public String boardList(Model model,
    						// required -> 넘겨받지 않을 수도 있음, defaultValue -> 그럴경우 1로
    						@RequestParam(value="currentPage", required=false, defaultValue="1") int currentPage) {
		Map<String, Object> returnMap = boardService.getBoardList(currentPage);
		model.addAttribute("map", returnMap);
        return "boardList";
	}
	
	@GetMapping(value="/boardAdd")
		public String boardAdd() {
			return "boardAdd";
	}

   @PostMapping(value="/boardAdd")
   public String boardAdd(Board board) {
	   // 커맨드 객체 사용을 위해 -> 필드 name = input type의 name이 같아야 setter 호출 가능함.
	   int result = boardService.addBoard(board);
	   System.out.println("INSERT 쿼리 실행 여부 -> " + result);
	   return "redirect:/boardList"; // 글입력후 "/boardList"로 리다이렉트(재요청)
   }
   
  @GetMapping(value="/boardRemove")
  public String boardRemove(@RequestParam(value="boardNo") int boardNo, Model model) {
	  									// 이름이 같으면 value 생략 가능
	  model.addAttribute("boardNo", boardNo);
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
  
}