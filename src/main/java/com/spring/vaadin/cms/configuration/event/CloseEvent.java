package com.spring.vaadin.cms.configuration.event;

import com.spring.vaadin.cms.configuration.AbstractEditForm;

public class CloseEvent extends AbstractFormEvent {

  public CloseEvent(AbstractEditForm source) {
    super(source, null);
  }
}