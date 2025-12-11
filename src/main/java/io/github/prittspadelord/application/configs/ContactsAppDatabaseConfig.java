package io.github.prittspadelord.application.configs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@ComponentScan(basePackages = "io.github.prittspadelord.application.data.dao.impl")
@Configuration
public class ContactsAppDatabaseConfig {

    @Bean
    public DataSource dataSource() {
        final String driverClassName = System.getenv("DB_DRIVERCLASSNAME");
        final String dbUrl = System.getenv("DB_URL");
        final String username = System.getenv("DB_USERNAME");
        final String password = System.getenv("DB_PASSWORD");

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setJdbcUrl(dbUrl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);

        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        dataSource.setMinimumIdle(1);
        dataSource.setMaximumPoolSize(5);

        return dataSource;
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
