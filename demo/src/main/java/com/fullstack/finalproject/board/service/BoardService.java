package com.fullstack.finalproject.board.service;

import java.util.List;
import java.util.Optional;

import com.fullstack.finalproject.board.dto.BoardDto;

public interface BoardService {

	public List<BoardDto> findAll();
    public Optional<BoardDto> findById(int id);
    public Optional<BoardDto> findByIdReview(int board_no);
    public List<BoardDto> findByIdRepls(int board_no);
    public void recruitInsert(BoardDto board);
    public void reviewInsert(BoardDto board);
    public void insertRepl(BoardDto board);
    public void recruitUpdate(BoardDto board);
    public void delete(int board_no);
}
