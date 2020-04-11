package com.spring.vaadin.cms.configuration;

import com.spring.vaadin.cms.configuration.event.CloseEvent;
import com.spring.vaadin.cms.configuration.event.DeleteEvent;
import com.spring.vaadin.cms.configuration.event.SaveEvent;
import com.spring.vaadin.cms.service.EntityService;
import com.spring.vaadin.cms.utils.ByteField;
import com.spring.vaadin.cms.utils.LongField;
import com.spring.vaadin.cms.utils.NumbersHelper;
import com.spring.vaadin.cms.utils.ShortField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

@Slf4j
public abstract class AbstractEditForm<T> extends FormLayout {

  private static final String ADMIN_I18N_PREFIX = "admin.";

  private final ApplicationContext applicationContext;

  //  private final Class<T> entityClass;
  private final Binder<T> entityBinder;
  private final EntityService<T> repository;
  private final Grid<T> grid;


  private final Button entitySave = new Button("Сохранить", event -> Notification.show("Сохранено"));
  private final Button entityDelete = new Button("Удалить", event -> Notification.show("Удалено"));
  private final Button entityClose = new Button("Закрыть", event -> Notification.show("Скрыто"));


  public AbstractEditForm(ApplicationContext applicationContext,
                          Class<T> entityClass,
                          EntityService<T> repository,
                          Grid<T> grid) {
    this.grid = grid;
    this.repository = repository;
    this.applicationContext = applicationContext;
    //    this.entityClass = entityClass;

    //    addClassName("entity-form");
    addClassName("achievement-type-form");

    entityBinder = new BeanValidationBinder<>(entityClass);

    //    collectFieldsForVaadinForm(ManagedEntity.class);
    bindFields(entityClass);

    add(createButtonLayout());

    addListener(SaveEvent.class, this::saveEntity);
    addListener(DeleteEvent.class, this::deleteEntity);
    addListener(CloseEvent.class, event -> this.closeEditor());
  }

  public void setEntity(T entity) {
    entityBinder.setBean(entity);
  }

  public void deleteEntity(DeleteEvent event) {
    repository.delete((T) event.getEntity());
    updateList();
    closeEditor();
  }

  public void saveEntity(SaveEvent event) {
    repository.save((T) event.getEntity());
    updateList();
    closeEditor();
  }

  public void editEntity(T entity) {
    if (entity == null) {
      closeEditor();
    } else {
      setEntity(entity);
      setVisible(true);
      addClassName("editing");
    }
  }

  private void updateList() {
    //    final List<AchievementType> all = repository.findAll(countSelectedRows.getValue(), versionFilter.getValue());
    var all = repository.findAll();
    grid.setItems(all);
  }

  public void closeEditor() {
    setEntity(null);
    setVisible(false);
    removeClassName("editing");
  }


  private void bindFields(Class<?> entity) {
    final var className = entity.getSimpleName();

    for (var field : entity.getDeclaredFields()) {
      if (field.getName().equals("createdById") || field.getName().equals("id")) {
        continue;
      }

      final var i18nFieldName = applicationContext.getMessage(
          ADMIN_I18N_PREFIX + className.toLowerCase() + "." + field.getName(),
          null,
          field.getName(),
          Locale.getDefault()
      );

      resolveAndBindField(field, i18nFieldName);
    }
  }

  private void resolveAndBindField(Field field, String i18nFieldName) {
    if (String.class.isAssignableFrom(field.getType())) {
      final var textField = new TextField(i18nFieldName);
      entityBinder.bind(textField, i18nFieldName);
      add(textField);
    } else if (NumbersHelper.BOOLEAN_VALUES.contains(field.getType())) {
      final var checkbox = new Checkbox(i18nFieldName);
      entityBinder.bind(checkbox, i18nFieldName);
      add(checkbox);
    } else if (NumbersHelper.FLOATING_NUMBERS.contains(field.getType())) {
      var numberField = new NumberField(i18nFieldName);
      entityBinder.bind(numberField, i18nFieldName);
      add(numberField);
    } else if (NumbersHelper.checkFieldType(field.getType(), Integer.class, int.class)) {
      var numberField = new IntegerField(i18nFieldName);
      entityBinder.bind(numberField, i18nFieldName);
      add(numberField);
    } else if (NumbersHelper.checkFieldType(field.getType(), Long.class, long.class)) {
      var longField = new LongField(i18nFieldName);
      entityBinder.bind(longField, i18nFieldName);
      add(longField);

    } else if (NumbersHelper.checkFieldType(field.getType(), Byte.class, byte.class)) {
      final var byteField = new ByteField(i18nFieldName);
      entityBinder.bind(byteField, i18nFieldName);
      add(byteField);
    } else if (NumbersHelper.checkFieldType(field.getType(), Short.class, short.class)) {
      final ShortField shortField = new ShortField(i18nFieldName);
      entityBinder.bind(shortField, i18nFieldName);
      add(shortField);
    } else if (Date.class.isAssignableFrom(field.getType())) {// Create a DateField with the default style
      //        DateField date = new DateField();
      //        vaadinField = new DateField(fieldName);
    } else {
      log.error("resolveFieldAsVaadinAbstractField: not bind field type={}", field.getType());
    }
  }


  private Component createButtonLayout() {
    entitySave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    entityDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    entityClose.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    entitySave.addClickShortcut(Key.ENTER);
    entityClose.addClickShortcut(Key.ESCAPE);

    entitySave.addClickListener(click -> validateAndSave());
    entityDelete.addClickListener(click -> fireEvent(new DeleteEvent(this, entityBinder.getBean())));
    entityClose.addClickListener(click -> fireEvent(new CloseEvent(this)));

    entityBinder.addStatusChangeListener(event -> entitySave.setEnabled(entityBinder.isValid()));

    return new HorizontalLayout(entitySave, entityDelete, entityClose);
  }

  private void validateAndSave() {
    if (entityBinder.isValid()) {
      fireEvent(new SaveEvent(this, entityBinder.getBean()));
    }
  }


}
