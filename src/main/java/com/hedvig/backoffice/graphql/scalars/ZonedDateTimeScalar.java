package com.hedvig.backoffice.graphql.scalars;

import java.time.ZonedDateTime;
import org.springframework.stereotype.Component;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

@Component
public class ZonedDateTimeScalar extends GraphQLScalarType {
  public ZonedDateTimeScalar() {
    super("ZonedDateTime", "A string-representation of `java.time.ZonedDateTime`",
        new Coercing<ZonedDateTime, String>() {

          @Override
          public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
            if (dataFetcherResult == null) {
              return null;
            }

            if (!(dataFetcherResult instanceof ZonedDateTime)) {
              throw new CoercingSerializeException(
                  String.format("dataFetcherResult is of wrong type: Expected %s, got %s",
                      ZonedDateTime.class.toString(), dataFetcherResult.getClass().toString()));
            }

            return ((ZonedDateTime) dataFetcherResult).toString();
          }

          @Override
          public ZonedDateTime parseValue(Object input) throws CoercingParseValueException {
            try {
              return ZonedDateTime.parse((String) input);
            } catch (Exception e) {
              throw new CoercingParseValueException("Could not parse value", e);
            }
          }

          @Override
          public ZonedDateTime parseLiteral(Object input) throws CoercingParseLiteralException {
            try {
              return ZonedDateTime.parse(((StringValue) input).getValue());
            } catch (Exception e) {
              throw new CoercingParseLiteralException("Could not parse literal", e);
            }
          }
        });
  }
}
