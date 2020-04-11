package com.spring.vaadin.cms.configuration;

import com.spring.vaadin.cms.factory.EntityConfigModel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import java.util.List;
import java.util.stream.Collectors;

//помоему эта страница использует дефолтный роутинг - localhost:8080/
@CssImport("./styles/shared-styles.css")
public class MainLayout extends AppLayout {

  private final List<EntityConfigModel> factoryModels;

  public MainLayout(List<EntityConfigModel> factoryModels) {
    this.factoryModels = factoryModels;
    createHeader();
    createDrawer();
  }

  private void createHeader() {
    var logo = new H1("Stoloto-Admin");
    logo.addClassName("logo");

    var header = new HorizontalLayout(new DrawerToggle(), logo);
    header.addClassName("header");
    header.setWidth("100%");
    header.setDefaultVerticalComponentAlignment(Alignment.CENTER);

    addToNavbar(header);
  }

  private void createDrawer() {
    //    var listLink = new RouterLink("List", TestListView.class);
    //    listLink.setHighlightCondition(HighlightConditions.sameLocation());

    final List<RouterLink> collect = factoryModels.stream()
        .map(entityService -> new RouterLink(entityService.getRouterName(), entityService.getAbstractListView().getClass()))
        .collect(Collectors.toList());

    addToDrawer(
        new VerticalLayout(collect.toArray(Component[]::new))
    );
  }
}
