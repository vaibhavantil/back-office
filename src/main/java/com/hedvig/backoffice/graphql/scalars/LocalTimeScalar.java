package com.hedvig.backoffice.graphql.scalars;

import graphql.language.StringValue;
import graphql.schema.*;
import java.time.LocalDate;
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
          if (o instanceof LocalTime) {
            return fmt.format((LocalTime) o);
          } else {
            throw new CoercingSerializeException("Could not serialize :" + o.toString() + "as a LocalTime");
          }
        }

        @Override
        public LocalTime parseValue(Object input) throws CoercingParseValueException {
          try {
            return LocalTime.parse((String) input);
          } catch (Exception e) {
            throw new CoercingParseValueException("Could not parse value as a Local Time", e);
          }
        }

        @Override
        public LocalTime parseLiteral(Object input) throws CoercingParseLiteralException {

          try {

            return LocalTime.parse(((StringValue) input).getValue());
          } catch (Exception e) {
            throw new CoercingParseLiteralException("Could parse literal as Local Time", e);
          }
        }
      });
  }
}
