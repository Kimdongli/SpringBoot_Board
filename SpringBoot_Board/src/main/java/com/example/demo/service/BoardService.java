package com.example.demo.service;

import com.example.demo.DTO.BoardDTO;
import com.example.demo.DTO.FileDTO;
import com.example.demo.entity.Board;
import com.example.demo.entity.BoardFile;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;


@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;


    // ** 학원에서는 /G/
    // ** 집에서는 본인 PC 이름.
    private final String filePath = "C:/Users/G/Desktop/green/Board Files/";
    // ** paging 을 함수
    public Page<BoardDTO> paging(Pageable pageable){
        // ** 페이지 시작 번호 셋팅
        int page = pageable.getPageNumber() - 1;
        // ** 페이지에 포함될 게시물 개수
        int size= 5;

        // ** 전체 게시물을 불러온다.
        Page<Board> boards = boardRepository.findAll(
                PageRequest.of(page,size));

        // ** 람다식
        // ** 우리 눈에는 안 보이지만 for 문이 들어가있다. DTO형식으로 반환
        return boards.map(board -> new BoardDTO(
                board.getId(),
                board.getTitle(),
                board.getContents(),
                board.getCreateTime(),
                board.getUpdateTime()));
    }

    public BoardDTO findById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 게시글을 찾을 수 없습니다."));
        return BoardDTO.toBardDTO(board);
    }



    // ** 데이터 변경해야 되는곳이면 별도로 함수위에 트렌젝션을 넣는다.
    @Transactional
    public void save(BoardDTO dto, MultipartFile[] files) throws IOException {

        Path uploadPath = Paths.get(filePath);

        // ** 만약 경로가 없다면... 경로 생성.
        if(!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // ** 게시글 DB에 저장 후 pk을 받아옴.
        Long id = boardRepository.save(dto.toEntity()).getId();
        Board board = boardRepository.findById(id).get();

        // file null이면 들어오면안된다. 예외처리
        // ** 파일 정보 저장.
        if(files !=null && files.length >0) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {

                    // ** 파일명 추출
                    String originalFileName = file.getOriginalFilename();

                    // ** 확장자 추출
                    String formatType = originalFileName.substring(
                            originalFileName.lastIndexOf("."));

                    // ** UUID 생성
                    String uuid = UUID.randomUUID().toString();

                    // ** 경로 지정
                    // ** C:/Users/G/Desktop/green/Board Files/{uuid + originalFileName}
                    String path = filePath + uuid + originalFileName;

                    // ** 경로에 파일을 저장.  DB 아님
                    file.transferTo(new File(path));

                    BoardFile boardFile = BoardFile.builder()
                            .filePath(filePath)
                            .fileName(originalFileName)
                            .uuid(uuid)
                            .fileType(formatType)
                            .fileSize(file.getSize())
                            .board(board)
                            .build();

                    fileRepository.save(boardFile);

                }
            }
        }
    }

    private String createFilePath(MultipartFile file) throws IOException {

        return "";
    }

    @Transactional
    public void delete(Long id) {

        // ** 사진받은 경로에 파일까지 삭제하게 만든다
        List<BoardFile> boardFiles = fileRepository.findByBoardId(id);

        for (BoardFile file : boardFiles) {
            File targetFile = new File(file.getFilePath() + file.getUuid() + file.getFileName());
            if (targetFile.exists()) {
                targetFile.delete();
            }
            fileRepository.delete(file);
        }
        boardRepository.deleteById(id);
    }

    @Transactional
    public void update(BoardDTO boardDTO) {
        Optional<Board> boardOptional = boardRepository.findById(boardDTO.getId());

        //if(boardOptional.isPresent()) ...예외처리 생략
        Board board = boardOptional.get();

        board.updateFromDTO(boardDTO);

        boardRepository.save(board);

    }
}