package com.example.demo.board;

import com.example.demo.file.BoardFile;
import com.example.demo.file.FileRepository;
import com.example.demo.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private final UserService userService;

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
                board.getUser().getName(),
                board.getTitle(),
                board.getContents(),
                board.getCreateTime(),
                board.getUpdateTime(),
                board.getUser().getId()));
    }

    public BoardDTO findById(Long id) {
        Board board = boardRepository.findById(id).get();
        return BoardDTO.toBardDTO(board);
    }



    // ** 데이터 변경해야 되는곳이면 별도로 함수위에 트렌젝션을 넣는다.
    @Transactional
    public void save(BoardDTO dto, MultipartFile[] files, HttpSession session) throws IOException {

        dto.setCreateTime(LocalDateTime.now());
        // ** 게시글 DB에 저장 후 pk을 받아옴.
        Long id = boardRepository.save(dto.toEntity()).getId();
        Board board = boardRepository.findById(id).get();
        board.updateFromUser(userService.setUserInfoInSession(session));

        // file null이면 들어오면안된다. 예외처리
        // ** 파일 정보 저장.
        if(files !=null && files.length >0) {
            if (!files[0].isEmpty()) {
                Path uploadPath = Paths.get(filePath);
                // ** 만약 경로가 없다면... 경로 생성.
                if(!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                for (MultipartFile file : files) {
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


    @Transactional
    public void update(BoardDTO boardDTO, MultipartFile[] files) throws IOException {
        Optional<Board> boardOptional = boardRepository.findById(boardDTO.getId());
        Board board = boardOptional.get();
        board.updateFromDTO(boardDTO);
        board.clearFile();
        boardRepository.save(board);

        fileRepository.deleteByBoard_id(board.getId());
            if (!files[0].isEmpty()) {
                for (MultipartFile file : files) {
                    Path uploadPath = Paths.get(filePath);

                    // 만약 경로가 없다면... 경로 생성
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }

                    // 파일명 추출
                    String originalFilename = file.getOriginalFilename();

                    // 확장자 추출
                    String formatType = originalFilename.substring(
                            originalFilename.lastIndexOf("."));

                    // UUID 생성
                    String uuid = UUID.randomUUID().toString();

                    // 경로 지정
                    String path = filePath + uuid + originalFilename;

                    // 파일을 물리적으로 저장 (DB에 저장 X)
                    file.transferTo( new File(path) );

                    BoardFile boardFile = BoardFile.builder()
                            .filePath(filePath)
                            .fileName(originalFilename)
                            .uuid(uuid)
                            .fileType(formatType)
                            .fileSize(file.getSize())
                            .board(board)
                            .build();

                    fileRepository.save(boardFile);
                }
            }
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
    public List<BoardFile> byBoardFiles(Long id){
        return fileRepository.findByBoardId(id);
    }

}