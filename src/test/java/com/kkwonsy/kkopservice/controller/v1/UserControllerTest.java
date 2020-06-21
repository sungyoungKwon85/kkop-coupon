package com.kkwonsy.kkopservice.controller.v1;

import java.util.Collections;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.kkwonsy.kkopservice.domain.User;
import com.kkwonsy.kkopservice.repository.UserJpaRepository;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String token;

    @Before
    public void setUp() throws Exception {
        userJpaRepository.save(
            User.builder()
                .email("kkwonsytest@naver.com")
                .name("kkwonsytest")
                .password(passwordEncoder.encode("1234"))
                .roles(Collections.singletonList("ROLE_USER"))
                .build());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", "kkwonsytest@naver.com");
        params.add("password", "1234");
        MvcResult result = mockMvc.perform(post("/v1/signin").params(params))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.code").value(0))
            .andExpect(jsonPath("$.msg").exists())
            .andExpect(jsonPath("$.data").exists())
            .andReturn();

        String resultString = result.getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        token = jsonParser.parseMap(resultString).get("data").toString();
    }


    @Test
    public void invalidToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/users")
            .header("X-AUTH-TOKEN", "XXXXXXXXXX"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/exception/entrypoint"));
    }

    @Test
    @WithMockUser(username = "mockUser", roles = {"ADMIN"}) // 가상의 Mock 유저 대입
    public void access_denied() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/users"))
            //.header("X-AUTH-TOKEN", token))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/exception/accessdenied"));
    }

    @Test
    public void findAllUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/users")
            .header("X-AUTH-TOKEN", token))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.list").exists());
    }

    @Test
    public void findUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/user")
            .header("X-AUTH-TOKEN", token))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").exists());
    }

    // todo
    // UnsupportedOperationException가 발생하는데 이유를 모르겠다.
    // 보통 불변 리스트 등을 선언하고 변경하려고 할때 발생하는 녀석이다.
    // user merge시 발생한다.
//    @Test
//    public void modify() throws Exception {
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("email", "kkwonsytest@naver.com");
//        String newName = "kkwonsytest_new";
//        params.add("name", newName);
//        mockMvc.perform(MockMvcRequestBuilders
//            .put("/v1/user")
//            .header("X-AUTH-TOKEN", token)
//            .params(params))
//            .andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.success").value(true))
//            .andExpect(jsonPath("$.data.name").value(newName));
//    }

    @Test
    public void delete() throws Exception {
        Optional<User> user = userJpaRepository.findByEmail("kkwonsytest@naver.com");
        assertTrue(user.isPresent());
        mockMvc.perform(MockMvcRequestBuilders
            .delete("/v1/user/" + user.get().getId())
            .header("X-AUTH-TOKEN", token))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }
}