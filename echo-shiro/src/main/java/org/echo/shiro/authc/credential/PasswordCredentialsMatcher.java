package org.echo.shiro.authc.credential;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.subject.PrincipalCollection;
import org.echo.exception.ThrowableToString;

import java.util.Arrays;

/**
 * @author Liguiqing
 * @since V1.0
 */
@AllArgsConstructor
@Slf4j
public class PasswordCredentialsMatcher implements CredentialsMatcher {
    private MD5PasswordEncoder passwordEncoder;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String tokenHashedCredentials = tokenHashedCredentials(token, info);
        byte[] tokenBytes = tokenHashedCredentials.getBytes();
        byte[] accountBytes = info.getCredentials().toString().getBytes();
        return Arrays.equals(tokenBytes, accountBytes);
    }

    private String tokenHashedCredentials(AuthenticationToken token, AuthenticationInfo info) {
        PrincipalCollection pc = info.getPrincipals();
        Object user = pc.getPrimaryPrincipal();
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String originalPassword = new String(upToken.getPassword());
        String salt = "";
        try {
            Object o = FieldUtils.readField(user,"salt",true);
            salt = o.toString();
        } catch (Exception e) {
            log.warn(ThrowableToString.toString(e));
        }
        return this.passwordEncoder.encode(salt, originalPassword);
    }
}