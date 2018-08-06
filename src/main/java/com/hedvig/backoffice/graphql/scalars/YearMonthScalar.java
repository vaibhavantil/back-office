package com.hedvig.backoffice.graphql.scalars;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import java.time.YearMonth;
import org.springframework.stereotype.Component;

@Component
public class YearMonthScalar extends GraphQLScalarType {
  public YearMonthScalar() {
    super(
        "YearMonth",
        "A string-representation of `java.time.YearMonth`",
        new Coercing<YearMonth, String>() {

          @Override
          public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
            if (dataFetcherResult == null) {
              return null;
            }

            if (!(dataFetcherResult instanceof YearMonth)) {
              throw new CoercingSerializeException(
                  String.format(
                      "dataFetcherResult is of wrong type: Expected %s, got %s",
                      YearMonth.class.toString(), dataFetcherResult.getClass().toString()));
            }

            return ((YearMonth) dataFetcherResult).toString();
          }

          @Override
          public YearMonth parseValue(Object input) throws CoercingParseValueException {
            try {
              return YearMonth.parse((String) input);
            } catch (Exception e) {
              throw new CoercingParseValueException("Could not parse value", e);
            }
          }

          @Override
          public YearMonth parseLiteral(Object input) throws CoercingParseLiteralException {
            try {
              return YearMonth.parse(((StringValue) input).getValue());
            } catch (Exception e) {
              throw new CoercingParseLiteralException("Could not parse literal", e);
            }
          }
        });
  }
}
