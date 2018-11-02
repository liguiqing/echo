import org.echo.test.config.JunitTestConfigurations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Liguiqing
 * @since V1.0
 */

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        JunitTestConfigurations.class})
public class ConfigurationTest {
    @Autowired
    Environment env;
    @Value("${jdb.url}")
    private String jdbcUrl;
    @Test
    public void test(){
        assertNotNull(env);
        assertNotNull(jdbcUrl);
    }

}