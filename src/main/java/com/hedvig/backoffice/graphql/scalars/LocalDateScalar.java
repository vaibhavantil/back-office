package com.hedvig.backoffice.graphql.scalars;

import java.time.LocalDate;
import org.springframework.stereotype.Component;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

@Component
public class LocalDateScalar extends GraphQLScalarType {
  public LocalDateScalar() {
    super("LocalDate", "A string representation of `java.time.LocalDate`",
        new Coercing<LocalDate, String>() {

          @Override
          public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
            return null;
          }

          @Override
          public LocalDate parseValue(Object input) throws CoercingParseValueException {
            return null;
          }

          @Override
          public LocalDate parseLiteral(Object input) throws CoercingParseLiteralException {
            return null;
          }

        });
  }
}
