package org.echo.shiro.config;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
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
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.echo.shiro.SubjectPicker;
import org.echo.shiro.SubjectsContext;
import org.echo.shiro.authc.credential.*;
import org.echo.shiro.cache.SpringCacheManager;
import org.echo.shiro.realm.Decepticons;
import org.echo.shiro.realm.PrimusRealm;
import org.echo.shiro.realm.PrimusSubjectPicker;
import org.echo.shiro.session.mgt.eis.SessionIdGeneratorIterator;
import org.echo.shiro.web.session.mgt.StatelessWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.*;

/**
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(
        value = {
                ShiroProperties.class
        })
public class ShiroConfigurations {

    @Bean("shiroPlaceholder")
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public PrimusSubjectPicker decepticonsPicker(){
        return new PrimusSubjectPicker();
    }

    @Bean
    public SubjectsContext subjectsContext(Optional<Collection<SubjectPicker>> pickers){
        return new SubjectsContext(pickers);
    }

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
                                              Optional<Map<String, Filter>> filters){
        ShiroFilterFactoryBean filterFactory = new ShiroFilterFactoryBean();
        filterFactory.setSecurityManager(securityManager);
        filterFactory.setSuccessUrl(successUrl);
        filterFactory.setLoginUrl(loginUrl);
        filterFactory.setUnauthorizedUrl(unauthorizedUrl);
        filters.ifPresent(filterFactory::setFilters);
        Map<String,String> chains  = new HashMap<>();
        chains.put("/favicon.ico", "anon");
        chains.put("/static/**", "anon");
        chains.put(loginUrl, "anon");
        chains.put("/", "anon");
        chains.put("/logout", "logout");
        chains.put("/**", "user");
        filterFactory.setFilterChainDefinitionMap(chains);
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
    public Cookie sessionIdCookie(@Value("${shiro.session.name:SHIROSESSION}") String name,
                                  @Value("${shiro.cookie.maxAge:-1}") int maxAge,
                                  @Value("${shiro.cookie.domain:}") String domain,
                                  @Value("${shiro.cookie.path:/}") String path){
        SimpleCookie cookie = new SimpleCookie(name);
        cookie.setHttpOnly(true);
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        return  cookie;
    }

    @Bean
    public SessionDAO sessionDAO(@Value("${shiro.cache.activeSessionCache:shiro-activeSessionCache}") String activeSessionCache,
                                 SessionIdGeneratorIterator sessionIdGenerator,
                                 Optional<CacheManager> cacheManager){
        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        cacheManager.ifPresent(sessionDAO::setCacheManager);
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
        StatelessWebSessionManager sessionManager = new StatelessWebSessionManager();
        sessionManager.validateSessions();
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
    public CacheManager shiroCacheManager(Optional<org.springframework.cache.CacheManager> springCacheManager,
                                          ShiroProperties shiroProperties){
        String tempKey = "springCacheManager";
        HashMap<String,org.springframework.cache.CacheManager> map = Maps.newHashMap();
        map.put(tempKey, new CaffeineCacheManager());
        springCacheManager.ifPresent(sc->map.put(tempKey,sc));
        return new SpringCacheManager(map.get(tempKey),shiroProperties);
    }

    @Bean
    public AuthenticationStrategy authenticationStrategy(){
        return new FirstSuccessfulStrategy();
    }

    @Bean
    public Authenticator authenticator(AuthenticationStrategy authenticationStrategy,
                                       Optional<List<Realm>> realms){
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(authenticationStrategy);
        realms.ifPresent(authenticator::setRealms);
        return authenticator;
    }

    @Bean
    public SecurityManager securityManager(SessionManager sessionManager,
                                           CacheManager cacheManager,
                                           Authenticator authenticator,
                                           Optional<List<Realm>> realms){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setSessionManager(sessionManager);
        realms.ifPresent(securityManager::setRealms);
        securityManager.setCacheManager(cacheManager);
        securityManager.setAuthenticator(authenticator);
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public MD5PasswordEncoder md5PasswordEncoder(){
        return new MD5PasswordEncoder();
    }

    @Bean
    public PasswordCredentialsMatcher passwordCredentialsMatcher(
            @Value("${shiro.credentials.salt.reader.field:salt}") String salt,
            @Value("${shiro.credentials.salt.reader.method:getSalt}") String method,
            Optional<SaltReader> saltReader,
            MD5PasswordEncoder encoder){
        FieldSaltReader fieldReader = new FieldSaltReader(salt,Optional.of(new MethodSaltReader(method, saltReader)));
        return new PasswordCredentialsMatcher(encoder,fieldReader);
    }

    @Bean
    @Order(100)
    public Realm primusRealm(MD5PasswordEncoder encoder,PasswordCredentialsMatcher passwordCredentialsMatcher){
        PrimusRealm realm =  new PrimusRealm(encoder);
        realm.setCredentialsMatcher(passwordCredentialsMatcher);
        return realm;
    }

    @Bean
    public  Filter formAuthc(@Value("${shiro.authc.form.password.param:password}") String passwordParam,
                             @Value("${shiro.authc.form.username.param:username}") String userNameParam){
        FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
        formAuthenticationFilter.setPasswordParam(passwordParam);
        formAuthenticationFilter.setUsernameParam(userNameParam);
        return formAuthenticationFilter;
    }

}