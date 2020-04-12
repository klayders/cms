package com.spring.vaadin.cms.service;

import java.util.List;

public interface EntityService<T> {

  Class<T> getEntityClass();

  void delete(T entity);

  T save(T entity);

  List<T> findAll();
}
