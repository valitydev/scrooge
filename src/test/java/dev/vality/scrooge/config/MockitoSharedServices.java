package dev.vality.scrooge.config;

import org.springframework.test.context.bean.override.mockito.MockitoBean;

import javax.sql.DataSource;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@MockitoBean(types = {DataSource.class})
public @interface MockitoSharedServices {
}
