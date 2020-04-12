package com.spring.vaadin.cms.configuration;

import com.spring.vaadin.cms.conf.EntityAnnotationParser;
import com.spring.vaadin.cms.service.EntityService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.value.ValueChangeMode;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.stream.Stream;
import lombok.Builder;
import org.springframework.context.ApplicationContext;


//@PageTitle("Медаль | Столото cms")
public abstract class AbstractListView<T> extends VerticalLayout {

  private final ApplicationContext applicationContext;
  private final Class<T> entity;
  private final AbstractEditForm<T> entityForm;
  private final EntityService<T> entityService;
  private final Grid<T> entityGrid;
  private final EntityAnnotationParser entityAnnotationParser;
  private final IntegerField versionFilter = new IntegerField("Фильтр по версии", null, event -> updateList());
  private final IntegerField countSelectedRows = new IntegerField("Число записей на странице:", 50, event -> updateList());


  public AbstractListView(ApplicationContext applicationContext,
                          EntityService<T> entityService,
                          EntityAnnotationParser entityAnnotationParser) {
    this.applicationContext = applicationContext;
    this.entityService = entityService;
    this.entity = entityService.getEntityClass();
    this.entityGrid = new Grid<>(entityService.getEntityClass());
    this.entityAnnotationParser = entityAnnotationParser;
    this.entityForm = new AbstractEditForm<>(this.applicationContext, this.entity, this.entityService, this.entityGrid, this.entityAnnotationParser) {};

    //    entityForm = new TestForm(imageService.findAll(), applicationContext);

    var content = new Div(entityGrid, entityForm);
    content.addClassName("content");
    content.setSizeFull();

    addClassName("list-view");
    setSizeFull();

    configureGrid();
    getToolbar();

    add(getToolbar(), content);

    updateList();

    entityForm.closeEditor();
  }


  private HorizontalLayout getToolbar() {
    versionFilter.setMaxWidth("150px");
    versionFilter.setClearButtonVisible(true);
    versionFilter.setValueChangeMode(ValueChangeMode.LAZY);

    if (countSelectedRows.getValue() == null || countSelectedRows.getValue() <= 0) {
      countSelectedRows.setValue(50);
    }
    countSelectedRows.setMaxWidth("150px");
    countSelectedRows.setClearButtonVisible(true);
    countSelectedRows.setValueChangeMode(ValueChangeMode.LAZY);

    var createEntityButton = new Button("Создать", click -> createEntity());

    var toolBar = new HorizontalLayout(versionFilter, countSelectedRows, createEntityButton);
    toolBar.setClassName("toolbar");

    return toolBar;

  }

  private void configureGrid() {
    entityGrid.addClassName("main-grid");
    entityGrid.setSizeFull();
    //      grid.setColumns("type", "shortDescription", "row", "position", "description", "fullDescription", "smallPicId");

    var gridFields = Stream.of(entity.getDeclaredFields())
        .filter(field -> field.getAnnotation(entityAnnotationParser.annotationGridColumn()) != null)
        .sorted((field1, field2) -> {
          var annotation1 = field1.getAnnotation(entityAnnotationParser.annotationGridColumn());
          var order1 = (Integer) entityAnnotationParser.getGridOrderByAnnotation(annotation1);

          var annotation2 = field2.getAnnotation(entityAnnotationParser.annotationGridColumn());
          var order2 = (Integer) entityAnnotationParser.getGridOrderByAnnotation(annotation2);

          return order1.compareTo(order2);
        })
        .map(Field::getName)
        .toArray(String[]::new);

    entityGrid.setColumns(gridFields);

    entityGrid.getColumns().forEach(column -> {
      column.setAutoWidth(true);
      var fieldName = column.getKey();
      final var i18nFieldName = applicationContext.getMessage(
          "admin." + entity.getSimpleName().toLowerCase() + "." + fieldName,
          null,
          fieldName,
          Locale.getDefault()
      );
      column.setHeader(i18nFieldName);
    });

    entityGrid.asSingleSelect().addValueChangeListener(event -> entityForm.editEntity(event.getValue()));
  }

  private void updateList() {
    var all = entityService.findAll();
    entityGrid.setItems(all);
  }

  private void createEntity() {
    entityGrid.asSingleSelect().clear();
    T entity = null;
    try {
      entity = this.entity.getDeclaredConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      e.printStackTrace();
    }
    entityForm.editEntity(entity);
  }
}
