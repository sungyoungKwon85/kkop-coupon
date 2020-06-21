package com.kkwonsy.kkopservice.controller.v1;

import java.util.Collections;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kkwonsy.kkopservice.advice.exception.CEmailSigninFailedException;
import com.kkwonsy.kkopservice.config.security.JwtTokenProvider;
import com.kkwonsy.kkopservice.domain.User;
import com.kkwonsy.kkopservice.model.response.CommonResult;
import com.kkwonsy.kkopservice.model.response.SingleResult;
import com.kkwonsy.kkopservice.repository.UserRepository;
import com.kkwonsy.kkopservice.service.ResponseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = {"1. Sign"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class SignController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "로그인", notes = "이메일 회원 로그인")
    @PostMapping(value = "/signin")
    public SingleResult<String> signin(
        @ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String email,
        @ApiParam(value = "비밀번호", required = true) @RequestParam String password) {
        User user = userRepository.findByEmail(email).orElseThrow(CEmailSigninFailedException::new);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CEmailSigninFailedException();
        }
        return responseService
            .getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getRoles()));
    }

    @ApiOperation(value = "가입", notes = "회원가입")
    @PostMapping(value = "/signup")
    public CommonResult signup(
        @ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String email,
        @ApiParam(value = "비밀번호", required = true) @RequestParam String password,
        @ApiParam(value = "이름", required = true) @RequestParam String name) {
        userRepository.save(User.builder()
            .email(email)
            .password(passwordEncoder.encode(password))
            .name(name)
            .roles(Collections.singletonList("ROLE_USER"))
            .build());
        return responseService.getSuccessResult();
    }
}