package com.training.springbatch1;

import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

public class CustomSkipPolicy implements SkipPolicy {
    public static final Integer MAX_SKIP_COUNT = 2;

    @Override
    public boolean shouldSkip(Throwable throwable, int i) throws SkipLimitExceededException {
        if(throwable instanceof CoffeeException && i < MAX_SKIP_COUNT){
            return true;
        }
        if(throwable instanceof RuntimeException && i < MAX_SKIP_COUNT){
            return true;
        }
        return false;
    }
}
