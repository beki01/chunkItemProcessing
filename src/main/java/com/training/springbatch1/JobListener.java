package com.training.springbatch1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobListener extends JobExecutionListenerSupport {
    private static final Logger log = LoggerFactory.getLogger(JobListener.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobListener(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution){
        if(jobExecution.getStatus() == BatchStatus.COMPLETED){
            log.info("Your coffees have been processed. Results....");

            jdbcTemplate.query("SELECT blend, strength, origin FROM coffee",
                    (rs, row) -> new Coffee(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3)
                    )
            ).forEach(cup -> log.info("This cup is "+cup));
        }
    }
}
