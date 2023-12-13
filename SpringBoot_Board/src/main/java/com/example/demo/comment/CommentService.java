package com.example.demo.comment;

import com.example.demo.comment.CommentDTO;
import com.example.demo.board.Board;
import com.example.demo.comment.Comment;
import com.example.demo.board.BoardRepository;
import com.example.demo.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    // DB 트랜잭션을 필요로 하는 메소드임을 나타내는 어노테이션입니다.
    // 이 어노테이션을 붙임으로써, 메소드 내에서 실행되는 DB 작업들이 하나의 트랜잭션으로 묶이게 됩니다.
    @Transactional
    public Comment save(CommentDTO commentDTO) {
        // 댓글이 달릴 게시판을 찾기 위해 게시판의 ID를 사용하여 DB에서 게시판 정보를 가져옵니다.
        Optional<Board> optionalBoard = boardRepository.findById(commentDTO.getBoardId());

        // 게시판이 존재하는지 확인합니다. 존재하면 아래의 코드를 실행합니다.
        if(optionalBoard.isPresent()){
            // Optional에서 Board 객체를 가져옵니다.
            Board board = optionalBoard.get();

            // CommentDTO 객체를 Comment 엔티티로 변환합니다.
            Comment comment = commentDTO.toEntity();

            // 댓글이 어떤 게시판에 속하는지 설정합니다.
            comment.setBoard(board);

            // 댓글을 DB에 저장하고, 저장된 댓글을 반환합니다.
            Comment savedComment = commentRepository.save(comment);

            return savedComment;
        } else {
            // 게시판이 존재하지 않는 경우, null을 반환합니다.
            return null;
        }
    }

    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    public List<Comment> getCommentsByBoardId(Long boardId) {
        return commentRepository.findByBoardId(boardId);
    }

    @Transactional
    public void delete(Long id){
        commentRepository.deleteById(id);
    }

    @Transactional
    public void update(CommentDTO commentDTO){
        Optional<Comment> commentOptional = commentRepository.findById(commentDTO.getId());
        if (commentOptional.isPresent()){
            Comment comment = commentOptional.get();
            comment.updateFromDTO(commentDTO);
        }
    }

}
