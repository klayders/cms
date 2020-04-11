package com.spring.vaadin.cms.conf;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class EntityAnnotationParser {

  @Value("${admin.view.annotation.entity.field.grid.column.class}")
  private String annotationGrid;
  @Value("${admin.view.annotation.entity.field.grid.column.order.method}")
  private String orderMethod;

  @Bean
  public Class<?> annotationGridColumn()  {
    try {
      return Class.forName(annotationGrid);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      throw new RuntimeException("can`t load grid annotation, not found [" + annotationGrid + ".class]");
    }
  }

  public Object getGridOrderByAnnotation(Annotation annotation) {
    try {
      return annotation.annotationType().getMethod(orderMethod).invoke(annotation);
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      e.printStackTrace();
    }
    return null;
  }

}
