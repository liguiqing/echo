package org.echo.ddd.support.domain.model.id;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * @author Liguiqing
 * @since V3.0
 */
@Repository
public interface IdPrefixBeanRepository extends CrudRepository<IdPrefixBean, Long> {

    @CacheEvict(value = "IdPrefix#-1#-1",key="#p0.idClassNameHash")
    @Modifying
    IdPrefixBean save(IdPrefixBean idp);

    @Cacheable(value = "IdPrefix#-1#-1",key = "#p0",unless = "#result == null")
    @Query("From IdPrefixBean where idClassNameHash=?1")
    IdPrefixBean loadOf(Integer idClassNameHash);

    IdPrefixBean findByIdPrefix(String idPrefix);
}