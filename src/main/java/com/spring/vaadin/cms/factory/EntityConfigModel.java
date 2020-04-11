package com.spring.vaadin.cms.factory;

import com.spring.vaadin.cms.configuration.AbstractListView;

public class EntityConfigModel<T> {

  private String routerName;
  private AbstractListView<T> abstractListView;


  public EntityConfigModel(String routerName, AbstractListView<T> abstractListView) {
    this.routerName = routerName;
    this.abstractListView = abstractListView;
  }


  public String getRouterName() {
    return routerName;
  }

  public void setRouterName(String routerName) {
    this.routerName = routerName;
  }

  public AbstractListView<T> getAbstractListView() {
    return abstractListView;
  }

  public void setAbstractListView(AbstractListView<T> abstractListView) {
    this.abstractListView = abstractListView;
  }
}
