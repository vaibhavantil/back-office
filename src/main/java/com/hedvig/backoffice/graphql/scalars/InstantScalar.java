package com.hedvig.backoffice.graphql.scalars;

import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class InstantScalar extends GraphQLScalarType {
  public InstantScalar(ObjectMapper objectMapper) {
    super(
        "Instant",
        "An epoch representation of a `java.time.instant`",
        new Coercing<Instant, String>() {
          @Override
          public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
            try {
              return objectMapper.writeValueAsString(dataFetcherResult).replace("\"", "");
            } catch (Exception e) {
              throw new CoercingSerializeException("Unable to serialize value", e);
            }
          }

          @Override
          public Instant parseValue(Object input) throws CoercingParseValueException {
            try {
              return Instant.parse((String) input);
            } catch (Exception e) {
              throw new CoercingParseValueException("Could not parse value", e);
            }
          }

          @Override
          public Instant parseLiteral(Object input) throws CoercingParseLiteralException {
            try {
              return Instant.parse((String) input);
            } catch (Exception e) {
              throw new CoercingParseLiteralException("Could not parse literal", e);
            }
          }
        });
  }
}
