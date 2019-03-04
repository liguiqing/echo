package org.echo.shiro.authc.credential;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @author Liguiqing
 * @since V1.0
 */
@NoArgsConstructor
@AllArgsConstructor
public class MD5PasswordEncoder {
    private String algorithmName = "md5";

    private int hashIterations = 2;

    public  String encode(String salt, String password) {
        return new SimpleHash(this.algorithmName, password, ByteSource.Util.bytes(salt), this.hashIterations).toHex();
    }
}