package com.spring.vaadin.cms.configuration.event;

import com.spring.vaadin.cms.configuration.AbstractEditForm;

public class SaveEvent extends AbstractFormEvent {


  public SaveEvent(AbstractEditForm<?> source, Object entity) {
    super(source, entity);
  }

}
