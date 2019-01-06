package org.echo.ddd.infrastructure.id;

import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcOperations;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * id数据库适配器.
 * 默认为Mysql实现
 *
 * @author Liguiqing
 * @since V1.0
 */

public interface IdDbVendorAdapter {

    default String tableName(){
        return "t_cm_dddId";
    }

    default String nextId(JdbcOperations jdbc,String prefix){
        String sql =  "select idSeq,id from t_cm_dddId where idPrefix='"+prefix+"' for update ";
        final String prefix_ = prefix == null?"":prefix;
        return jdbc.execute((ConnectionCallback<String>) con -> {
            con.setAutoCommit(false);
            try(Statement ps = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = ps.executeQuery(sql)){
                if(rs.next()){
                    long idSeq = rs.getLong("idSeq");
                    rs.updateLong("idSeq",idSeq + 1);
                    con.commit();
                    return prefix_.concat(""+idSeq);
                }
            }
            return prefix_.concat("0");
        });
    }

    default void createTable(JdbcOperations jdbc){
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
        jdbc.batchUpdate(sqls);
    }
}