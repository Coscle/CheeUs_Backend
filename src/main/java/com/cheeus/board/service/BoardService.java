package com.cheeus.board.service;

import com.cheeus.board.dto.BoardDto;

import java.util.List;
import java.util.Optional;

public interface BoardService {
	Integer findLatest();
    List<BoardDto> findAll();
    List<BoardDto> findAllFreeboard();
    List<BoardDto> findAllShortform();
    List<BoardDto> findAllEventboard();
    Optional<BoardDto> findById(int id);
    void insert(BoardDto board);
    void update(BoardDto board);
    void delete(int id);
}
