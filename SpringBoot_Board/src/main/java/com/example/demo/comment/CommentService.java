package com.example.demo.comment;

import com.example.demo.comment.CommentDto;
import com.example.demo.board.Board;
import com.example.demo.comment.Comment;
import com.example.demo.user.User;
import com.example.demo.board.BoardRepository;
import com.example.demo.comment.CommentRepository;
import com.example.demo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public Comment save(CommentDto commentDto) {
        // 댓글이 달릴 게시판을 찾기 위해 게시판의 ID를 사용하여 DB에서 게시판 정보를 가져옵니다.
        Optional<Board> optionalBoard = boardRepository.findById(commentDto.getBoardId());
        // 댓글이 달 유저 찾기 위해 게시판의 ID를 사용하여 DB에서 게시판 정보를 가져옵니다.
        Optional<User> optionalUser = userRepository.findById(commentDto.getUserId());
        // 게시판이 존재하는지 확인합니다. 존재하면 아래의 코드를 실행합니다.
        if (optionalUser.isPresent() && optionalBoard.isPresent()){
            User user = optionalUser.get();
            Board board = optionalBoard.get();
            Hibernate.initialize(user.getComments());
            Hibernate.initialize(user.getBoards());
            Hibernate.initialize(board.getComment());
            Hibernate.initialize(board.getFiles());

            Comment comment = Comment.builder()
                    .contents(commentDto.getContents())
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .board(board)
                    .user(user)
                    .build();
            commentRepository.save(comment);
            return comment;
        } else {
            return null;
        }
    }

    public List<CommentDto> CList(Long id) {
        List<Comment> comments = commentRepository.findByBoard_id(id);
        List<CommentDto> commentDtos = new LinkedList<>();
        for (Comment c : comments){
            commentDtos.add(CommentDto.toCommentDto(c));
        }
        return commentDtos;
    }

    @Transactional
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    public void update(CommentDto commentDto) {
        Optional<Comment> commentOptional = commentRepository.findById(commentDto.getId());
        if (commentOptional.isPresent()){
            Comment comment = commentOptional.get();
            comment.updateFromDto(commentDto);
        }
    }
}
