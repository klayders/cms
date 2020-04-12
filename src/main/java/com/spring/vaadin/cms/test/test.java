package com.spring.vaadin.cms.test;

import java.lang.reflect.InvocationTargetException;

public class test {

  public static void main(String[] args) {
    final test instantiate = instantiate("com.spring.vaadin.cms.service.test", test.class);
    System.out.println(instantiate);
  }

  public static <T> T instantiate(final String className, final Class<T> type) {
    try {
      return type.cast(Class.forName(className).getDeclaredConstructor().newInstance());
    } catch (InstantiationException
        | IllegalAccessException
        | ClassNotFoundException
        | NoSuchMethodException
        | InvocationTargetException e) {
      throw new IllegalStateException(e);
    }
  }
}
