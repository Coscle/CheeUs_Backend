package com.cheeus.board.controller;

import com.cheeus.board.dto.DtBoardDto;
import com.cheeus.board.mapper.DtBoardMapper;
import com.cheeus.board.service.DtBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/dtBoard")
public class DtBoardController {

	@Autowired
	private DtBoardService boardService;
    @Autowired
    private DtBoardMapper dtBoardMapper;

	@GetMapping("/")
	public List<DtBoardDto> getBoard(){
		return boardService.findAll();
	}

	@GetMapping("/post/{id}")
	public Optional<DtBoardDto> getBoardById(@PathVariable("id") int id){
		return boardService.findById(id);
	}

	@GetMapping("/{id}")
	public Optional<DtBoardDto> getBoardById2(@PathVariable("id") int id){
		return boardService.findById(id);
	}
	
	@GetMapping("/userLiked/{id}")
	public ResponseEntity<?> userLiked(@PathVariable("id") int id, @RequestParam("authorId") String authorId){
		Map<String, Object> response = new HashMap<>();
		response.put("userIsLiked", boardService.isLikedByUser(id, authorId));
		return ResponseEntity.ok(response);
	}

	@PostMapping("/insert")
	public int insertBoard(@RequestBody DtBoardDto board) {
		boardService.insert(board);
		return boardService.responseForInsert();
	}

	@PutMapping("/update")
	public String updateBoard(@RequestBody DtBoardDto board) {
		Optional<DtBoardDto> existing = boardService.findById(board.getId());
		if (existing.isPresent()) {
			boardService.update(board);
			return "update 성공";
		} else {
			return "update 실패";
		}
	}
	
	@DeleteMapping("/delete/{id}")
	public String delete(@PathVariable("id") int id) {
		Optional<DtBoardDto> existing = boardService.findById(id);
		if (existing.isPresent()) {
			boardService.delete(id);
			return id + "번 게시글 delete 성공";
		} else {
			return "delete 실패";
		}
	}

	// 좋아요 토글
	@PutMapping("/toggleLike/{id}")
	public ResponseEntity<?> toggleLike(@PathVariable("id") int id, @RequestParam("authorId") String authorId) {
		try {
			Integer updatedLikeCount = boardService.toggleLike(id, authorId);


			// isLikedByUser 메서드가 null을 반환할 수 있는 경우를 방지
			Boolean isLiked = boardService.isLikedByUser(id, authorId);
			if (isLiked == null) {
				isLiked = false;  // 기본값 설정
			}

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("updatedLikeCount", updatedLikeCount);
			response.put("isLiked", isLiked);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to toggle like for post: " + id + ". Error: " + e.getMessage());
		}
	}
	
	// 조회수 조회 & 증가
	@PutMapping("/incrementView/{id}")
	public ResponseEntity<?> incrementViewCount(@PathVariable("id") int id) {
		try {
			int updatedViewCount = boardService.incrementViewCount(id);
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("updatedViewCount", updatedViewCount);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to increment view count for post: " + id);
		}
	}

}

