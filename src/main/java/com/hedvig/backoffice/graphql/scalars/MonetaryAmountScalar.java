package com.hedvig.backoffice.graphql.scalars;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.springframework.stereotype.Component;

import graphql.language.ObjectField;
import graphql.language.ObjectValue;
import graphql.language.StringValue;
import graphql.language.FloatValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

@Component
public class MonetaryAmountScalar extends GraphQLScalarType {
    public MonetaryAmountScalar() {
        super("MonetaryAmount", "An object representation of `javax.money.MonetaryAmount`", new Coercing<MonetaryAmount, Map<String, Object>>() {

		@Override
		public Map<String, Object> serialize(Object dataFetcherResult) throws CoercingSerializeException {
            if (dataFetcherResult == null) {
                return null;
            }

            if (!((dataFetcherResult) instanceof MonetaryAmount)) {
                throw new CoercingSerializeException(String.format("dataFetcherResult is of wring tpye: Expected %s, got %s", MonetaryAmount.class.toString(), dataFetcherResult.getClass().toString()));
            }
            Map<String, Object> serialized = new HashMap<String, Object>();
            serialized.put("amount", ((MonetaryAmount)dataFetcherResult).getNumber().doubleValueExact());
            serialized.put("currency", ((MonetaryAmount)dataFetcherResult).getCurrency().toString());
            return serialized;
		}

		@Override
		public MonetaryAmount parseValue(Object input) throws CoercingParseValueException {
            try {
                Map<String, Object> in = (HashMap<String, Object>) input; // TODO Validate this better
                return Money.of((BigDecimal) in.get("amount"), (String) in.get("currency"));
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
                        currency = ((StringValue)field.getValue()).getValue();
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