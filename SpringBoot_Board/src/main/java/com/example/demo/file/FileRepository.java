package com.example.demo.file;

import com.example.demo.file.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository <BoardFile, Long> {
    List<BoardFile> findByBoardId(Long boardId);

    void deleteByBoard_id(Long id);
}
