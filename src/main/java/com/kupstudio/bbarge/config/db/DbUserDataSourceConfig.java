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
@MapperScan(value = "com.kupstudio.bbarge.dao.dbUser", sqlSessionFactoryRef = "userSqlSessionFactory")
@EnableTransactionManagement
public class DbUserDataSourceConfig {

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;


    @Bean(name="userDataSource")
    @ConfigurationProperties(prefix="spring.datasource.user") //application.properties 참고.
    public DataSource userDataSource() {

        return DataSourceBuilder.create().username(username).password(password).build();

    }

    @Bean(name="userSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("userDataSource") DataSource dataSource, ApplicationContext applicationContext) throws Exception{
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(applicationContext.getResources("classpath:/mapper/dbUser/**/*.xml")); //쿼리작성용 mapper.xml위치 설정.
        return sessionFactory.getObject();
    }

    @Bean(name="userSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("userSqlSessionFactory") SqlSessionFactory userSqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(userSqlSessionFactory);
    }


    @Bean(name = "userTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("userDataSource") DataSource userDataSource) {
        return new DataSourceTransactionManager(userDataSource);
    }

}
