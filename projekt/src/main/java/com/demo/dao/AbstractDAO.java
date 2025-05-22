package com.demo.dao;

import com.demo.models.AbstractModel;

import java.util.List;
import java.util.Optional;

public interface AbstractDAO <T extends AbstractModel>  {
    T save(T t);
    void delete(Long id);
    Optional<T> findById(Long id);
    List<T> findAll();
}
