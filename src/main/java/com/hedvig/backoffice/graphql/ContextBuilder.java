package com.hedvig.backoffice.graphql;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import graphql.servlet.GraphQLContext;
import graphql.servlet.GraphQLContextBuilder;

public class ContextBuilder implements GraphQLContextBuilder {

	@Override
	public GraphQLContext build(Optional<HttpServletRequest> req, Optional<HttpServletResponse> resp) {
		// TODO Here we can get the information about who makes the request, so that we can attribute actions made by this person
		return null;
	}
}