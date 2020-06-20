package com.kkwonsy.kkopservice.controller.v1;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kkwonsy.kkopservice.advice.exception.CUserNotFoundException;
import com.kkwonsy.kkopservice.domain.user.User;
import com.kkwonsy.kkopservice.domain.user.UserRepository;
import com.kkwonsy.kkopservice.model.response.CommonResult;
import com.kkwonsy.kkopservice.model.response.ListResult;
import com.kkwonsy.kkopservice.model.response.SingleResult;
import com.kkwonsy.kkopservice.service.ResponseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = {"2. User"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class UserController {

    private final UserRepository userRepository;
    private final ResponseService responseService;

    @ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 리스트 조회", notes = "모든 회원을 조회한다")
    @GetMapping(value = "/users")
    public ListResult<User> findAllUser() {
        return responseService.getListResult(userRepository.findAll());
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 단건 조회", notes = "회원번호(id)로 회원을 조회한다")
    @GetMapping(value = "/user")
    public SingleResult<User> findUserById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        return responseService.getSingleResult(userRepository.findByUid(id).orElseThrow(CUserNotFoundException::new));
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다")
    @PutMapping(value = "/user")
    public SingleResult<User> modify(
        @ApiParam(value = "회원이름", required = true) @RequestParam String name) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        User user = userRepository.findByUid(id).orElseThrow(CUserNotFoundException::new);
        user.setName(name);
        return responseService.getSingleResult(userRepository.save(user));
    }


    @ApiImplicitParams({
        @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 삭제", notes = "userId로 회원정보를 삭제한다")
    @DeleteMapping(value = "/user/{id}")
    public CommonResult delete(
        @ApiParam(value = "회원번호", required = true) @PathVariable Long id) {
        userRepository.deleteById(id);
        return responseService.getSuccessResult();
    }
}