package org.echo.ddd.support.infrastructure.id;

import com.google.common.collect.Maps;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.echo.ddd.domain.events.EventHandlers;
import org.echo.ddd.domain.events.GuavaEventHandler;
import org.echo.ddd.domain.id.IdPrefix;
import org.echo.ddd.domain.id.Identity;
import org.echo.ddd.domain.id.IdentityGenerator;
import org.echo.ddd.support.domain.model.id.IdLessThenWarned;
import org.echo.ddd.support.domain.model.id.IdPrefixBean;
import org.echo.ddd.support.domain.model.id.IdPrefixBeanRepository;
import org.echo.lock.DistributedLock;
import org.echo.spring.cache.CacheDequeFactory;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.Map;
import java.util.UUID;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class CachingStringIdentityGenerator implements IdentityGenerator<String, Class<? extends Identity>> {

    private static final Map<String, Deque<Long>> idCaches = Maps.newConcurrentMap();

    private static final Object locker = new Object();


    private static final GuavaEventHandler<IdLessThenWarned> handler = new GuavaEventHandler<IdLessThenWarned>() {
        @Override
        protected void when(IdLessThenWarned event) {
            log.debug("Auto load more id");
            CachingStringIdentityGenerator identityGenerator = event.getIdentityGenerator();
            Deque<Long> idQueue = event.getDeque();
            identityGenerator.loadMore(idQueue, event.getPrefixBean());
        }
    };

    private IdPrefixBeanRepository repository;

    private IdPrefix<Class<? extends Identity>> idPrefix;

    private CacheDequeFactory cacheDequeFactory;

    private DistributedLock<Object, Boolean> lock;

    @Setter
    private boolean withPrefix = true;

    @Setter
    private int step = 100;

    public CachingStringIdentityGenerator(IdPrefixBeanRepository repository, IdPrefix<Class<? extends Identity>> idPrefix,
                                          CacheDequeFactory cacheDequeFactory, DistributedLock lock) {
        this.repository = repository;
        this.idPrefix = idPrefix;
        this.cacheDequeFactory = cacheDequeFactory;
        this.lock = lock;
        EventHandlers.getInstance().register(handler);
    }

    @Override
    public String genId() {
        log.debug("Gen an id");
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    @Override
    public String genId(Class<? extends Identity> aClass) {
        log.debug("Gen Id for {}", aClass);

        IdPrefixBean prefixBean = getPrefixBean(aClass);

        String myPrefix = prefixBean.getIdPrefix();
        Deque<Long> idQueue = getIdQueue(myPrefix);
        Long nextId = idQueue.pop();
        log.debug("Gen Id for {} -> {}", aClass, nextId);
        tryToLoad(prefixBean, idQueue);
        if (withPrefix) {
            return prefixBean.getIdPrefix().concat(nextId + "");
        }
        return String.valueOf(nextId);
    }

    private Deque<Long> getIdQueue(String prefix) {
        idCaches.putIfAbsent(prefix, cacheDequeFactory.getDeque(prefix));
        return idCaches.get(prefix);
    }

    private IdPrefixBean getPrefixBean(Class<? extends Identity> aClass) {
        String className = aClass.getName();
        IdPrefixBean prefixBean = this.repository.loadOf(className.hashCode());
        String prefix = this.idPrefix.of(aClass);
        if (prefixBean == null) {
            prefixBean = this.repository.save(new IdPrefixBean(className, prefix, 1L));
            Deque<Long> idQueue = cacheDequeFactory.getDeque(prefix);
            idCaches.put(prefix, idQueue);
            initDeque(idQueue, prefixBean);
        }
        return prefixBean;
    }

    private void initDeque(Deque<Long> idQueue, IdPrefixBean prefixBean) {
        lock.lock(prefixBean, () -> {
            if (idQueue.isEmpty()) {
                loadMore(idQueue, prefixBean);
            }
            return true;
        });
    }

    private void tryToLoad(IdPrefixBean prefixBean, Deque<Long> idQueue) {
        if (needMore(idQueue)) {
            this.lock.lock(locker, () -> {
                EventHandlers.getInstance().post(new IdLessThenWarned(this, prefixBean, idQueue));
                return true;
            });
        }
    }

    private boolean needMore(Deque<Long> idQueue) {
        return idQueue.size() < this.getThreshold();
    }

    private void loadMore(Deque<Long> idQueue, IdPrefixBean prefixBean) {
        Long nextId = prefixBean.getIdSeq();
        int times = 0;
        while (times < this.step) {
            times++;
            idQueue.addLast(nextId++);
        }
        prefixBean.add(times);
    }

    private int getThreshold() {
        //最优实现函数为：f(x)=k/(x-h)^n
        //f(1) 去尝试ｋ，ｈ，ｎ
        return BigDecimal.valueOf(Math.pow(step, -0.09)).multiply(BigDecimal.valueOf(this.step)).intValue();
    }
}