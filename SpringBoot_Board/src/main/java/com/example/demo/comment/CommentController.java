package com.example.demo.comment;

import com.example.demo.board.BoardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity<CommentDto> save(@ModelAttribute CommentDto commentDto){
        Comment comment = commentService.save(commentDto);

        if (comment != null){
            return ResponseEntity.ok().body(commentDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/getComments")
    public ResponseEntity<List<CommentDto>> CList(@ModelAttribute BoardDTO boardDto){
        Long boardId = boardDto.getId();
        List<CommentDto> comments = commentService.CList(boardId);

        return ResponseEntity.ok().body(comments);
    }

    @GetMapping("/update")
    public ResponseEntity update(@ModelAttribute CommentDto commentDto){
        commentService.update(commentDto);

        return new ResponseEntity<>("댓글이 업데이트 되었습니다.", HttpStatus.OK);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        commentService.delete(id);

        return new ResponseEntity<>("댓글이 삭제 되었습니다.",HttpStatus.OK);
    }



}
