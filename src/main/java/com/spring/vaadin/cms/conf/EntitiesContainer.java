package com.spring.vaadin.cms.conf;


import static java.util.stream.Collectors.toMap;

import com.spring.vaadin.cms.configuration.AbstractListView;
import com.spring.vaadin.cms.service.EntityService;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.GenericWebApplicationContext;

@Slf4j
@Component
@Getter
@Setter
@RequiredArgsConstructor
public class EntitiesContainer {

  private static final String PREFIX = "admin/";

  private final EntityAnnotationParser entityAnnotationParser;
  private final GenericWebApplicationContext context;

  private Map<String, AbstractListView> entitiesBeanRouterView = new HashMap<>();
  private Map<Object, EntityService> entitiesServicesMap = new HashMap<>();

  @PostConstruct
  public void initView() {
    entitiesBeanRouterView = context.getBeansOfType(EntityService.class).values()
        .stream()
        .collect(
            toMap(
                entityService -> PREFIX + entityService.getEntityClass().getSimpleName().toLowerCase(),

                entityService -> {
                  final var abstractListView = new AbstractListView(context,  entityService, entityAnnotationParser) {};
                  context.registerBean("view" + entityService.toString(), AbstractListView.class, () -> abstractListView);
                  return abstractListView;
                },

                (o1, o2) -> {
                  log.error("initView: duplicate view={}", o1);
                  return o1;
                }
            )
        );


  }


}
