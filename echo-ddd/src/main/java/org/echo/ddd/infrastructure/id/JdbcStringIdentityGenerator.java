package org.echo.ddd.infrastructure.id;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.echo.ddd.domain.id.IdentityGenerator;
import org.echo.exception.ThrowableToString;
import org.echo.util.NumbersUtil;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

/**
 * 从数据库生成 String 类型的 {@link org.echo.ddd.domain.id.Identity}
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
@AllArgsConstructor
public class JdbcStringIdentityGenerator implements IdentityGenerator<String,String> {

    private static final String tableName = "t_cm_dddId";

    private JdbcTemplate jdbc;

    public String genId(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    public String genId(String prefix){
        log.debug("Next Id from Db for {} ",prefix);
        String sql = "select * from t_cm_dddId where idPrefix='" + prefix + "' for update ";
        Long nextId = jdbc.execute((ConnectionCallback<Long>) con -> {
                con.setAutoCommit(false);
                try (Statement ps = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                     ResultSet rs = ps.executeQuery(sql)) {
                    if (rs.next()) {
                        long idSeq = rs.getLong("idSeq");
                        rs.absolute(1);
                        rs.updateLong("idSeq", idSeq + 1);
                        rs.updateRow();
                        con.commit();
                        return idSeq;
                    }
                }
                return 1L;
        });
        log.debug("Next id of {} is {}",prefix,nextId);
        return prefix.concat(String.valueOf(nextId));
    }

    public void createIdTable(){
        if(!this.hasTable()){
            this.createTable();
        }
    }

    public void newPrefix(String name,String prefix,String clazzName){
        String id = genId(prefix);
        Long lid =  NumbersUtil.stringToLong(id.substring(prefix.length()));
        if(lid.compareTo(1L)==0){
            jdbc.update("insert into t_cm_dddId(idName,idPrefix,idSeq,idClazzPath) values(?,?,?,?)",new Object[]{name,prefix,2L,clazzName});
        }
    }

    public void clearPrefix(String prefix){
        jdbc.update("DELETE FROM t_cm_dddId where idPrefix = ?",prefix);
    }

    private boolean hasTable(){
        try{
            jdbc.query("select 1 from " + this.tableName(),rm->{});
        }catch (Exception e){
            log.warn(ThrowableToString.toString(e));
            return false;
        }
        return true;
    }

    private String tableName(){
        return tableName;
    }

    private void createTable(){
        String[] sqls = { "DROP TABLE IF EXISTS `t_cm_dddId`;",
                "CREATE TABLE `t_cm_dddId` (  " +
                        "    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,  " +
                        "    `idName` VARCHAR(16) COMMENT 'id名称',  " +
                        "    `idPrefix` VARCHAR(4) UNIQUE COMMENT 'id前缀',  " +
                        "    `idSeq` BIGINT(20) COMMENT 'id流水号',  " +
                        "    `idClazzPath` VARCHAR(256) COMMENT '类路径:foo.bar.Bar',  " +
                        "    PRIMARY KEY (`id`),  " +
                        "    INDEX `x_cm_idPrefix` (`idPrefix`)  " +
                        ")  ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 COMMENT='领域模型id记录表';"};
        this.jdbc.batchUpdate(sqls);
    }

}