package com.filepackage.service;

import java.util.List;

public interface IBaseService <E,ID>{
    E getById(ID id);
    List<E> getAll();
    void delete(ID id);
    E update(ID id, E updatedEntity);
    E create(E dto);
}
