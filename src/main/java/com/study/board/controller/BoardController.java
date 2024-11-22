package com.study.board.controller;

import com.study.board.dto.BoardDto;
import com.study.board.entity.Member;
import com.study.board.repository.MemberRepository;
import com.study.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private  final BoardService boardService;
    private  final MemberRepository memberRepository;

    @GetMapping("/post")
    public String post() {
        return "/board/post";
    }

    @PostMapping("/post")
    public String save(BoardDto boardDto) {
        boardService.save(boardDto);
        return "redirect:/";
    }

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = boardService.getPost(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAuthor = false;
        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserEmail = authentication.getName();
            Member currentUser = memberRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            isAuthor = boardDto.getMember().getId().equals(currentUser.getId());
        }

        boardService.updateView(id);
        model.addAttribute("isAuthor", isAuthor);
        model.addAttribute("post", boardDto);
        return "/board/detail";
    }

    @GetMapping("/post/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = boardService.getPost(id);
        model.addAttribute("post", boardDto);
        return "/board/edit";
    }

    @PutMapping("/post/edit/{id}")
    public String update(@PathVariable("id") Long id, BoardDto boardDto){
        boardService.update(id, boardDto);
        return "redirect:/";
    }

    @DeleteMapping("/post/{id}")
    public String delete(@PathVariable("id") Long id){
        boardService.delete(id);
        return "redirect:/";
    }

    @GetMapping("post/my-post")
    public String getMyPost(Model model, @RequestParam(value="page", defaultValue="0")int page){
        Page<BoardDto> paging = boardService.getMyPost(page);
        model.addAttribute("paging",paging);
        return "/board/my-post";
    }

}
