package com.study.board.controller;

import com.study.board.dto.BoardDto;
import com.study.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private  final BoardService boardService;

    @GetMapping("/")
    public String home(Model model, @RequestParam(value="page", defaultValue="0") int page) {
        Page<BoardDto> paging = boardService.getPosts(page);
        model.addAttribute("paging", paging);
        return "home";
    }


}
