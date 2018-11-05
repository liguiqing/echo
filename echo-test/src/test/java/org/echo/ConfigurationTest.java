import lombok.extern.slf4j.Slf4j;
import org.echo.test.config.JunitTestConfigurations;
import org.junit.jupiter.api.DisplayName;
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
@Slf4j
@DisplayName("Echo Test : Configuration")
public class ConfigurationTest {
    @Autowired
    Environment env;
    @Value("${jdb.url}")
    private String jdbcUrl;
    @Test
    public void test(){
        log.debug("I am testing ...");
        assertNotNull(env);
        assertNotNull(jdbcUrl);

    }

}