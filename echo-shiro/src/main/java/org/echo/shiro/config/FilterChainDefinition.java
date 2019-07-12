package org.echo.shiro.config;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <pre>
 *  TODO
 * </pre>
 *
 * @author liguiqing
 * @since V1.0.0 2019-07-12 16:26
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FilterChainDefinition {
    private String pattern;

    private String filter;

    public static FilterChainDefinition allAuthc(){
       return new FilterChainDefinition("/**","authc");
    }
}
