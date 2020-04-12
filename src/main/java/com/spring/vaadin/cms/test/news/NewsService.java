package com.spring.vaadin.cms.test.news;

import com.spring.vaadin.cms.service.EntityService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class NewsService implements EntityService<News> {

  private final List<News> testEntities = new ArrayList<>();

  @Override
  public Class<News> getEntityClass() {
    return News.class;
  }

  @Override
  public void delete(News entity) {
    testEntities.remove(entity);
  }

  @Override
  public News save(News entity) {
    testEntities.add(entity);
    return entity;
  }

  @Override
  public List<News> findAll() {
    return testEntities;
  }
}
