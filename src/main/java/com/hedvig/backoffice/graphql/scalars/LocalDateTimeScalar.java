package com.hedvig.backoffice.graphql.scalars;

import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class LocalDateTimeScalar extends GraphQLScalarType {
  static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

  public LocalDateTimeScalar() {
    super("LocalDateTime",
      "An string representation of LocalDateTime",
      new Coercing<LocalDateTime, String>() {
        @Override
        public String serialize(Object data) throws CoercingSerializeException {
          return fmt.format((LocalDateTime)data);
        }

        @Override
        public LocalDateTime parseValue(Object input) throws CoercingParseValueException {
          return LocalDateTime.from(fmt.parse((String)input));
        }

        @Override
        public LocalDateTime parseLiteral(Object input) throws CoercingParseLiteralException {
          return LocalDateTime.from(fmt.parse((String)input));
        }
      });
  }
}
