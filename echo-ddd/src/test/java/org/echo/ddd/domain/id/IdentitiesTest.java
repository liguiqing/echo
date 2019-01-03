package org.echo.ddd.domain.id;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : Identities Test")
class IdentitiesTest {

    @Test
    protected void test(){
        String id1 = Identities.genId("i1");
        assertTrue(id1.startsWith("i1"));
        String id2 = Identities.genId();
        assertTrue(id2.length()==32);

        IdentityGenerator ig = new IdentityGenerator<Long,Long>(){
            @Override
            public Long genId() {
                return 10000L;
            }

            @Override
            public Long genId(Long prefix) {
                return 10000L + prefix;
            }
        };
        Identities.setGenerator(ig);
        Long id3 = Identities.genId();
        assertTrue(id3 == 10000L);
        Long id4 = Identities.genId(10L);
        assertTrue(id4.compareTo(10010L) == 0);
    }
}