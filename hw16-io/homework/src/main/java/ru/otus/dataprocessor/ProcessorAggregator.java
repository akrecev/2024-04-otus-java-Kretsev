package ru.otus.dataprocessor;

import java.util.*;
import ru.otus.model.Measurement;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        Map<String, Double> result = new TreeMap<>();
        for (Measurement datum : data) {
            result.compute(datum.name(), (k, v) -> v == null ? datum.value() : v + datum.value());
        }
        return result;
    }
}
