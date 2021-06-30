package com.example.orderservice.common;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapUtils {

    private static ModelMapper modelMapper;
    private final ModelMapper beanMapper;

    public static <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {

        return source
            .stream()
            .map(element -> modelMapper.map(element, targetClass))
            .collect(Collectors.toList());
    }

    @PostConstruct
    private void init() {
        modelMapper = beanMapper;
    }

}
