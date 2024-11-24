package com.study.board.service;

import com.study.board.dto.CommentDto;
import com.study.board.entity.Board;
import com.study.board.entity.Comment;
import com.study.board.entity.Member;
import com.study.board.repository.BoardRepository;
import com.study.board.repository.CommentRepository;
import com.study.board.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public Long save(CommentDto commentDto, Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        Member member = memberRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Board board = boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다. " + id));

        Comment comment = commentDto.toEntity(member,board);

        return commentRepository.save(comment).getId();
    }

    @Transactional
    public List<CommentDto> getComment(Long id){
        Board board = boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해딩 게시글이 존재하지 않습니다"));

        List<Comment> comments = board.getComments();

        List<CommentDto> commentDtos = comments.stream()
                .map(comment -> new CommentDto(
                        comment.getId(),
                        comment.getComment(),
                        comment.getWriter(),
                        comment.getCreatedDate(),
                        comment.getModifiedDate(),
                        comment.getMember(),
                        comment.getBoard()
                ))
                .collect(Collectors.toList());

        return commentDtos;
    }

    @Transactional
    public void update(Long id, CommentDto commentDto) {
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        comment.update(commentDto.getComment());
    }

    @Transactional
    public void delete(Long id){
        commentRepository.deleteById(id);
    }

}
