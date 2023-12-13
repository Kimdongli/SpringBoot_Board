package com.example.demo.comment;


import com.example.demo.comment.CommentDTO;
import com.example.demo.board.Board;
import com.example.demo.user.User;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

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
