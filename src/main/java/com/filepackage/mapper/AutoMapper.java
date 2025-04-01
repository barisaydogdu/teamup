package com.filepackage.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class AutoMapper {
    private final ModelMapper modelMapper;

    public AutoMapper() {this.modelMapper = new ModelMapper();
    }

    public <D, T> D convertToDto(T entity, Class<D> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }

    public <T, D> T convertToEntity(D dto, Class<T> entityClass) {
        return modelMapper.map(dto, entityClass);
    }

    public void map(Object source, Object destination) {
        modelMapper.map(source,destination);
    }
}
