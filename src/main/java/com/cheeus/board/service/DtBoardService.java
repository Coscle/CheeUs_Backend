package com.cheeus.board.service;

import com.cheeus.board.dto.DtBoardDto;

import java.util.List;
import java.util.Optional;

public interface DtBoardService {
    List<DtBoardDto> findAll();
    Optional<DtBoardDto> findById(int id);
    void insert(DtBoardDto board);
    int responseForInsert();
    void update(DtBoardDto board);
    Integer toggleLike(int id, String authorId);
    // void toggleLikeOn(int id);
    // void toggleLikeOff(int id);
    void delete(int id);
}
