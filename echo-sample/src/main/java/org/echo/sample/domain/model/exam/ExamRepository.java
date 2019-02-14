package org.echo.sample.domain.model.exam;

import org.echo.ddd.domain.PersistenceDomainObjectRepository;
import org.echo.ddd.domain.id.Identities;
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

    @Override
    default  ExamId nextIdentity(){
        return new ExamId(Identities.genId(ExamId.class));
    }

    @CacheEvict(value = "exam#3600#3600",key="#p0.id.id")
    @Modifying
    void save(Exam exam);

    @Cacheable(value = "exam#3600#3600",key = "#p0.id",unless = "#result == null")
    @Query("From Exam where examId=?1")
    Exam loadOf(ExamId examId);
}