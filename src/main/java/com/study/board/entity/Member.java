package com.study.board.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="password")
    private String password;


    @Builder
    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Board> bards = new ArrayList<>();

    @Override //권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    //사용자의 id 반환 (고유값)
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true; // true -> 만료되지 않았음
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; //true -> 잠금되지 않았음
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; //true -> 만료되지 않았음
    }

    @Override
    public boolean isEnabled() {
        return true; //true -> 사용 가능
    }

}