package com.spring.vaadin.cms.configuration.event;

import com.spring.vaadin.cms.configuration.AbstractEditForm;

public class DeleteEvent extends AbstractFormEvent {

  public DeleteEvent(AbstractEditForm<?> source, Object entity) {
    super(source, entity);
  }
}
