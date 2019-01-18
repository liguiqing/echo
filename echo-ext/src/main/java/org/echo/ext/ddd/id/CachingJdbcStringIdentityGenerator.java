package org.echo.ext.ddd.id;

import com.google.common.collect.Maps;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.echo.ddd.infrastructure.id.JdbcStringIdentityGenerator;
import org.echo.exception.ThrowableToString;
import org.echo.lock.DistributedLock;
import org.echo.spring.cache.CacheDequeFactory;
import org.echo.util.Threads;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.Map;

/**
 * @author Liguiqing
 * @since V3.0
 */
@Slf4j
public class CachingJdbcStringIdentityGenerator extends JdbcStringIdentityGenerator {

    @Setter
    private boolean withPrefix = true;

    private int step;

    private int threshold;

    private DistributedLock lock;

    private CacheDequeFactory cacheDequeFactory;

    private static Map<String,Deque<Long>> idCaches = Maps.newConcurrentMap();

    public CachingJdbcStringIdentityGenerator(JdbcTemplate jdbc, CacheDequeFactory cacheDequeFactory,DistributedLock lock) {
        this(jdbc, 10000,cacheDequeFactory,lock);
    }

    public CachingJdbcStringIdentityGenerator(JdbcTemplate jdbc, int step, CacheDequeFactory cacheDequeFactory,DistributedLock lock) {
        super(jdbc, step);
        this.cacheDequeFactory = cacheDequeFactory;
        this.lock = lock;
        this.setStep(step);
    }

    @Override
    public void setStep(int step){
        this.step = step;
        super.setStep(step);
        this.threshold = BigDecimal.valueOf(step * 0.1).intValue();
    }

    @Override
    public String genId(String prefix){
        String myPrefix = getPrefix(prefix);
        log.debug("Get prefix {} next id ",prefix);
        idCaches.putIfAbsent(myPrefix,cacheDequeFactory.getDeque(myPrefix));
        Deque<Long> idQueue = idCaches.get(myPrefix);
        initDeque(idQueue,myPrefix);
        Long nextSeq = idQueue.pop();
        callLoadMore(idQueue,prefix);
        if(withPrefix){
            return prefix.concat(nextSeq.toString());
        }
        return nextSeq.toString();
    }

    private void initDeque(Deque<Long> idQueue,String prefix){
        try {
            lock.lock(prefix,()->{
                if(idQueue.isEmpty()){
                    loadMore(idQueue,prefix);
                }
                return 0;
            });
        } catch (Exception e) {
            log.warn(ThrowableToString.toString(e));
        }
    }

    private void callLoadMore(Deque<Long> idQueue,String prefix){
        if(idQueue.size() < this.threshold){
            Threads.getExecutorService().submit(() -> loadMore(idQueue, prefix));
        }
    }

    private void loadMore(Deque<Long> idQueue,String prefix) {
        String next = super.genId(prefix);
        Long nextId = Long.valueOf(next.substring(prefix.length()));
        int times = 0;
        while (times <= step) {
            times++;
            idQueue.addLast(nextId++);
        }
    }

    private String getPrefix(String prefix){
        if(StringUtils.isEmpty(prefix)){
            return "CMMN";
        }
        return prefix;
    }
}