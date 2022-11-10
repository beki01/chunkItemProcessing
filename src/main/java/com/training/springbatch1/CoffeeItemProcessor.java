package com.training.springbatch1;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.springframework.batch.item.ItemProcessor;

import java.util.Locale;

public class CoffeeItemProcessor implements ItemProcessor<Coffee, Coffee> {
    private static final Logger log = LoggerFactory.getLogger(CoffeeItemProcessor.class);


    @Override
    public Coffee process(Coffee coffee) throws Exception {
        final String blend = coffee.getBlend().toUpperCase(Locale.ROOT);
        final String strength = coffee.getStrength().toUpperCase(Locale.ROOT);
        final String origin = coffee.getOrigin().toUpperCase(Locale.ROOT);

        final Coffee upperCaseCoffee = new Coffee(blend, strength,origin);

        log.info("Processing coffee from: (" +coffee+") to " + upperCaseCoffee);
        return upperCaseCoffee;
    }
}
