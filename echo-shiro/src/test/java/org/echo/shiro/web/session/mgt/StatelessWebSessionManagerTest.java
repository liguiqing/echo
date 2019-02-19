package org.echo.shiro.web.session.mgt;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.web.session.mgt.DefaultWebSessionContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("ServletUtilWrapper Test")
class StatelessWebSessionManagerTest {

    @Test
    void getSessionId(){
        StatelessWebSessionManager sessionManager = new StatelessWebSessionManager("x-auth-token","header");
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("User-Agent"))
                .thenReturn("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50 ")
                .thenReturn("(iPhone; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us)  Mobile/8J2 micromessenger")
                .thenReturn("(iPhone; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us)  Mobile/8J2 micromessenger")
                .thenReturn("(iPad; U; CPU OS 4_3_3 like Mac OS X; en-us)  Mobile/8J2 ");
        HttpServletResponse response = mock(HttpServletResponse.class);

        String session = UUID.randomUUID().toString();
        when(request.getHeader(sessionManager.getSessionToken())).thenReturn(null).thenReturn(session);

        TestSessionKey sessionKey = spy(new TestSessionKey());
        when(sessionKey.getServletRequest()).thenReturn(request);
        when(sessionKey.getServletResponse()).thenReturn(response);

        when(sessionKey.getSessionId()).thenReturn(session).thenReturn(null).thenReturn(null).thenReturn(null).thenReturn(null);

        assertEquals(session,sessionManager.getSessionId(sessionKey));
        assertNull(sessionManager.getSessionId(sessionKey));
        assertNull(sessionManager.getSessionId(sessionKey));
        assertEquals(session,sessionManager.getSessionId(sessionKey));
        assertEquals(session,sessionManager.getSessionId(sessionKey));
    }

    @Test
    void onStart(){

        DefaultWebSessionContext sessionContext = spy(new DefaultWebSessionContext());
        Session session = mock(Session.class);
        StatelessWebSessionManager sessionManager = new StatelessWebSessionManager();

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("User-Agent"))
                .thenReturn("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50 ")
                .thenReturn("(iPhone; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us)  Mobile/8J2 micromessenger")
                .thenReturn("(iPhone; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us)  Mobile/8J2 micromessenger")
                .thenReturn("(iPad; U; CPU OS 4_3_3 like Mac OS X; en-us)  Mobile/8J2 ");
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(sessionContext.getServletRequest()).thenReturn(request);
        when(sessionContext.getServletResponse()).thenReturn(response);

        sessionManager.onStart(session,sessionContext);

        String sessionId = UUID.randomUUID().toString();
        when(session.getId()).thenReturn(sessionId);
        sessionManager.onStart(session,sessionContext);
        sessionManager.onStart(session,sessionContext);
    }

    @Test
    void onStop(){

        Session session = mock(ValidatingSession.class);
        StatelessWebSessionManager sessionManager = spy(new StatelessWebSessionManager());
        SessionDAO sessionDao = mock(SessionDAO.class);
        sessionManager.setSessionDAO(sessionDao);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("User-Agent"))
                .thenReturn("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50 ")
                .thenReturn("(iPhone; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us)  Mobile/8J2 micromessenger")
                .thenReturn("(iPhone; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us)  Mobile/8J2 micromessenger")
                .thenReturn("(iPad; U; CPU OS 4_3_3 like Mac OS X; en-us)  Mobile/8J2 ");
        HttpServletResponse response = mock(HttpServletResponse.class);

        TestSessionKey sessionKey = spy(new TestSessionKey());
        when(sessionKey.getServletRequest()).thenReturn(request);
        when(sessionKey.getServletResponse()).thenReturn(response);

        String sessionId = UUID.randomUUID().toString();
        when(session.getId()).thenReturn(sessionId);
        when(sessionKey.getSessionId()).thenReturn(sessionId);
        when(sessionDao.readSession(any(Serializable.class))).thenReturn(session);
        sessionManager.onStop(session,sessionKey);
        sessionManager.onStop(session,sessionKey);

        SessionKey sessionKey1 = mock(SessionKey.class);
        sessionManager.onStop(session,sessionKey1);
    }

    @Test
    void isServletContainerSessions(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("User-Agent"))
                .thenReturn("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50 ")
                .thenReturn("(iPhone; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us)  Mobile/8J2 micromessenger")
                .thenReturn("(iPhone; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us)  Mobile/8J2 micromessenger")
                .thenReturn("(iPad; U; CPU OS 4_3_3 like Mac OS X; en-us)  Mobile/8J2 ");

        ServletRequestAttributes attributes = spy(new ServletRequestAttributes(request));

        RequestContextHolder.setRequestAttributes(attributes);

        StatelessWebSessionManager sessionManager = spy(new StatelessWebSessionManager());
        assertFalse(sessionManager.isServletContainerSessions());
        assertFalse(sessionManager.isServletContainerSessions());
        assertFalse(sessionManager.isServletContainerSessions());
        assertFalse(sessionManager.isServletContainerSessions());
    }
}