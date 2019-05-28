package com.hedvig.backoffice.graphql.scalars;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import okhttp3.HttpUrl;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Optional;
import java.util.function.Function;

@Component
public class URLScalar extends GraphQLScalarType {

  public URLScalar() {
    super("URL", "A Url scalar", new Coercing<URL, URL>() {
      @Override
      public URL serialize(Object input) throws CoercingSerializeException {
        Optional<URL> url;
        if (input instanceof String) {
          url = Optional.of(parseURL(input.toString(), CoercingSerializeException::new));
        } else {
          url = toURL(input);
        }
        if (url.isPresent()) {
          return url.get();
        }
        throw new CoercingSerializeException(
          "Expected a 'URL' like object but was '" + input.getClass().getSimpleName() + "'."
        );
      }

      @Override
      public URL parseValue(Object input) throws CoercingParseValueException {
        if (input instanceof String) {
          return parseURL(String.valueOf(input), CoercingParseValueException::new);
        }
        Optional<URL> url = toURL(input);
        return url.orElseThrow(( ) -> new CoercingParseValueException("Expected a 'URL' like object but was '"  + input.getClass().getSimpleName() + "'."));
      }

      @Override
      public URL parseLiteral(Object input) throws CoercingParseLiteralException {
        if (!(input instanceof StringValue)) {
          throw new CoercingParseLiteralException(
            "Expected AST type 'StringValue' but was '"  + input.getClass().getSimpleName() + "'."
          );
        }
        return parseURL(((StringValue) input).getValue(), CoercingParseLiteralException::new);
      }

      private URL parseURL(String input, Function<String, RuntimeException> exceptionMaker) {
        HttpUrl httpUrl = HttpUrl.parse(input);
        if (httpUrl == null) {
          throw exceptionMaker.apply("Invalid URL value : '" + input + "'.");
        }
        return httpUrl.url();
      }
    });
  }

  private static Optional<URL> toURL(Object input) {
    if (input instanceof URL) {
      return Optional.of((URL) input);
    }
    if (input instanceof URI) {
      try {
        return Optional.of(((URI) input).toURL());
      } catch (MalformedURLException ignored) {
          return Optional.empty();
      }
    }
    if (input instanceof File) {
      try {
        return Optional.of(((File) input).toURI().toURL());
      } catch (MalformedURLException ignored) {
          return Optional.empty();
      }
    }
    return Optional.empty();
  }
}
