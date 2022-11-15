package com.training.springbatch1;

import org.springframework.batch.item.ItemProcessor;

import java.util.Locale;

public class CoffeeProcessor implements ItemProcessor<Coffee, Coffee> {
    @Override
    public Coffee process(Coffee coffee) throws Exception {
        coffee.setOrigin(coffee.getOrigin().toUpperCase(Locale.ROOT));
        return coffee;
    }
}
