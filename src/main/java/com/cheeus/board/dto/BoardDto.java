package com.cheeus.board.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BoardDto {

	// 일반 게시판 DB
	private int id;
	@JsonProperty("author_id")
	private String authorId;
	private String nickname;
	private int category;
	private String title;
	private String content;
	private String writeday;
	private int views;
	private int like;
	@JsonProperty("repl_cnt")
	private int replCnt;
	private boolean pinned;
	private boolean hidden;
	
	// Board join table 기본속성
	private String boardId;

	// FreePostPhoto 속성들
	private int photoes;
	
	// FreeBoard 속성들
	private String head;

	// Shorts 속성들
//	@Setter
//    private String media;

	// Notices 속성들
	private String createdAt;

}
