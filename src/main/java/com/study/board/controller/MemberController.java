package com.study.board.controller;

import com.study.board.dto.MemberDto;
import com.study.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")

public class MemberController {

    private final MemberService memberService;

   @GetMapping("/login")
    public String login(){
       return "/member/login";
   }

   @GetMapping("/join")
    public String joinForm(){
       return "/member/join";
   }

   @PostMapping("/join")
    public String join(MemberDto memberDto){
       memberService.save(memberDto);
       return "redirect:/member/login";
   }
}

