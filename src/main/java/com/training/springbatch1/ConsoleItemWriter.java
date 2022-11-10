package com.training.springbatch1;

import org.springframework.batch.item.support.AbstractItemStreamItemWriter;

import java.util.List;

public class ConsoleItemWriter extends AbstractItemStreamItemWriter<Coffee> {
    @Override
    public void write(List<? extends Coffee> list) throws Exception {
        list.stream().forEach(System.out::println);
        System.out.println("Chunk");
    }
}
