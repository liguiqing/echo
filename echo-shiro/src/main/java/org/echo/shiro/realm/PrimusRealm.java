package org.echo.shiro.realm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.echo.shiro.authc.credential.MD5PasswordEncoder;

/**
 * 测试用户Realm
 *
 * 元始天尊(Primus)代表的是“秩序”、“光”以及“创造”，也是塞伯坦星的创世神
 *
 *
 * @author Liguiqing
 * @since V1.0
 */

@AllArgsConstructor
@Slf4j
public class PrimusRealm extends AuthorizingRealm {

    @Getter
    private final Decepticons megatron = new Decepticons().megatron();

    private MD5PasswordEncoder encoder;

    @Override
    public String getName() {
        return "PrimusRealm";
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return true;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)  {
        var userToken = (UsernamePasswordToken) token;
        String name = userToken.getUsername();
        if(!megatron.getName().equals(name)){
            throw new UnknownAccountException("You are The Fallen");
        }

        var principalCollection = new SimplePrincipalCollection(megatron, getName());
        var simpleAuthenticationInfo = new SimpleAuthenticationInfo();
        simpleAuthenticationInfo.setCredentials(encoder.encode(megatron.getSalt(), megatron.getPassword()));
        simpleAuthenticationInfo.setPrincipals(principalCollection);
        return simpleAuthenticationInfo;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return new SimpleAuthorizationInfo();
    }
}