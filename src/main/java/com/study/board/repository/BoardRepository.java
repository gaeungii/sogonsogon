package com.study.board.repository;

import com.study.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Modifying
    @Query("update Board b set b.views = b.views + 1 where b.id =  :id")
    int updateView(Long id);

    List<Board> findByMemberId(Long memberId);
    Page<Board> findAll(Pageable pageable);
    Page<Board> findByMemberId(Long memberId, Pageable pageable);

}
