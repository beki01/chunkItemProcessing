package com.training.springbatch1;

import org.springframework.batch.item.ItemProcessor;

import java.util.Locale;
import java.util.Objects;

public class CoffeeProcessor implements ItemProcessor<Coffee, Coffee> {
    @Override
    public Coffee process(Coffee coffee) throws Exception {
        if(coffee.getOrigin().isEmpty() || Objects.equals(coffee.getOrigin(), "NA")) throw new CoffeeException("Coffee doesn't have origin");
        coffee.setOrigin(coffee.getOrigin().toUpperCase(Locale.ROOT));
        return coffee;
    }
}
