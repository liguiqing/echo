package org.echo.shiro.authc.credential;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

import java.util.Arrays;

/**
 * @author Liguiqing
 * @since V1.0
 */
@AllArgsConstructor
@Slf4j
public class PasswordCredentialsMatcher implements CredentialsMatcher {
    private MD5PasswordEncoder passwordEncoder;

    private SaltReader saltReader;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        var tokenHashedCredentials = tokenHashedCredentials(token, info);
        byte[] tokenBytes = tokenHashedCredentials.getBytes();
        byte[] accountBytes = info.getCredentials().toString().getBytes();
        return Arrays.equals(tokenBytes, accountBytes);
    }

    private String tokenHashedCredentials(AuthenticationToken token, AuthenticationInfo info) {
        var pc = info.getPrincipals();
        var upToken = (UsernamePasswordToken) token;
        var originalPassword = new String(upToken.getPassword());
        var salt = saltReader.getSalt(pc.getPrimaryPrincipal());
        return this.passwordEncoder.encode(salt, originalPassword);
    }
}