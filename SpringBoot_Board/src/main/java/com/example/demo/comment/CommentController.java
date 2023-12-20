package com.example.demo.comment;

import com.example.demo.board.BoardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity<CommentDTO> save(@ModelAttribute CommentDTO commentDto){
        Comment comment = commentService.save(commentDto);

        if (comment != null){
            return ResponseEntity.ok().body(commentDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // HTTP GET 요청을 처리하는 메소드임을 나타내는 어노테이션입니다.
    // "/getComments/{boardId}"는 클라이언트가 요청하는 URL 경로입니다.
    // "{boardId}"는 URL 경로의 일부로서, 클라이언트로부터 받은 게시판 ID를 나타냅니다.
    @GetMapping("/getComments/{boardId}")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long boardId) {
        // 서비스 레이어의 getCommentsByBoardId 메소드를 호출하여, 해당 게시판에 달린 댓글을 가져옵니다.
        List<CommentDTO> commentDTOS = commentService.getCommentsByBoardId(boardId);

        // CommentDTO 리스트를 HTTP 상태 코드 200(OK)와 함께 ResponseEntity에 담아 반환합니다.
        return new ResponseEntity<>(commentDTOS, HttpStatus.OK);
    }

    /*
    @GetMapping("/getComments")
    public ResponseEntity<List<CommentDTO>>CList(@ModelAttribute BoardDTO dto){
        Long boardId= dto.getId();
        List<CommentDTO> commentDTOS = commentService.CList(boardId);

        return ResponseEntity.ok().body(commentDTOS);
    }

     */
    @PutMapping("/update/{id}")
    public ResponseEntity update(@ModelAttribute CommentDTO commentDTO){
        commentService.update(commentDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        commentService.delete(id);
        return new ResponseEntity<>("댓글이 삭제 되었습니다.",HttpStatus.OK);
    }
}