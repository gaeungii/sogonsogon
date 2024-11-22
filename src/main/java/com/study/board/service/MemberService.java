package com.study.board.service;

import com.study.board.dto.MemberDto;
import com.study.board.entity.Member;
import com.study.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service

public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(MemberDto memberDto) {
        return memberRepository.save(Member.builder()
                .email(memberDto.getEmail())
                .password(bCryptPasswordEncoder.encode(memberDto.getPassword()))
                .build()).getId();
    }

}

