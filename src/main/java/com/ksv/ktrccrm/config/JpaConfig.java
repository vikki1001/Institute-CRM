//package com.ksv.ktrccrm.config;
//
//import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//
//@Configuration
//@EnableJpaRepositories(
//        basePackages = {"com.ksv.ktrccrm.db1.repository", "com.ksv.ktrccrm.db2.repository"},
//        entityManagerFactoryRef = "entityManagerFactory",
//        transactionManagerRef = "transactionManager"
//)
//public class JpaConfig {
//
//    @Autowired
//    @Qualifier("dataSource1")
//    private DataSource dataSource1;
//
//    @Autowired
//    @Qualifier("dataSource2")
//    private DataSource dataSource2;
//
//    @Primary
//    @Bean(name = "entityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {
//        return builder
//                .dataSource(dataSource1)
//                .packages("com.ksv.ktrccrm.db1.entities")
//                .persistenceUnit("db1")
//                .build();
//    }
//
//    @Bean(name = "entityManagerFactory2")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory2(EntityManagerFactoryBuilder builder) {
//        return builder
//                .dataSource(dataSource2)
//                .packages("com.ksv.ktrccrm.db2.entities")
//                .persistenceUnit("db2")
//                .build();
//    }
//
//    @Primary
//    @Bean(name = "transactionManager")
//    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
//        return new JpaTransactionManager(entityManagerFactory);
//    }
//
//    @Bean(name = "transactionManager2")
//    public PlatformTransactionManager transactionManager2(@Qualifier("entityManagerFactory2") EntityManagerFactory entityManagerFactory2) {
//        return new JpaTransactionManager(entityManagerFactory2);
//    }
//
//}
