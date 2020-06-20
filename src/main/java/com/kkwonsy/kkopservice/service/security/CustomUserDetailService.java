package com.kkwonsy.kkopservice.service.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.kkwonsy.kkopservice.advice.exception.CUserNotFoundException;
import com.kkwonsy.kkopservice.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetails loadUserByUsername(String id) {
        return userRepository.findById(Long.valueOf(id)).orElseThrow(CUserNotFoundException::new);
    }
}