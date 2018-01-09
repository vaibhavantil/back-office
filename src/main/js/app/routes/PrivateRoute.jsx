import React from 'react';
import { Route, Redirect } from 'react-router';
import { checkAssetAuthorization } from '../lib/checkAuth';

/* eslint-disable react/prop-types */
const PrivateRoute = ({ component: Component, store, ...rest }) => {
    return (
        <Route
            {...rest}
            render={props =>
                checkAssetAuthorization(store) ? (
                    <Component {...props} />
                ) : (
                    <Redirect
                        to={{
                            pathname: '/login',
                            state: { from: props.location }
                        }}
                    />
                )
            }
        />
    );
};

export default PrivateRoute;
