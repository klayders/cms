package com.spring.vaadin.cms.test.user;

import com.spring.vaadin.cms.service.EntityService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class UserService implements EntityService<User> {

  private final List<User> testEntities = new ArrayList<>();

  @Override
  public Class<User> getEntityClass() {
    return User.class;
  }

  @Override
  public void delete(User entity) {
    testEntities.remove(entity);
  }

  @Override
  public User save(User entity) {
    testEntities.add(entity);
    return entity;
  }

  @Override
  public List<User> findAll() {
    return testEntities;
  }
}
