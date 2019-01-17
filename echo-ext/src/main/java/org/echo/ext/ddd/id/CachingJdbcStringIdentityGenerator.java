package org.echo.ext.ddd.id;

import com.google.common.collect.Maps;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.echo.ddd.infrastructure.id.JdbcStringIdentityGenerator;
import org.echo.lock.DistributedLock;
import org.echo.spring.cache.CacheDequeFactory;
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

    @Setter
    private DistributedLock lock = new DistributedLock(){};

    private CacheDequeFactory cacheDequeFactory;

    private static Map<String,Deque<Long>> idCaches = Maps.newConcurrentMap();

    public CachingJdbcStringIdentityGenerator(JdbcTemplate jdbc, CacheDequeFactory cacheDequeFactory) {
        this(jdbc, 10000,cacheDequeFactory);
    }

    public CachingJdbcStringIdentityGenerator(JdbcTemplate jdbc, int step, CacheDequeFactory cacheDequeFactory) {
        super(jdbc, step);
        this.cacheDequeFactory = cacheDequeFactory;
        this.setStep(step);
    }

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
        intDequq(idQueue,myPrefix);
        Long nextSeq = idQueue.pop();
        loadMore(idQueue,prefix);
        if(withPrefix){
            return prefix.concat(nextSeq.toString());
        }
        return nextSeq.toString();
    }

    private void intDequq(Deque<Long> idQueue,String prefix){
        lock.lock(prefix);
        if(idQueue.size() == 0){
            loadMore(idQueue,prefix);
        }
        lock.unlock(prefix);
    }

    private void loadMore(Deque<Long> idQueue,String prefix) {
        if(idQueue.size() < this.threshold){
            String next = super.genId(prefix);
            Long nextId = Long.valueOf(next.substring(prefix.length()));
            int times = 0;
            while(times < step){
                times++;
                idQueue.addLast(nextId++);
            }
        }
    }

    private String getPrefix(String prefix){
        if(StringUtils.isEmpty(prefix)){
            return "common";
        }
        return prefix;
    }
}