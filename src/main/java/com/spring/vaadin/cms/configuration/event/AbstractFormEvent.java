package com.spring.vaadin.cms.configuration.event;

import com.spring.vaadin.cms.configuration.AbstractEditForm;
import com.vaadin.flow.component.ComponentEvent;

public abstract class AbstractFormEvent extends ComponentEvent<AbstractEditForm<?>> {

  private final Object entity;

  protected AbstractFormEvent(AbstractEditForm<?> source, Object entity) {
    super(source, false);
    this.entity = entity;
  }

  public Object getEntity() {
    return entity;
  }
}
