package org.echo.ddd.support.domain.model.vo;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/

class LabelDetailServiceTest {

    @Test
    void toLabelDetail() {
        assertTrue(true);
        LabelValueObjectRepository repository = mock(LabelValueObjectRepository.class);
        ArrayList<LabelValueObject> arrayList = Lists.newArrayList();
        arrayList.add(new LabelValueObject(1,"a","Ab","A-A"));
        arrayList.add(new LabelValueObject(1,"a","Aa","A-A"));

        ArrayList<LabelValueObject> arrayList2 = Lists.newArrayList();
        arrayList2.add(new LabelValueObject(1,"a","Ab","A-A"));
        arrayList2.add(new LabelValueObject(1,"a","Ac","A-A"));

        when(repository.findAllByCategoryOrderBySeq(anyString())).thenReturn(arrayList).thenReturn(arrayList2).thenReturn(Lists.newArrayList());
        LabelDetailService service = new LabelDetailService(repository);
        LabelDetail detail = service.toLabelDetail(String.class,"Aa");
        assertEquals("Aa",detail.getValueText());
        assertEquals("a",detail.getLabel());
        assertNull(service.toLabelDetail(String.class, "A"));
        assertNull(service.toLabelDetail(String.class, "A"));
    }
}