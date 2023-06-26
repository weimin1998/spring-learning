package com.example.springheima.beanFactoryPostProcessor;

import com.alibaba.druid.pool.DruidDataSource;
import com.example.springheima.beanFactoryPostProcessor.mapper.Mapper1;
import com.example.springheima.beanFactoryPostProcessor.mapper.Mapper2;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.example.springheima.beanFactoryPostProcessor.component")
public class Config {

    @Bean
    public Bean1 bean1(){
        return new Bean1();
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource){
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }

    @Bean(initMethod = "init")
    public DruidDataSource dataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUsername("root");
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/test");
        druidDataSource.setPassword("root");
        return druidDataSource;
    }

//    @Bean
//    public MapperFactoryBean<Mapper1> mapper1(SqlSessionFactory sqlSessionFactory){
//        MapperFactoryBean<Mapper1> mapperFactoryBean = new MapperFactoryBean<>(Mapper1.class);
//        mapperFactoryBean.setSqlSessionFactory(sqlSessionFactory);
//        return mapperFactoryBean;
//    }
//
//    @Bean
//    public MapperFactoryBean<Mapper2> mapper2(SqlSessionFactory sqlSessionFactory){
//        MapperFactoryBean<Mapper2> mapperFactoryBean = new MapperFactoryBean<>(Mapper2.class);
//        mapperFactoryBean.setSqlSessionFactory(sqlSessionFactory);
//        return mapperFactoryBean;
//    }
}
