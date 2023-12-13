package com.example.demo.board;

import lombok.*;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    
    private Long id;

    // ** 제목
    @Column(length = 50)
    private String title;

    // ** 내용
    @Column(length = 50)
    private String contents;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
    public Board toEntity(){
        return  Board.builder()
                .title(title)
                .contents(contents)
                .createTime(createTime)
                .updateTime(LocalDateTime.now())
                .build();
    }

    public static BoardDTO toBardDTO(Board board){
        return new BoardDTO(
                board.getId(),
                board.getTitle(),
                board.getContents(),
                board.getCreateTime(),
                board.getUpdateTime() );
    }
}
