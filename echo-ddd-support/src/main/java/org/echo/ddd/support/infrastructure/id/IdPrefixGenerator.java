package org.echo.ddd.support.infrastructure.id;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.echo.ddd.domain.id.IdPrefix;
import org.echo.ddd.domain.id.Identity;
import org.echo.ddd.support.domain.model.id.IdPrefixBean;
import org.echo.ddd.support.domain.model.id.IdPrefixBeanRepository;

/**
 * @author Liguiqing
 * @since V3.0
 */
@Slf4j
@AllArgsConstructor
public class IdPrefixGenerator implements IdPrefix<Class<? extends Identity>>{
    private int length = 3;

    private WordsToString wordsToString;

    private IdPrefixBeanRepository repository;

    @Override
    public String of(Class<? extends Identity> aClass) {
        IdPrefixBean prefixBean = get(aClass);
        if(prefixBean != null)
            return prefixBean.getIdPrefix();

        String className = aClass.getSimpleName();
        String[] words = className.split("(?=[A-Z])");
        return this.wordsToString.toString(words, this.length,(s)->true);
    }

    private IdPrefixBean get(Class<? extends Identity> aClass){
        return this.repository.loadOf(aClass.getName().hashCode());
    }
}