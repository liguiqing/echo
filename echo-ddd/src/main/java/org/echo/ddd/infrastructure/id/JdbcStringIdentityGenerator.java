package org.echo.ddd.infrastructure.id;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.echo.ddd.domain.id.IdPrefix;
import org.echo.ddd.domain.id.Identity;
import org.echo.ddd.domain.id.IdentityGenerator;
import org.echo.exception.ThrowableToString;
import org.echo.util.NumbersUtil;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 从数据库生成 String 类型的 {@link org.echo.ddd.domain.id.Identity}
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
@AllArgsConstructor
public class JdbcStringIdentityGenerator implements IdentityGenerator<String, Class<? extends Identity>> {

    private static final String tableName = "t_cm_dddId";

    private JdbcTemplate jdbc;

    private IdPrefix<Class<? extends Identity>> idPrefix;

    @Setter
    private int step = 1;

    public String genId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public String genId(Class<? extends Identity> idClass) {
        String prefix = idPrefix.of(idClass);
        log.debug("Next Id from Db for {} ", prefix);
        String sql = "select * from t_cm_dddId where idPrefix='" + prefix + "' for update ";
        Long nextId = jdbc.execute((ConnectionCallback<Long>) con -> {
            con.setAutoCommit(false);
            try (Statement ps = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                 ResultSet rs = ps.executeQuery(sql)) {
                if (rs.next()) {
                    long idSeq = rs.getLong("idSeq");
                    rs.absolute(1);
                    rs.updateLong("idSeq", idSeq + step);
                    rs.updateRow();
                    con.commit();
                    return idSeq;
                }
            }
            return 1L;
        });
        log.debug("Next id of {} is {}", prefix, nextId);
        return prefix.concat(String.valueOf(nextId));
    }

    public void createIdTable() {
        if (!this.hasTable()) {
            this.createTable();
        }
    }

    public void newPrefix(Class<? extends Identity> idClass) {
        String id = genId(idClass);
        String prefix = idPrefix.of(idClass);
        Long lid = NumbersUtil.stringToLong(id.substring(prefix.length()));
        if (lid.compareTo(1L) == 0) {
            jdbc.update("insert into t_cm_dddId(idPrefix,idSeq,idClazzName) values(?,?,?)", new Object[]{prefix, 1L, idClass.getName()});
        }
    }

    public void clearPrefix(String prefix) {
        jdbc.update("DELETE FROM t_cm_dddId where idPrefix = ?", prefix);
    }

    public Optional<List<IdPrefixBean>> existsPrefixes() {
        return Optional.ofNullable(jdbc.query("select idClazzName,idPrefix,idSeq from t_cm_dddId",
                (rs, rowNum) -> new IdPrefixBean(rs.getString("idClazzName"), rs.getString("idPrefix"),
                        rs.getLong("idSeq"))));
    }

    private boolean hasTable() {
        try {
            jdbc.query("select 1 from " + this.tableName(), rm -> {
            });
        } catch (Exception e) {
            log.warn(ThrowableToString.toString(e));
            return false;
        }
        return true;
    }

    private String tableName() {
        return tableName;
    }

    private void createTable() {
        String[] sqls = {"DROP TABLE IF EXISTS t_cm_dddId;",
                "CREATE TABLE t_cm_dddId (  " +
                        "    id BIGINT(20) NOT NULL AUTO_INCREMENT,  " +
                        "    idClazzName VARCHAR(256) UNIQUE COMMENT '类名:foo.bar.Bar',  " +
                        "    idPrefix VARCHAR(4) UNIQUE COMMENT 'id前缀',  " +
                        "    idSeq BIGINT(20)  COMMENT 'id流水号',  " +
                        "    PRIMARY KEY (id),  " +
                        "    INDEX x_cm_idPrefix (idPrefix),  " +
                        "    INDEX x_cm_idClazzName (idClazzName)  " +
                        ")  ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8 COMMENT='领域模型id记录表';"};
        this.jdbc.batchUpdate(sqls);
    }

}