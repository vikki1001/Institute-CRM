//package com.ksv.ktrccrm.config;
//
//
//import javax.sql.DataSource;
//
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//@Configuration
//public class DataSourceConfig {
//
//    @Primary
//    @Bean(name = "dataSource1")
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource dataSource1() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "dataSource2")
//    @ConfigurationProperties(prefix = "spring.datasource2")
//    public DataSource dataSource2() {
//        return DataSourceBuilder.create().build();
//    }
//
//}
