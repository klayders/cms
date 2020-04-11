package com.spring.vaadin.cms.service;

import com.spring.vaadin.cms.annotation.ColumnDisplayGrid;

public class TestEntity {

  @ColumnDisplayGrid(displayOrder = 0)
  private int id;
  @ColumnDisplayGrid(displayOrder = 1)
  private String name;
  @ColumnDisplayGrid(displayOrder = 2)
  private String description;
  @ColumnDisplayGrid(displayOrder = 3)
  private Short number;
  private Integer number1;
  private Long number2;
  private Double number3;
  private Float number4;
  private boolean check;

  public TestEntity() {
  }

  public TestEntity(int id, String name, String description, Short number, Integer number1, Long number2, Double number3, Float number4, boolean check) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.number = number;
    this.number1 = number1;
    this.number2 = number2;
    this.number3 = number3;
    this.number4 = number4;
    this.check = check;
  }

  public Float getNumber4() {
    return number4;
  }

  public void setNumber4(Float number4) {
    this.number4 = number4;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Short getNumber() {
    return number;
  }

  public void setNumber(Short number) {
    this.number = number;
  }

  public Integer getNumber1() {
    return number1;
  }

  public void setNumber1(Integer number1) {
    this.number1 = number1;
  }

  public Long getNumber2() {
    return number2;
  }

  public void setNumber2(Long number2) {
    this.number2 = number2;
  }

  public Double getNumber3() {
    return number3;
  }

  public void setNumber3(Double number3) {
    this.number3 = number3;
  }

  public boolean isCheck() {
    return check;
  }

  public void setCheck(boolean check) {
    this.check = check;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
