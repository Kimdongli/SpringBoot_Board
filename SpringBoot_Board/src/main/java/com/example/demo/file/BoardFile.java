package com.example.demo.file;

import com.example.demo.board.Board;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class BoardFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ** 파일 경로
    @NotNull
    private String filePath;
    // ** 파일 이름
    @Column(length = 150,nullable = false)
    private String fileName;
    // ** uuid(랜덤 키)
    private String uuid;
    // ** 파일 포멧
    @NotNull
    private String fileType;
    // ** 파일 크기
    private Long fileSize;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;


    @Builder
    public BoardFile(Long id, String filePath, String fileName, String uuid, String fileType, Long fileSize, Board board) {
        this.id = id;
        this.filePath = filePath;
        this.fileName = fileName;
        this.uuid = uuid;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.board = board;
    }
}