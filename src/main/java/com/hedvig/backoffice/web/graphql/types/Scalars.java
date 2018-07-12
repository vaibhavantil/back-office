package com.hedvig.backoffice.web.graphql.types;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.HashMap;

import javax.money.CurrencyContextBuilder;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

import org.javamoney.moneta.CurrencyUnitBuilder;
import org.javamoney.moneta.Money;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import graphql.language.FloatValue;
import graphql.language.ObjectField;
import graphql.language.ObjectValue;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import lombok.val;

// TODO These could be improved upon a little, and maybe then published as an open source library.
@Configuration
public class Scalars {
    public static GraphQLScalarType yearMonth = new GraphQLScalarType("YearMonth", "A string representation of `java.time.YearMonth`", new Coercing<YearMonth, String>() {

		@Override
		public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
            if (dataFetcherResult == null) {
                return null;
            }

            if (!(dataFetcherResult instanceof YearMonth)) {
                throw new CoercingSerializeException(String.format("dataFetcherResult is of wrong type: Expected %s, got %s", YearMonth.class.toString(), dataFetcherResult.getClass().toString()));
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
    } );

    public static GraphQLScalarType monetaryAmount = new GraphQLScalarType("MonetaryAmount", "An object representation of javax.money.MonetaryAmount", new Coercing<MonetaryAmount, HashMap<String, Object>>() {

		@Override
		public HashMap<String, Object> serialize(Object dataFetcherResult) throws CoercingSerializeException {
            if (dataFetcherResult == null) {
                return null;
            }

            if (!((dataFetcherResult) instanceof MonetaryAmount)) {
                throw new CoercingSerializeException(String.format("dataFetcherResult is of wring tpye: Expected %s, got %s", MonetaryAmount.class.toString(), dataFetcherResult.getClass().toString()));
            }
            val serialized = new HashMap<String, Object>();
            serialized.put("amount", ((MonetaryAmount)dataFetcherResult).getNumber().doubleValueExact());
            serialized.put("currency", ((MonetaryAmount)dataFetcherResult).getCurrency().toString());
            return serialized;
		}

		@Override
		public MonetaryAmount parseValue(Object input) throws CoercingParseValueException {
            val currencyContext = CurrencyContextBuilder.of("Scalars").build();
            try {
                val in = (HashMap<String, Object>) input; // TODO Validate this better
                return Money.of((BigDecimal) in.get("amount"), CurrencyUnitBuilder.of((String) in.get("currency"), currencyContext).build());
            } catch (Exception e) {
                throw new CoercingParseValueException("Could not parse value", e);
            }
		}

		@Override
		public MonetaryAmount parseLiteral(Object input) throws CoercingParseLiteralException {
            val currencyContext = CurrencyContextBuilder.of("Scalars").build();
            try {
                val in = (ObjectValue) input;
                BigDecimal amount = null;
                CurrencyUnit currency = null;
                for (ObjectField field : in.getObjectFields()) {
                    if (field.getName().equals("amount")) {
                        amount = ((FloatValue) field.getValue()).getValue();
                    }
                    if (field.getName().equals("currency")) {
                        currency = CurrencyUnitBuilder.of(((StringValue) field.getValue()).getValue(), currencyContext).build();
                    }

                }
                return Money.of(amount, currency);
            } catch (Exception e) {
                throw new CoercingParseValueException("Could not parse value", e);
            }
		}
    });

    @Bean
    public GraphQLScalarType MonetaryAmount() {
        return Scalars.monetaryAmount;
    }

    @Bean
    public GraphQLScalarType YearMonth() {
        return Scalars.yearMonth;
    }
}