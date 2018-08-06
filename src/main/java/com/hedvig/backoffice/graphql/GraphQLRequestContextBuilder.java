package com.hedvig.backoffice.graphql;

import graphql.servlet.GraphQLContext;
import graphql.servlet.GraphQLContextBuilder;
import java.security.Principal;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class GraphQLRequestContextBuilder implements GraphQLContextBuilder {

  @Override
  public GraphQLContext build(
      Optional<HttpServletRequest> req, Optional<HttpServletResponse> resp) {
    Principal userPrincipal = null;
    if (req.isPresent()) {
      userPrincipal = req.get().getUserPrincipal();
    }
    return new GraphQLRequestContext(req, resp, userPrincipal);
  }
}
