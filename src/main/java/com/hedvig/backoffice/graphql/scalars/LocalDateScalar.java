package com.hedvig.backoffice.graphql.scalars;

import java.time.LocalDate;
import org.springframework.stereotype.Component;
import graphql.language.StringValue;
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
            if (dataFetcherResult == null) {
              return null;
            }

            if (!(dataFetcherResult instanceof LocalDate)) {
              throw new CoercingSerializeException(
                  String.format("dataFetcherResult is of wrong type: Expected %s, got %s",
                      LocalDate.class.toString(), dataFetcherResult.getClass().toString()));
            }

            return ((LocalDate) dataFetcherResult).toString();
          }

          @Override
          public LocalDate parseValue(Object input) throws CoercingParseValueException {
            try {
              return LocalDate.parse((String) input);
            } catch (Exception e) {
              throw new CoercingParseValueException("Could not parse value", e);
            }
          }

          @Override
          public LocalDate parseLiteral(Object input) throws CoercingParseLiteralException {
            try {
              return LocalDate.parse(((StringValue) input).getValue());
            } catch (Exception e) {
              throw new CoercingParseLiteralException("Could not parse literal", e);
            }
          }
        });
  }
}
