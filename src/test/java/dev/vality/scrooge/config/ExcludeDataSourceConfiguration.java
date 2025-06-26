//package dev.vality.scrooge.config;
//
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
//import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//
//import javax.sql.DataSource;
//
//@TestConfiguration
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class,
//        DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
//public class ExcludeDataSourceConfiguration {
//
//    @MockitoBean
//    private DataSource dataSource;
//}
