package com.hedvig.backoffice.graphql.scalars;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.language.FloatValue;
import graphql.language.ObjectField;
import graphql.language.ObjectValue;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import java.math.BigDecimal;
import java.util.Map;
import javax.money.MonetaryAmount;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Component;

@Component
public class MonetaryAmountScalar extends GraphQLScalarType {
  public MonetaryAmountScalar(ObjectMapper objectMapper) {
    super(
        "MonetaryAmount",
        "An object representation of `javax.money.MonetaryAmount`",
        new Coercing<MonetaryAmount, Map<String, Object>>() {

          @Override
          public Map<String, Object> serialize(Object dataFetcherResult)
              throws CoercingSerializeException {
            try {
              return objectMapper.convertValue(
                  dataFetcherResult, new TypeReference<Map<String, Object>>() {});
            } catch (Exception e) {
              throw new CoercingSerializeException("Could not serialize value", e);
            }
          }

          @SuppressWarnings("unchecked")
          @Override
          public MonetaryAmount parseValue(Object input) throws CoercingParseValueException {
            try {
              Map<String, Object> in = (Map<String, Object>) input;
              return Money.of(
                  BigDecimal.valueOf((Integer) in.get("amount")), (String) in.get("currency"));
            } catch (Exception e) {
              throw new CoercingParseValueException("Could not parse value", e);
            }
          }

          @Override
          public MonetaryAmount parseLiteral(Object input) throws CoercingParseLiteralException {
            try {
              ObjectValue in = (ObjectValue) input;
              BigDecimal amount = null;
              String currency = null;
              for (ObjectField field : in.getObjectFields()) {
                if (field.getName().equals("amount")) {
                  amount = ((FloatValue) field.getValue()).getValue();
                }
                if (field.getName().equals("currency")) {
                  currency = ((StringValue) field.getValue()).getValue();
                }
              }
              return Money.of(amount, currency);
            } catch (Exception e) {
              throw new CoercingParseValueException("Could not parse value", e);
            }
          }
        });
  }
}
