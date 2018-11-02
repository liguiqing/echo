package org.echo.share.id.commons;

import org.echo.share.id.AbstractId;
import org.echo.share.id.IdPrefix;
import org.echo.share.id.Identities;

import java.io.Serializable;

/**
 * Exam对象唯一标识
 *
 * @author Liguiqing
 * @since V3.0
 */

public class ExamId extends AbstractId {
    public ExamId(Serializable id) {
        super(id);
    }

    public ExamId() {
        super(Identities.genId(IdPrefix.ExamId));
    }
}