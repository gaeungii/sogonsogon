package com.study.board.dto;

import com.study.board.entity.Board;
import com.study.board.entity.Comment;
import com.study.board.entity.Member;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String comment;
    private String writer;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Member member;
    private Board board;
    private boolean writerFlag;

    public Comment toEntity(Member member, Board board) {
        return Comment.builder()
                .comment(comment)
                .writer(writer)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .member(member)
                .board(board)
                .build();
    }

    @Builder
    public CommentDto(Long id, String comment, String writer, LocalDateTime createdDate, LocalDateTime modifiedDate, Member member, Board board) {
        this.id = id;
        this.comment = comment;
        this.writer = writer;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.member = member;
        this.board = board;
    }

}
