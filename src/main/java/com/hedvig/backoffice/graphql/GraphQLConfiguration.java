package com.hedvig.backoffice.graphql;

import com.coxautodev.graphql.tools.ObjectMapperConfigurer;
import com.coxautodev.graphql.tools.ObjectMapperConfigurerContext;
import com.coxautodev.graphql.tools.SchemaParserDictionary;
import com.coxautodev.graphql.tools.SchemaParserOptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedvig.backoffice.BackOfficeApplication;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentation;
import graphql.scalars.ExtendedScalars;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLScalarType;
import lombok.val;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.List;

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

  public static String getIdToken(DataFetchingEnvironment env, PersonnelService personnelService) {
    GraphQLRequestContext context = env.getContext();
    return personnelService.getIdToken(context.getUserPrincipal().getName());
  }

  public static String getEmail(DataFetchingEnvironment env, PersonnelService personnelService)
    throws AuthorizationException {
    GraphQLRequestContext context = env.getContext();
    val personnel = personnelService.getPersonnelByEmail(context.getUserPrincipal().getName());
    return personnel.getEmail();
  }

  @Bean
  SchemaParserDictionary schemaParserDictionary() throws ClassNotFoundException {

    val schemaParserDictionary = new SchemaParserDictionary();

    val scanner = new ClassPathScanningCandidateComponentProvider(false);
    scanner.addIncludeFilter(new AnnotationTypeFilter(UnionType.class));

    for (val beanDefinition : scanner
      .findCandidateComponents(BackOfficeApplication.class.getPackage().getName())) {
      schemaParserDictionary.add(Class.forName(beanDefinition.getBeanClassName()));
    }

    return schemaParserDictionary;
  }

  @Bean
  SchemaParserOptions schemaParserOptions() {
    return SchemaParserOptions.newOptions().objectMapperConfigurer(new ObjectMapperConfigurer() {

      @Override
      public void configure(ObjectMapper mapper, ObjectMapperConfigurerContext context) {
        mapper.findAndRegisterModules();
      }
    }).build();
  }

  @Bean
  public GraphQLScalarType jsonType() {
    return ExtendedScalars.Json;
  }
}
