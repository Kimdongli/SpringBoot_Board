package com.example.demo.board;

import com.example.demo.core.error.exception.Exception403;
import com.example.demo.core.error.exception.Exception500;
import com.example.demo.file.BoardFile;
import com.example.demo.file.FileRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final FileRepository fileRepository;

    // 뤼튼으로 html로 메뉴를 만들어서 메뉴타이틀하나를 클릭을하면 create로 넘어가게 만들면 된다.

    //** Create
    @GetMapping("/create")
    public String create(){
        return "create";
    }

    //** Read
    @GetMapping(value = {"/paging", "/"})
    public String paging(@PageableDefault(page = 1)Pageable pageable, Model model){
        Page<BoardDTO> boards = boardService.paging(pageable);

        // ** 페이지
        int blockLimit = 3;
        int startPage = (int)(Math.ceil((double)pageable.getPageNumber() / blockLimit)-1) * blockLimit +1;
        int endPage = Math.min((startPage + blockLimit - 1), boards.getTotalPages());

        model.addAttribute("boardList", boards);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "paging";
    }

    @GetMapping("/{id}")
    public  String paging(@PathVariable Long id, Model model, @PageableDefault(page = 1)Pageable pageable){


        BoardDTO dto = boardService.findById(id);

        model.addAttribute("board", dto);
        model.addAttribute("page", pageable.getPageNumber());

        // ** 파일들중에 보드 아이디를 검사해서 들고온다
        List<BoardFile> byBoardFiles = fileRepository.findByBoardId(id);
        model.addAttribute("files",byBoardFiles );


        return "detail";
    }

    //** Update
    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO,
                       @RequestParam MultipartFile[] files, HttpServletRequest request) throws IOException {

        boardDTO.setCreateTime(LocalDateTime.now());
        boardService.save(boardDTO, files,request.getSession());

        return "redirect:/board/paging";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model){
        BoardDTO boardDTO =boardService.findById(id);
        model.addAttribute("board",boardDTO);
        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO){
        boardService.update(boardDTO);
        return "redirect:/board/";
    }

    //목록 읽어오는기능

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        boardService.delete(id);
        return "redirect:/board/paging";
    }
}
