package com.example.demo.controller;

import com.example.demo.DTO.CommentDTO;
import com.example.demo.entity.Comment;
import com.example.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity save(@ModelAttribute CommentDTO commentDTO) {

        System.out.println(commentDTO);

        Comment savedComment = commentService.save(commentDTO);
        CommentDTO savedCommentDTO = commentDTO.toCommentDTO(savedComment);

        if (savedCommentDTO != null) {
            return new ResponseEntity<>(savedCommentDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("게시글이 없음.", HttpStatus.NOT_FOUND);
        }
    }

    // HTTP GET 요청을 처리하는 메소드임을 나타내는 어노테이션입니다.
    // "/getComments/{boardId}"는 클라이언트가 요청하는 URL 경로입니다.
    // "{boardId}"는 URL 경로의 일부로서, 클라이언트로부터 받은 게시판 ID를 나타냅니다.
    @GetMapping("/getComments/{boardId}")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long boardId) {
        // 서비스 레이어의 getCommentsByBoardId 메소드를 호출하여, 해당 게시판에 달린 댓글을 가져옵니다.
        List<Comment> comments = commentService.getCommentsByBoardId(boardId);

        // 가져온 댓글들(Comment 객체)을 CommentDTO 객체로 변환합니다.
        // 이때 Java 8의 Stream API와 람다 표현식을 사용합니다.
        List<CommentDTO> commentDTOS = comments.stream().map(CommentDTO::toCommentDTO).collect(Collectors.toList());

        // CommentDTO 리스트를 HTTP 상태 코드 200(OK)와 함께 ResponseEntity에 담아 반환합니다.
        return new ResponseEntity<>(commentDTOS, HttpStatus.OK);
    }


    @PutMapping("/edit/{id}")
    public ResponseEntity edit(@PathVariable Long id, @RequestBody CommentDTO commentDTO){
        Comment editedcomment =commentService.edit(commentDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity<>("댓글이 수정 되었습니다.",headers,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        commentService.delete(id);
        return new ResponseEntity<>("댓글이 삭제 되었습니다.",HttpStatus.OK);
    }
}