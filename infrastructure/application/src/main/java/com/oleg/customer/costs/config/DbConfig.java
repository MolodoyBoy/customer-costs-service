package com.oleg.customer.costs.config;

import org.jooq.DSLContext;
import org.jooq.impl.DataSourceConnectionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

import static org.jooq.impl.DSL.using;
import static org.jooq.SQLDialect.POSTGRES;

@Configuration
public class DbConfig {

    @Bean
    public DataSourceConnectionProvider dataSourceConnectionProvider(DataSource dataSource) {
        DataSource dataSourceProxy = new TransactionAwareDataSourceProxy(dataSource);
        return new DataSourceConnectionProvider(dataSourceProxy);
    }

    @Bean
    public DSLContext benchmarkDslContext(DataSourceConnectionProvider dataSourceConnectionProvider) {
        return using(dataSourceConnectionProvider, POSTGRES);
    }
}