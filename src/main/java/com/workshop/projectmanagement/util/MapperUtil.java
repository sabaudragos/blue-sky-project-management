package com.workshop.projectmanagement.util;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapperUtil {
    private Mapper mapper;

    public MapperUtil() {
        this.mapper = DozerBeanMapperBuilder.buildDefault();

    }

    public <T, U> U map(T source, Class<U> destinationClass) {
        return mapper.map(source, destinationClass);
    }

    public <T, U> List<U> mapList(List<T> source, Class<U> destinationClass) {

        return source
                .stream()
                .map(item -> mapper.map(item, destinationClass))
                .collect(Collectors.toList());
    }
}
