package org.echo.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : CollectionsUtil test")
class CollectionsUtilTest {

    @Test
    void isNotNullAndNotEmpty() {
        assertAll(()->assertFalse(CollectionsUtil.isNotNullAndNotEmpty(null)),
                ()->assertFalse(CollectionsUtil.isNotNullAndNotEmpty(new ArrayList())),
                ()->assertTrue(CollectionsUtil.isNotNullAndNotEmpty(Arrays.asList("a"))));
    }

    @Test
    void isNullOrEmpty() {
        assertAll(()->assertTrue(CollectionsUtil.isNullOrEmpty(null)),
                ()->assertTrue(CollectionsUtil.isNullOrEmpty(new ArrayList())),
                ()->assertFalse(CollectionsUtil.isNullOrEmpty(Arrays.asList("a"))));
    }

    @Test
    void hasElements() {
        assertAll(()->assertFalse(CollectionsUtil.hasElements(null)),
                ()->assertFalse(CollectionsUtil.hasElements(new ArrayList())),
                ()->assertTrue(CollectionsUtil.hasElements(Arrays.asList("a"))));
    }
}