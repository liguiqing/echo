import org.springframework.beans.factory.annotation.Value;

/**
 * @author Liguiqing
 * @since V1.0
 */

public class TestBean {

    private String jdbcUrl;

    public TestBean(@Value("jdbc.url") String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }
}