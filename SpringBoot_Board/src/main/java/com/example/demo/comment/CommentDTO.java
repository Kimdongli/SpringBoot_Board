package com.example.demo.comment;


import com.example.demo.board.Board;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Long id;

    private String writer;

    private String contents;

    private Long boardId;

    private Long userId;
    public Comment toEntity(){
        Comment comment = Comment.builder()
                .id(id)
                .contents(contents)
                .build();
        return comment;
    }
     public static CommentDTO toCommentDTO(Comment comment){
        return new CommentDTO(
                comment.getId(),
                comment.getUser().getName(),
                comment.getContents(),
                comment.getBoard().getId(),
                comment.getUser().getId());
     }
    
}
