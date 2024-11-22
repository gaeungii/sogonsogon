package com.study.board.dto;

import com.study.board.entity.Board;
import com.study.board.entity.Member;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Integer views = 0;
    private Member member;


    public Board toEntity(Member member){
        return Board.builder()
                .id(id)
                .title(title)
                .content(content)
                .author(author)
                .views(views)
                .member(member)
                .build();
    }

    @Builder
    public BoardDto(Long id, String title, String content, String author, LocalDateTime createdDate, LocalDateTime modifiedDate,int views, Member member) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.views = views;
        this.member = member;
    }
}
