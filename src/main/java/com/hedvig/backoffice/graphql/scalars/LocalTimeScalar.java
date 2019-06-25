package com.hedvig.backoffice.graphql.scalars;

import graphql.schema.*;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class LocalTimeScalar extends GraphQLScalarType {
  static private final DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_TIME;

  public LocalTimeScalar() {
    super("LocalTime", "A string representation of LocalTime",
      new Coercing<LocalTime, String>() {
        @Override
        public String serialize(Object o) throws CoercingSerializeException {
          return fmt.format((LocalTime)o);
        }

        @Override
        public LocalTime parseValue(Object o) throws CoercingParseValueException {
          return LocalTime.from(fmt.parse((String)o));
        }

        @Override
        public LocalTime parseLiteral(Object o) throws CoercingParseLiteralException {
          return LocalTime.from(fmt.parse((String)o));
        }
      });
  }
}
