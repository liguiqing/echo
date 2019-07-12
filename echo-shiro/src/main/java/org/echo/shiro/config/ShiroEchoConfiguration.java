package org.echo.shiro.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.session.mgt.quartz.QuartzSessionValidationScheduler;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration;
import org.apache.shiro.spring.web.config.AbstractShiroWebConfiguration;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.echo.shiro.SubjectPicker;
import org.echo.shiro.SubjectsContext;
import org.echo.shiro.authc.credential.*;
import org.echo.shiro.realm.PrimusRealm;
import org.echo.shiro.realm.PrimusSubjectPicker;
import org.echo.shiro.session.mgt.eis.SessionIdGeneratorIterator;
import org.echo.shiro.web.session.mgt.StatelessWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
@Configuration
@AutoConfigureBefore(ShiroWebAutoConfiguration.class)
@EnableConfigurationProperties
public class ShiroEchoConfiguration extends AbstractShiroWebConfiguration {

    @Value("#{ @environment['shiro.cache.activeSessionCache'] ?: T(org.apache.shiro.session.mgt.eis.CachingSessionDAO).ACTIVE_SESSION_CACHE_NAME}")
    private String activeSessionCacheName;

    @Value("#{ @environment['shiro.session.globalSessionTimeout'] ?: T(org.apache.shiro.session.mgt.DefaultSessionManager).DEFAULT_GLOBAL_SESSION_TIMEOUT}")
    private long globalSessionTimeout;

    @Value("#{ @environment['shiro.credentials.salt.reader.field'] ?: T(org.echo.shiro.authc.credential.FieldSaltReader).DEFAULT_FIELD}")
    private String saltReaderFieldName;

    @Value("#{ @environment['shiro.credentials.salt.reader.method'] ?:T(org.echo.shiro.authc.credential.MethodSaltReader).DEFAULT_METHOD }")
    private String saltReaderMethodName;

    @Value("#{ @environment['shiro.scheduler.validationInterval'] ?:T(org.apache.shiro.session.mgt.quartz.QuartzSessionValidationScheduler).DEFAULT_SESSION_VALIDATION_INTERVAL}")
    private long schedulerValidationInterval;

    @Bean
    static PrimusSubjectPicker decepticonsPicker(){
        return new PrimusSubjectPicker();
    }

    @Bean
    static SubjectsContext subjectsContext(Optional<Collection<SubjectPicker>> pickers){
        return new SubjectsContext(pickers);
    }

    @Bean
    static SessionIdGeneratorIterator sessionIdGeneratorIterator(Optional<List<SessionIdGenerator>> sessionIdGenerators){
        return new SessionIdGeneratorIterator(sessionIdGenerators);
    }

    @Bean(name = "sessionCookieTemplate")
    @Override
    public Cookie sessionCookieTemplate(){
        return super.sessionCookieTemplate();
    }

    @Bean
    SessionDAO sessionDAO(SessionIdGeneratorIterator sessionIdGenerator,
                                 Optional<CacheManager> cacheManager){
        var sessionDAO = new EnterpriseCacheSessionDAO();
        cacheManager.ifPresent(sessionDAO::setCacheManager);
        sessionDAO.setActiveSessionsCacheName(activeSessionCacheName);
        sessionDAO.setSessionIdGenerator(sessionIdGenerator);
        return sessionDAO;
    }

    @Bean
    SessionValidationScheduler sessionValidationScheduler(){
        var scheduler = new QuartzSessionValidationScheduler();
        scheduler.setSessionValidationInterval(schedulerValidationInterval);
        return scheduler;
    }

    @Bean
    @Override
    public SessionFactory sessionFactory(){
        return super.sessionFactory();
    }

    @Bean
    SessionManager sessionManager(SessionValidationScheduler sessionValidationScheduler,
                                         SessionDAO sessionDAO,
                                         SessionFactory sessionFactory){
        var sessionManager = new DefaultWebSessionManager();
        sessionManager.validateSessions();
        sessionManager.setGlobalSessionTimeout(globalSessionTimeout);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionValidationScheduler(sessionValidationScheduler);
        sessionManager.setSessionDAO(sessionDAO);
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdCookie(super.sessionCookieTemplate());
        sessionManager.setSessionFactory(sessionFactory);
        return sessionManager;
    }

    @Bean
    MD5PasswordEncoder md5PasswordEncoder(){
        return new MD5PasswordEncoder();
    }

    @Bean
    PasswordCredentialsMatcher passwordCredentialsMatcher(Optional<SaltReader> saltReader,MD5PasswordEncoder encoder){
        var fieldReader = new FieldSaltReader(saltReaderFieldName,Optional.of(new MethodSaltReader(saltReaderMethodName, saltReader)));
        return new PasswordCredentialsMatcher(encoder,fieldReader);
    }

    @Bean
    @Order(100)
    public Realm primusRealm(MD5PasswordEncoder encoder,PasswordCredentialsMatcher passwordCredentialsMatcher){
        var realm =  new PrimusRealm(encoder);
        realm.setCredentialsMatcher(passwordCredentialsMatcher);
        return realm;
    }


    @Bean
    @ConfigurationProperties(prefix = "shiro.filter-chain")
    List<FilterChainDefinition> filterChainDefinitions(){
        return new LinkedList<>();
    }


    @Bean
    ShiroFilterChainDefinition shiroFilterChain(@Qualifier("filterChainDefinitions") List<FilterChainDefinition> filterChainDefinitions){
        if(!filterChainDefinitions.contains(FilterChainDefinition.allAuthc()))
            filterChainDefinitions.add(FilterChainDefinition.allAuthc());
        Map<String,String> maps = filterChainDefinitions.stream().collect(Collectors.toMap(FilterChainDefinition::getPattern,FilterChainDefinition::getFilter));
        var chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinitions(maps);
        return chainDefinition;
    }
}