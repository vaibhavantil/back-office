package com.hedvig.backoffice.graphql;

import graphql.servlet.GraphQLContext;
import java.security.Principal;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GraphQLRequestContext extends GraphQLContext {
  private final Principal userPrincipal;

  public GraphQLRequestContext(
      Optional<HttpServletRequest> request,
      Optional<HttpServletResponse> response,
      Principal userPrincipal) {
    super(request, response);
    this.userPrincipal = userPrincipal;
  }

  public Principal getUserPrincipal() {
    return userPrincipal;
  }
}
