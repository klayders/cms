package com.spring.vaadin.cms.conf;

import com.spring.vaadin.cms.configuration.AbstractListView;
import com.spring.vaadin.cms.factory.EntityConfigModel;
import com.spring.vaadin.cms.service.EntityService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EntityContainer {

  private static final String PREFIX = "admin/";
  private final ApplicationContext applicationContext;

  public EntityContainer(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }


  @Bean
  public <T> List<EntityConfigModel<T>> factoryModels(List<EntityService<T>> entityServices, EntityAnnotationParser entityAnnotationParser) {
    return entityServices.stream()
        .map(entityService -> {
          final var routerKey = PREFIX + entityService.getEntityClass().getSimpleName().toLowerCase();
          final var abstractListView = new AbstractListView<>(applicationContext, entityService, entityAnnotationParser) {};
          return new EntityConfigModel<T>(routerKey, abstractListView);
        })
        .collect(Collectors.toList());
  }

}
