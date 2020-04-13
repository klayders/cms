package com.spring.vaadin.cms.test.entity;

import com.spring.vaadin.cms.service.EntityService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TestEntityService implements EntityService<TestEntity> {

  private final List<TestEntity> testEntities = new ArrayList<>();

  public TestEntityService() {
  }

  @Override
  public Class<TestEntity> getEntityClass() {
    return TestEntity.class;
  }

  @Override
  public void delete(TestEntity entity) {
    testEntities.remove(entity);
  }

  @Override
  public TestEntity save(TestEntity entity) {
    testEntities.add(entity);
    return entity;
  }

  @Override
  public List<TestEntity> findAll() {
    return testEntities;
  }
}
