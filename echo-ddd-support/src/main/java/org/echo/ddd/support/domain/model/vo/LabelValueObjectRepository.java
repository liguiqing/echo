package org.echo.ddd.support.domain.model.vo;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Repository
public interface LabelValueObjectRepository extends CrudRepository<LabelValueObject, Long> {

    @CacheEvict( value = "LabelVo#-1#-1",key="#p0.detail.category")
    LabelValueObject save(LabelValueObject lvo);

    @Query(value = "From  LabelValueObject where tid = ?1")
    LabelValueObject get(Long tid);

    @CacheEvict(value = "LabelVo#-1#-1",key="#p1")
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE from t_ddd_label_vo where tid=? ",nativeQuery = true)
    void delete(Long tid,String category);

    @CacheEvict(value = "LabelVo#-1#-1",key="#p0")
    @Modifying(clearAutomatically = true)
    @Query(value = "update t_ddd_label_vo set category=?2 where category=?1 ",nativeQuery = true)
    void updateCategory(String category,String newCategory);

    @Cacheable(value = "LabelVo#-1#-1",key = "#p0",unless = "#result == null")
    @Query(value = "From LabelValueObject where detail.category=?1 order by detail.seq")
    List<LabelValueObject> findAllByCategoryOrderBySeq(String category);

}