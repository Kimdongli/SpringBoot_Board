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

    public Comment toEntity(){
        Comment comment = Comment.builder()
                .writer(writer)
                .contents(contents)
                .build();
        if (boardId != null) {
            Board board = new Board();
            board.setId(boardId);
            comment.setBoard(board);
        }
        return comment;
    }
     public static CommentDTO toCommentDTO(Comment comment){
        return new CommentDTO(
                comment.getId(),
                comment.getWriter(),
                comment.getContents(),
                comment.getBoard().getId());
     }
    
}
