package com.example.demo.entity;


import com.example.demo.DTO.CommentDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ** 내용
    @Column(length = 50)
    private String contents;

    @Column(length = 50)
    private String writer;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Comment(Long id, String contents,Board board, String writer) {
        this.id = id;
        this.contents = contents;
        this.board = board;
        this.writer = writer;
    }


    public void updateFromDTO(CommentDTO commentDTO){
        this.contents = commentDTO.getContents();
    }
}
