package org.echo.share.id;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
class IdentitiesTest {

    @Test
    @RepeatedTest(10)
    void genId() {
        String aId = Identities.genId("a");
        assertTrue(aId.startsWith("a"));

        BigInteger bId = Identities.genId(1);
        assertTrue(bId.compareTo(new BigInteger("1"))>0);
    }

    @Test
    @RepeatedTest(10)
    void genId1() {
        assertNotNull(Identities.genId());
    }
}