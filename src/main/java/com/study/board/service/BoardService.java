package com.study.board.service;

import com.study.board.dto.BoardDto;
import com.study.board.entity.Board;
import com.study.board.entity.Member;
import com.study.board.repository.BoardRepository;
import com.study.board.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long save(BoardDto boardDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        Member member = memberRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Board board = boardDto.toEntity(member);
        board.setViews(0);

        return boardRepository.save(board).getId();
    }

    @Transactional
    public Page<BoardDto> getPosts(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Board> boardsPage = boardRepository.findAll(pageable);

        return boardsPage.map(board -> BoardDto.builder()
                .id(board.getId())
                .author(board.getAuthor())
                .title(board.getTitle())
                .content(board.getContent())
                .createdDate(board.getCreatedDate())
                .views(board.getViews())
                .build());

    }

    @Transactional
    public BoardDto getPost(Long id){
        Board board = boardRepository.findById(id).get();

        BoardDto boardDto = BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .author(board.getAuthor())
                .content(board.getContent())
                .createdDate(board.getCreatedDate())
                .views(board.getViews())
                .member(board.getMember())
                .build();
        return boardDto;

    }
    @Transactional
    public Long update(Long id, BoardDto boardDto) {
        Board existingBoard = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 수정할 내용만 업데이트
        existingBoard.setTitle(boardDto.getTitle());
        existingBoard.setContent(boardDto.getContent());
        // 조회수는 기존 값을 유지

        return boardRepository.save(existingBoard).getId();
    }


    @Transactional
    public void delete(Long id){
        boardRepository.deleteById(id);
    }

    @Transactional
    public int updateView(Long id){
        return boardRepository.updateView(id);
    }

    public Page<BoardDto> getMyPost(int page){
        Pageable pageable = PageRequest.of(page, 8);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        Member member = memberRepository.findByEmail(currentUserEmail).
                orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다."));

        Page<Board> boards = boardRepository.findByMemberId(member.getId(), pageable);

        return boards.map(board -> BoardDto.builder()
                .id(board.getId())
                .author(board.getAuthor())
                .title(board.getTitle())
                .content(board.getContent())
                .createdDate(board.getCreatedDate())
                .views(board.getViews())
                .build());

    }

}
