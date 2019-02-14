package org.echo.ddd.support.domain.model.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : LabelValueObject Test")
class LabelValueObjectTest {

    @Test
    void test() {
        LabelValueObject lvo1 = new LabelValueObject(1, "a-1", "1", "A-A");
        LabelValueObject lvo2 = new LabelValueObject(2, "a-2", "2", "A-A");
        LabelValueObject lvo3 = new LabelValueObject(10, "b-1", "1", "A-B");
        LabelValueObject lvo4 = new LabelValueObject(11, "a-1", "1", "A-A");
        assertEquals(lvo1, lvo4);
        assertEquals("LabelValueObject(detail=LabelDetail(seq=1, label=a-1, valueText=1, category=A-A))",lvo1.toString());
        lvo1.exchangeSeq(lvo3);
        assertEquals(1,lvo1.getDetail().getSeq());
        lvo1.exchangeSeq(lvo2);
        assertEquals(2,lvo1.getDetail().getSeq());
        assertEquals(1,lvo2.getDetail().getSeq());
        lvo1.exchangeSeq(lvo3);
        assertEquals(2,lvo1.getDetail().getSeq());
        assertEquals(lvo1, lvo4);

        lvo1.newDetail(lvo3.getDetail());
        assertEquals(2,lvo1.getDetail().getSeq());
        lvo1.newDetail(lvo4.getDetail());
        assertEquals(11,lvo1.getDetail().getSeq());
    }
}