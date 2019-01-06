package org.echo.ddd.infrastructure.id;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.echo.ddd.domain.id.IdentityGenerator;
import org.echo.exception.ThrowableToString;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 从数据库生成 String 类型的 {@link org.echo.ddd.domain.id.Identity}
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
@AllArgsConstructor
public class JdbcStringIdentityGenerator implements IdentityGenerator<String,String> {

    private JdbcTemplate jdbc;

    private IdDbVendorAdapter idDbVendorAdapter;

    public String genId(String prefix){
        log.debug("Next Id from Db for {} ",prefix);
        return idDbVendorAdapter.nextId(jdbc, prefix);
    }

    public void createIdTable(){
        if(!hasTable()){
            this.idDbVendorAdapter.createTable(jdbc);
        }
    }

    private boolean hasTable(){
        try{
            jdbc.query("select 1 from " + this.idDbVendorAdapter.tableName(),rm->{});
        }catch (Exception e){
            log.warn(ThrowableToString.toString(e));
            return false;
        }
        return true;
    }

}