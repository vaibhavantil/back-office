package com.hedvig.backoffice.graphql.scalars;

import java.math.BigInteger;
import java.time.Instant;

import org.springframework.stereotype.Component;

import graphql.language.IntValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

@Component
public class InstantScalar extends GraphQLScalarType {
    public InstantScalar() {
        super("Instant", "An epoch representation of a `java.time.instant`", new Coercing<Instant, BigInteger>() {
            @Override
            public BigInteger serialize(Object dataFetcherResult) throws CoercingSerializeException {
                if (dataFetcherResult == null) {
                    return null;
                }
    
                if (!(dataFetcherResult instanceof Instant)) {
                    throw new CoercingSerializeException(String.format("dataFetcherResult is of wrong type: Expected %s, got %s", Instant.class.toString(), dataFetcherResult.getClass().toString()));
                }
    
                return BigInteger.valueOf(((Instant) dataFetcherResult).toEpochMilli());
            }
    
            @Override
            public Instant parseValue(Object input) throws CoercingParseValueException {
                try {
                    return Instant.ofEpochMilli(((BigInteger) input).longValueExact());
                } catch (Exception e) {
                    throw new CoercingParseValueException("Could not parse value", e);
                }
            }
    
            @Override
            public Instant parseLiteral(Object input) throws CoercingParseLiteralException {
                try {
                    return Instant.ofEpochMilli(((IntValue) input).getValue().longValueExact());
                } catch (Exception e) {
                    throw new CoercingParseLiteralException("Could not parse literal", e);
                }
            }
    
        });
    }
}
