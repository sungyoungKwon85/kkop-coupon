package com.kkwonsy.kkopservice.controller.v1;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.kkwonsy.kkopservice.domain.Coupon;
import com.kkwonsy.kkopservice.domain.User;
import com.kkwonsy.kkopservice.domain.UserCoupon;
import com.kkwonsy.kkopservice.repository.CouponJpaRepository;
import com.kkwonsy.kkopservice.repository.UserCouponJpaRepository;
import com.kkwonsy.kkopservice.repository.UserJpaRepository;
import com.kkwonsy.kkopservice.service.UserCouponService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserCouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserCouponJpaRepository userCouponJpaRepository;

    private String token;
    private Long userId;
    private Long couponId;

    @Before
    public void setUp() throws Exception {
        User user = saveAndGetUser();
        userId = user.getId();

        String code1 = LocalDateTime.now().toString();
        Coupon coupon1 = couponJpaRepository.save(Coupon.createCoupon(code1));
        String code2 = LocalDateTime.now().plusDays(10).toString();
        Coupon coupon2 = couponJpaRepository.save(Coupon.createCoupon(code2));

        couponId = coupon1.getId();

        userCouponJpaRepository.save(UserCoupon.createUserCoupon(user, coupon1));
        userCouponJpaRepository.save(UserCoupon.createUserCoupon(user, coupon2));

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
    public void testCreateCoupons() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("n", "10");
        mockMvc.perform(MockMvcRequestBuilders
            .post("/api/v1/coupons")
            .header("X-AUTH-TOKEN", token)
            .params(params))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void testIssueCoupon() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .post("/api/v1/users/" + userId + "/coupons/issues")
            .header("X-AUTH-TOKEN", token))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void testGetCoupons() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .get("/api/v1/users/" + userId + "/coupons")
            .header("X-AUTH-TOKEN", token))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void testUseCoupon() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("useYn", "true");
        mockMvc.perform(MockMvcRequestBuilders
            .put("/api/v1/users/" + userId + "/coupons/" + couponId)
            .header("X-AUTH-TOKEN", token)
            .params(params))
            .andDo(print())
            .andExpect(status().isOk());
    }

    private User saveAndGetUser() {
        return userJpaRepository.save(
            User.builder()
                .email("kkwonsytest@naver.com")
                .name("kkwonsytest")
                .password(passwordEncoder.encode("1234"))
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
    }
}