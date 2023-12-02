package com.example.demo.entity;

import com.example.demo.DTO.BoardDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Board {

    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 작성자 이름
    @Column(length = 50)
    private String username;

    // 게시물 제목
    @Column(length = 50)
    private String title;

    // 내용
    @Column(length = 50)
    private String contents;

    // 최초 작성 시간
    private LocalDateTime createTime;

    // 최근 수정 시간
    private LocalDateTime updateTime;

    // ** 1:다
    // ** 소유 & 비소유
    // ** cascade = CascadeType.REMOVE : 소유한쪽에서 데이터를 지웠을때 가지고있을필요없을때 자동으로 지운다.
    // ** EX) 게시물이 삭제되면 댓글을 자동으로 지워준다.
    // ** orphanRemoval = true : 연결 관계가 끊어지면 삭제.
    // ** fetch = FetchType.LAZY : 지연로딩
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE,orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Comment> comment = new ArrayList<>();


    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE,orphanRemoval = true,fetch = FetchType.LAZY)
    private List<BoardFile> files = new ArrayList<>();
    @Builder
    public Board(Long id, String username, String title, String contents, LocalDateTime createTime, LocalDateTime updateTime) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.contents = contents;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public void updateFromDTO(BoardDTO boardDTO){
        // ** 모든 변경 사항을 셋팅.

        this.title= boardDTO.getTitle();
        this.contents = boardDTO.getContents();
    }

}
