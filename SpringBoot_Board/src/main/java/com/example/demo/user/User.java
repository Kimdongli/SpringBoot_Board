package com.example.demo.user;

import com.example.demo.board.Board;
import com.example.demo.comment.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 255)
    private String access_token;

    @Column(length = 255)
    private String refresh_token;

    @Column(length = 100)
    private String platform;

    @Column(length = 50)
    @Convert(converter = StringArrayConverter.class)
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Board> boards = new LinkedList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments = new LinkedList<>();

    @Builder
    public User(Long id, String email, String password, String access_token, String refresh_token, List<String> roles,String platform, List<Board> boards, List<Comment> comments) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.roles = roles;
        this.platform = platform;
        this.boards = boards;
        this.comments = comments;
    }
    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}

