package org.echo.shiro.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.apache.shiro.session.mgt.SimpleSessionFactory;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.session.mgt.quartz.QuartzSessionValidationScheduler;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.echo.shiro.cache.SpringCacheManager;
import org.echo.shiro.session.mgt.eis.SessionIdGeneratorIterator;
import org.echo.shiro.web.session.mgt.StatelessSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
@Configuration
@PropertySource(value={"classpath:/application-shiro.yml"})
@EnableConfigurationProperties(
        value = {
                ShiroProperties.class
        })
public class ShiroConfiguration {

    @Autowired
    private ShiroProperties shiroProperties;

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
        return filterRegistration;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(@Value("${shiro.filter.success.url:/home}") String successUrl,
                                              @Value("${shiro.filter.login.url:/index}") String loginUrl,
                                              @Value("${shiro.filter.unauthorized.url:/unauthorized}") String unauthorizedUrl,
                                              SecurityManager securityManager,
                                              Map<String, Filter> filters){
        ShiroFilterFactoryBean filterFactory = new ShiroFilterFactoryBean();
        filterFactory.setSecurityManager(securityManager);
        filterFactory.setSuccessUrl(successUrl);
        filterFactory.setLoginUrl(loginUrl);
        filterFactory.setUnauthorizedUrl(unauthorizedUrl);
        filterFactory.setFilters(filters);
        Map<String,String> chains  = new HashMap<>();
        //chains.put("/favicon.ico", "anon");
        chains.put("/static/**", "anon");
        chains.put("/logout", "logout");
        chains.put("/**", "user");
        filterFactory.setFilterChainDefinitionMap(chains);
        //filterFactory.setFilterChainDefinitions(filterChainDefinitions());
        return  filterFactory;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator shiroAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator autoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        autoProxyCreator.setProxyTargetClass(true);
        return autoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor advisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public SessionIdGeneratorIterator sessionIdGeneratorIterator(Optional<List<SessionIdGenerator>> sessionIdGenerators){
        return new SessionIdGeneratorIterator(sessionIdGenerators);
    }

    @Bean
    public Cookie sessionIdCookie(@Value("${shiro.session.id:SHIROSESSION}") String name,
                                  @Value("${shiro.session.id:zhezhu}") String sessionId,
                                  @Value("${shiro.cookie.maxAge:-1}") int maxAge,
                                  @Value("${shiro.cookie.domain:}") String domain,
                                  @Value("${shiro.cookie.path:/}") String path){
        SimpleCookie cookie = new SimpleCookie(sessionId);
        cookie.setHttpOnly(true);
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookie.setName(name);
        cookie.setMaxAge(maxAge);
        return  cookie;
    }

    @Bean
    public SessionDAO sessionDAO(@Value("${shiro.activeSessionCache:shiro-activeSessionCache}") String activeSessionCache,
                                 SessionIdGeneratorIterator sessionIdGenerator,
                                 Optional<CacheManager> cacheManager){
        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        cacheManager.ifPresent(cacheManager1 -> sessionDAO.setCacheManager(cacheManager1));
        sessionDAO.setActiveSessionsCacheName(activeSessionCache);
        sessionDAO.setSessionIdGenerator(sessionIdGenerator);
        return sessionDAO;
    }

    @Bean
    public SessionValidationScheduler sessionValidationScheduler(@Value("${shiro.scheduler.validationInterval:1800000}") long interval){
        QuartzSessionValidationScheduler scheduler = new QuartzSessionValidationScheduler();
        scheduler.setSessionValidationInterval(interval);
        return scheduler;
    }

    @Bean
    public SessionFactory sessionFactory(){
        return new SimpleSessionFactory();
    }

    @Bean
    public SessionManager sessionManager(@Value("${shiro.session.globalSessionTimeout:1800000}") long globalSessionTimeout,
                                         SessionValidationScheduler sessionValidationScheduler,
                                         SessionDAO sessionDAO,
                                         Cookie cookie,
                                         SessionFactory sessionFactory){
        StatelessSessionManager sessionManager = new StatelessSessionManager();
        sessionManager.setGlobalSessionTimeout(globalSessionTimeout);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionValidationScheduler(sessionValidationScheduler);
        sessionManager.setSessionDAO(sessionDAO);
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdCookie(cookie);
        sessionManager.setSessionFactory(sessionFactory);
        return sessionManager;
    }

    @Bean
    public CacheManager shiroCacheManager(Optional<org.springframework.cache.CacheManager> springCacheManager){
        return new SpringCacheManager(springCacheManager.get(),this.shiroProperties);
    }

    @Bean
    public SecurityManager securityManager(SessionManager sessionManager,
                                           CacheManager cacheManager,
                                           Optional<List<Realm>> realms){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setSessionManager(sessionManager);
        realms.ifPresent(realms1 -> securityManager.setRealms(realms1));
        securityManager.setCacheManager(cacheManager);
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }
}