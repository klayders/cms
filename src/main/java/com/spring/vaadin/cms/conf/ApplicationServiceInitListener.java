package com.spring.vaadin.cms.conf;

import com.spring.vaadin.cms.configuration.MainLayout;
import com.spring.vaadin.cms.factory.EntityConfigModel;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import java.util.List;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationServiceInitListener<T> implements VaadinServiceInitListener {

  private final List<EntityConfigModel<T>> factoryModels;

  public ApplicationServiceInitListener(List<EntityConfigModel<T>> factoryModels) {
    this.factoryModels = factoryModels;
  }

  @Override
  public void serviceInit(ServiceInitEvent event) {
    if (!event.getSource().getDeploymentConfiguration().isProductionMode()) {

      var routeConfiguration = RouteConfiguration.forApplicationScope();

      factoryModels.forEach(entityFactoryModel -> {
        routeConfiguration.setRoute(entityFactoryModel.getRouterName(), entityFactoryModel.getAbstractListView().getClass(), MainLayout.class);
      });

    }
  }
}
