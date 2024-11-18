package com.kupstudio.bbarge.config.db;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@MapperScan(value = "com.kupstudio.bbarge.dao.dbProduct", sqlSessionFactoryRef = "productSqlSessionFactory")
@EnableTransactionManagement
public class DbProductDataSourceConfig {

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;


    @Bean(name="productDataSource")
    @ConfigurationProperties(prefix="spring.datasource.product") //application.properties 참고.
    public DataSource productDataSource() {

        return DataSourceBuilder.create().username(username).password(password).build();

    }

    @Bean(name="productSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("productDataSource") DataSource dataSource, ApplicationContext applicationContext) throws Exception{
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(applicationContext.getResources("classpath:/mapper/dbProduct/**/*.xml")); //쿼리작성용 mapper.xml위치 설정.
        return sessionFactory.getObject();
    }

    @Bean(name="productSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("productSqlSessionFactory") SqlSessionFactory productSqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(productSqlSessionFactory);
    }


    @Bean(name = "productTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("productDataSource") DataSource productDataSource) {
        return new DataSourceTransactionManager(productDataSource);
    }

}
