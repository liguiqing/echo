package org.echo.shiro.authc.credential;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("MD5PasswordEncoder Test")
class MD5PasswordEncoderTest {

    @Test
    void encode() {
        assertNotNull(new MD5PasswordEncoder().encode("aa", "password"));
        assertNotNull(new MD5PasswordEncoder("MD5",5).encode("aa", "password"));
    }
}