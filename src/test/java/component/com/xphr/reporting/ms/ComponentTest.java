package component.com.xphr.reporting.ms;

import com.xphr.reporting.ms.Application;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SpringBootTest(classes = {Application.class}, useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
@ActiveProfiles("test")
@Transactional
public @interface ComponentTest {
}
