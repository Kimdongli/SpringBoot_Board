package com.example.demo.file;

import com.example.demo.file.BoardFile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Setter
@ToString
@Getter
public class FileDTO {

    // ** 파일 경로
    private String filePath;

    // ** 파일 이름
    private String fileName;

    // ** uuid(랜덤 키)
    private String uuid;

    // ** 파일 포멧
    private String fileType;

    // ** 파일 크기
    private Long fileSize;

    // ** 게시물 id
    private Long boardId;

    public BoardFile toEntity(){
        return BoardFile.builder()
                .filePath(filePath)
                .fileName(fileName)
                .uuid(uuid)
                .fileType(fileType)
                .fileSize(fileSize)
                .build();
    }

/*
    public static FileDTO toFileDTO(File file){
        return new FileDTO(
                file.getBoard().getId(),
                file.getFileName(),
                file.getFileSize(),
                file.getFileType(),
                file.getFilePath());
    }
    */
}
