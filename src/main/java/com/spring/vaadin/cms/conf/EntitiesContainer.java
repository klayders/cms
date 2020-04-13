package com.spring.vaadin.cms.conf;


import static java.util.stream.Collectors.toMap;

import com.spring.vaadin.cms.configuration.AbstractListView;
import com.spring.vaadin.cms.service.EntityService;
import com.spring.vaadin.cms.utils.cglib.SimpleNaming;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Getter
@Setter
@RequiredArgsConstructor
public class EntitiesContainer {

  private static final String PREFIX = "admin/";

  private final EntityAnnotationParser entityAnnotationParser;
  private final ApplicationContext context;
  private final List<EntityService<?>> entityServices;
  private final ConfigurableBeanFactory beanFactory;

  private Map<String, AbstractListView> entitiesBeanRouterView = new HashMap<>();
  private Map<Object, EntityService> entitiesServicesMap = new HashMap<>();


  @PostConstruct
  public void initView() {

    Enhancer e = new Enhancer();
    e.setClassLoader(this.getClass().getClassLoader());
    e.setSuperclass(AbstractListView.class);
    e.setUseCache(false);
    e.setCallback((MethodInterceptor) (obj, method, args, proxy) -> proxy.invokeSuper(obj, args));

    entitiesBeanRouterView = entityServices
        .stream()
        .collect(
            toMap(
                entityService -> PREFIX + entityService.getEntityClass().getSimpleName().toLowerCase(),

                entityService -> {

                  e.setNamingPolicy(new SimpleNaming(entityService.getEntityClass().getSimpleName()));

                  var myProxy2 = (AbstractListView) e.create(
                      new Class<?>[]{ApplicationContext.class, EntityService.class, EntityAnnotationParser.class},
                      new Object[]{context, entityService, entityAnnotationParser}
                  );

                  final String beanName = "view" + entityService.toString();
                  beanFactory.registerSingleton(beanName, myProxy2);

                  return myProxy2;
                },

                (o1, o2) -> {
                  log.error("initView: duplicate view={}", o1);
                  return o1;
                }
            )
        );

  }
}