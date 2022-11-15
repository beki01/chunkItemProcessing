package com.training.springbatch1;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@EnableBatchProcessing
@Configuration
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;


//
    // We create a reader for our CSV file
    @Bean
    public FlatFileItemReader<Coffee> reader() {
        return new FlatFileItemReaderBuilder<Coffee>()
                .name("CoffeeItemReader")
                .resource(new ClassPathResource("coffee.csv"))
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Coffee.class);
                }})
                .lineTokenizer(new DelimitedLineTokenizer(){
                    {
                        setNames("id","blend", "strength", "origin");
                        setDelimiter(",");
                    }
                })
                .build();
    }
    //Defining the processing of our data
    @Bean
    public CoffeeProcessor processor() {
        return new CoffeeProcessor();
    }

    //Writing using Hibernate
    @Bean
    public JpaItemWriter<Coffee> jpaItemWriter(){
        return new JpaItemWriterBuilder<Coffee>()
                .entityManagerFactory(entityManagerFactory().getObject())
                .build();

    }


    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "create");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");

        return properties;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        System.out.println("ENTER CONFIG");
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(new String[] { "com.training.springbatch1" });

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());

        return em;
    }

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSources= new DriverManagerDataSource();
        dataSources.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSources.setUsername("Somewhere over the rainbow");
        dataSources.setPassword("Way up high, There's a land that I heard of");
        dataSources.setUrl("jdbc:mysql://localhost:3306/hbdemo?createDatabaseIfNotExist=true");

        return dataSources;
    }

    //Step Definition
    public Step step1(){
        return stepBuilderFactory.get("step1")
                .<Coffee, Coffee> chunk(2)
                .reader(reader())
                .processor(processor())
                .writer(jpaItemWriter())
                .build();
    }

    @Bean
    public Job importCoffeeJob(){
        return jobBuilderFactory.get("importCoffeeJob")
                .incrementer(new RunIdIncrementer())
                .start(step1()).build();
    }


}
