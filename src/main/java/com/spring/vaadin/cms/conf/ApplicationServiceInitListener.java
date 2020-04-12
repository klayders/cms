package com.spring.vaadin.cms.conf;

import com.spring.vaadin.cms.configuration.MainLayout;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApplicationServiceInitListener implements VaadinServiceInitListener {

  private final EntitiesContainer entityContainer;


  @Override
  public void serviceInit(ServiceInitEvent event) {
    if (!event.getSource().getDeploymentConfiguration().isProductionMode()) {

      var routeConfiguration = RouteConfiguration.forApplicationScope();

      entityContainer.getEntitiesBeanRouterView().forEach((routerKey, beanView ) -> {
        routeConfiguration.setRoute(routerKey, beanView.getClass(), MainLayout.class);
      });

    }
  }
}
