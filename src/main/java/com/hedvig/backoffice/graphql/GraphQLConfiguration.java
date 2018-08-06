package com.hedvig.backoffice.graphql;

import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentation;
import java.util.List;
import lombok.val;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphQLConfiguration {
  @Bean
  DataLoaderRegistry dataLoaderRegistry(List<DataLoader<?, ?>> loaderList) {
    val registry = new DataLoaderRegistry();
    for (DataLoader<?, ?> loader : loaderList) {
      registry.register(loader.getClass().getSimpleName(), loader);
    }
    return registry;
  }

  @Bean
  Instrumentation instrumentation(DataLoaderRegistry dataLoaderRegistry) {
    return new DataLoaderDispatcherInstrumentation(dataLoaderRegistry);
  }
}
