package com.kkwonsy.kkopservice.domain.user;

import java.util.Collections;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void save_user_then_findByUid() {
        String uid = "kkwonsy@gmail.com";
        String name = "kkwonsy";
        // given
        userRepository.save(User.builder()
            .uid(uid)
            .password(passwordEncoder.encode("1234"))
            .name(name)
            .roles(Collections.singletonList("ROLE_USER"))
            .build());
        // when
        Optional<User> user = userRepository.findByUid(uid);
        // then
        assertNotNull(user);
        assertTrue(user.isPresent());
        assertEquals(user.get().getName(), name);
        assertThat(user.get().getName(), is(name));
    }
}