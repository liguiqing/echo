package org.echo.util;

import com.google.common.collect.Sets;
import org.assertj.core.util.Lists;
import org.echo.test.PrivateConstructors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : CollectionsUtil Test")
class CollectionsUtilTest {

    @Test
    void isNotNullAndNotEmpty() {
        assertThrows(Exception.class,()->new PrivateConstructors().exec(CollectionsUtil.class));
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

    @Test
    void newSetIfNull(){
        ClassUtilsTestBean tt = new ClassUtilsTestBean();

        CollectionsUtil.newCollectionIfNull(tt,"set1",()-> Lists.newArrayList());

        assertNull(tt.getSet1());
        CollectionsUtil.newCollectionIfNull(tt,"set1",()-> Sets.newHashSet());
        assertNotNull(tt.getSet1());

        assertNull(tt.getSet2());
        CollectionsUtil.newCollectionIfNull(tt,"set2",()-> Sets.newTreeSet());
        assertNotNull(tt.getSet2());

        assertNull(tt.getList1());
        CollectionsUtil.newCollectionIfNull(tt,"list1",()-> Lists.newArrayList());
        assertNotNull(tt.getList1());

        assertNull(tt.getList2());
        CollectionsUtil.newCollectionIfNull(tt,"list2",()-> new LinkedList());
        assertNotNull(tt.getList2());

        CollectionsUtil.newCollectionIfNull(tt,"list3",()-> new LinkedList());
    }
}