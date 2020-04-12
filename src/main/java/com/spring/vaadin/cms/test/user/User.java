package com.spring.vaadin.cms.test.user;

import com.spring.vaadin.cms.annotation.ColumnDisplayGrid;
import com.spring.vaadin.cms.annotation.ColumnEditForm;
import lombok.Data;

@Data
public class User {

  @ColumnDisplayGrid(displayOrder = 0)
  private long id;

  @ColumnEditForm(displayOrder = 0)
  @ColumnDisplayGrid(displayOrder = 1)
  private String title;

  @ColumnEditForm(displayOrder = 1)
  @ColumnDisplayGrid(displayOrder = 2)
  private String description;

  @ColumnEditForm(displayOrder = 2)
  @ColumnDisplayGrid(displayOrder = 3)
  private String user;

}
