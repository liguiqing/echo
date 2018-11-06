package org.echo.exam.domain.model.exam;

import org.echo.share.domain.PersistenceDomainObjectRepository;
import org.echo.share.id.IdPrefix;
import org.echo.share.id.Identities;
import org.echo.share.id.commons.ExamId;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Liguiqing
 * @since V1.0
 */
@Repository
public interface ExamRepository extends PersistenceDomainObjectRepository<Exam, ExamId> {

    default  ExamId nextIdentity(){
        return new ExamId(Identities.genId(IdPrefix.ExamId));
    }

    @CacheEvict(key="#p0.id.id")
    @Modifying
    void save(Exam exam);

    @Cacheable(key = "#p0.id",unless = "#result == null")
    @Query("From Exam where examId=?1")
    Exam loadOf(ExamId examId);
}