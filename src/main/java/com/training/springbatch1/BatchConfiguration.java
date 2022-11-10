package com.training.springbatch1;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.HibernateCursorItemReader;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.HibernateCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@EnableBatchProcessing
@Configuration
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

//
//    // We create a reader for our CSV file
//    @Bean
//    public FlatFileItemReader<Coffee> reader() {
//        return new FlatFileItemReaderBuilder<Coffee>()
//                .name("CoffeeItemReader")
//                .resource(new ClassPathResource("coffee.csv"))
//                .delimited()
//                .names("blend", "strength", "origin")
//                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
//                    setTargetType(Coffee.class);
//                }})
//                .build();
//    }
//

    @Bean
    public HibernateCursorItemReader<Coffee> reader()  {

       SessionFactory factory = new org.hibernate.cfg.Configuration().configure("hbconfig.xml")
                .addAnnotatedClass(Coffee.class)
                .buildSessionFactory();

            return new HibernateCursorItemReaderBuilder<Coffee>()
                    .queryString("FROM Coffee")
                    .sessionFactory(factory)
                    .saveState(false)
                    .build();

    }

    //Defining the processing of our data
    @Bean
    public CoffeeItemProcessor processor() {
        return new CoffeeItemProcessor();
    }


    // Writing to the JDBC
//    @Bean
//    public JdbcBatchItemWriter<Coffee> writer(DataSource dataSource){
//        return new JdbcBatchItemWriterBuilder<Coffee>()
//                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
//                .sql("INSERT INTO coffee (blend, strength, origin) VALUES (:blend, :strength, :origin)")
//                .dataSource(dataSource)
//                .build();
//    }

    //Step Definition
    @Bean
    public Step step1(){
        return stepBuilderFactory.get("step1")
                .<Coffee, Coffee> chunk(2)
                .reader(reader())
                .writer(new ConsoleItemWriter())
                .build();
    }

    @Bean
    public Job importCoffeeJob(JobListener listener, Step step1){
        return jobBuilderFactory.get("importCoffeeJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end().build();
    }


}
