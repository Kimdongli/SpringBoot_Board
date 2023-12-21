package com.example.demo.comment;

import com.example.demo.comment.CommentDTO;
import com.example.demo.board.Board;
import com.example.demo.comment.Comment;
import com.example.demo.board.BoardRepository;
import com.example.demo.comment.CommentRepository;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;


    // DB 트랜잭션을 필요로 하는 메소드임을 나타내는 어노테이션입니다.
    // 이 어노테이션을 붙임으로써, 메소드 내에서 실행되는 DB 작업들이 하나의 트랜잭션으로 묶이게 됩니다.
    @Transactional
    public Comment save(CommentDTO commentDTO) {
        // 댓글이 달릴 게시판을 찾기 위해 게시판의 ID를 사용하여 DB에서 게시판 정보를 가져옵니다.
        Optional<Board> optionalBoard = boardRepository.findById(commentDTO.getBoardId());
        // 댓글이 달 유저 찾기 위해 게시판의 ID를 사용하여 DB에서 게시판 정보를 가져옵니다.
        Optional<User> optionalUser = userRepository.findById(commentDTO.getBoardId());
        // 게시판이 존재하는지 확인합니다. 존재하면 아래의 코드를 실행합니다.
        if (optionalUser.isPresent() && optionalBoard.isPresent()){
            User user = optionalUser.get();
            Board board = optionalBoard.get();
            Hibernate.initialize(user.getComments());
            Hibernate.initialize(user.getBoards());
            Hibernate.initialize(board.getComment());
            Hibernate.initialize(board.getFiles());

            Comment comment = Comment.builder()
                    .contents(commentDTO.getContents())
                    .board(board)
                    .user(user)
                    .build();
            commentRepository.save(comment);
            return comment;
        } else {
            return null;
        }
    }

    public List<CommentDTO> CList(Long id){
        List<Comment>comments = commentRepository.findByBoardId(id);
        List<CommentDTO>commentDTOS =new LinkedList<>();
        for (Comment comment : comments){
            commentDTOS.add(CommentDTO.toCommentDTO(comment));
        }
        return commentDTOS;
    }

    public List<CommentDTO> getCommentsByBoardId(Long boardId) {
        List<Comment> comments = commentRepository.findByBoardId(boardId);

        // 가져온 댓글들(Comment 객체)을 CommentDTO 객체로 변환합니다.
        // 이때 Java 8의 Stream API와 람다 표현식을 사용합니다.
        return comments.stream().map(CommentDTO::toCommentDTO).collect(Collectors.toList());
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
