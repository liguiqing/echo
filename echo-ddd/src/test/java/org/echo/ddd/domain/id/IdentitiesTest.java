package org.echo.ddd.domain.id;

import org.echo.test.PrivateConstructors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : Identities Test")
class IdentitiesTest {

    @Test
    protected void test(){
        String id1 = Identities.genId(AssociationId.class);
        assertTrue(id1.startsWith("ASS"));
        String id2 = Identities.genId();
        assertTrue(id2.length()==32);
        assertFalse(Identities.genId(new AssociationId()).getId().startsWith("ABD"));
        assertTrue(Identities.genId(new AssociationId()).getId().startsWith("ASS"));
        Identities.setIdPrefix((c)->c.getSimpleName().substring(0,3).toLowerCase());

        Identities.setIdPrefix((c)->null);
        id1 = Identities.genId(AssociationId.class);
        assertFalse(id1.startsWith("ass"));
        IdentityGenerator ig = new IdentityGenerator<Long,Class<Identity>>(){
            @Override
            public Long genId() {
                return 10000L;
            }

            @Override
            public Long genId(Class<Identity> identityClass) {
                return 10010L;
            }
        };
        Identities.setGenerator(ig);
        Long id3 = Identities.genId();
        assertTrue(id3 == 10000L);
        Long id4 = Identities.genId(AssociationId.class);
        assertTrue(id4.compareTo(10010L) == 0);
        assertThrows(Exception.class,()->new PrivateConstructors().exec(Identities.class));

        assertThrows(IdPrefixGeneratorNotFoundException.class, () -> {throw new IdPrefixGeneratorNotFoundException();});
        assertThrows(IdPrefixGeneratorNotFoundException.class, () -> {throw new IdPrefixGeneratorNotFoundException("");});
    }
}